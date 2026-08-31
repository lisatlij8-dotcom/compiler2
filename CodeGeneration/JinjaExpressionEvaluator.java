package CodeGeneration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Evaluates the small subset of Jinja expression syntax our grammar
 * captures as compact strings (e.g. "product.name", "products|length",
 * "products|length==0"): dotted property paths, the "length" filter,
 * "==" / "!=" comparisons, "+" string concatenation, and a focused
 * url_for(...) call form, against a rendering scope (template context plus
 * any active loop variables).
 *
 * This is intentionally not a general Jinja2 expression engine - only what
 * the real demo_flask templates actually use. Anything outside that raises
 * CodeGenerationException instead of guessing.
 */
public class JinjaExpressionEvaluator {

    private Map<String, String> routesByEndpoint = Map.of();

    /**
     * Supplies the Flask endpoint (function name) -> route path template
     * mapping (e.g. "product_details" -> "/product/<int:product_id>") so
     * url_for(...) calls can be resolved without ever running Flask/Jinja.
     */
    public void configureRoutes(Map<String, String> routesByEndpoint) {
        this.routesByEndpoint = routesByEndpoint != null ? routesByEndpoint : Map.of();
    }

    public Object evaluate(String expression, Map<String, Object> scope) {
        String expr = expression == null ? "" : expression.trim();
        if (expr.isEmpty()) {
            throw new CodeGenerationException("Empty Jinja expression");
        }

        if (expr.startsWith("url_for(") && expr.endsWith(")")) {
            return evaluateUrlFor(expr, scope);
        }

        // "+" is only used in the real templates for string concatenation
        // inside url_for(...) arguments (e.g. 'uploads/' + product.image),
        // but is handled generally here so it also works in {{ }} text.
        List<String> concatParts = splitTopLevel(expr, '+');
        if (concatParts.size() > 1) {
            StringBuilder result = new StringBuilder();
            for (String part : concatParts) {
                Object value = evaluate(part.trim(), scope);
                result.append(value == null ? "" : String.valueOf(value));
            }
            return result.toString();
        }

        String path = expr;
        String filter = null;
        int pipeIndex = expr.indexOf('|');
        if (pipeIndex >= 0) {
            path = expr.substring(0, pipeIndex).trim();
            filter = expr.substring(pipeIndex + 1).trim();
        }

        Object value = evaluatePath(path, scope);
        return filter != null ? applyFilter(filter, value, expr, scope) : value;
    }

    private Object evaluateUrlFor(String expr, Map<String, Object> scope) {
        String argsText = expr.substring("url_for(".length(), expr.length() - 1);
        List<String> args = splitTopLevel(argsText, ',');
        if (args.size() == 1 && args.get(0).trim().isEmpty()) {
            args = List.of();
        }
        if (args.isEmpty()) {
            throw new CodeGenerationException("url_for() requires an endpoint name in '" + expr + "'");
        }

        Object endpointValue = evaluate(args.get(0).trim(), scope);
        if (!(endpointValue instanceof String)) {
            throw new CodeGenerationException("url_for() endpoint must be a string literal in '" + expr + "'");
        }
        String endpoint = (String) endpointValue;

        Map<String, Object> kwargs = new LinkedHashMap<>();
        for (int i = 1; i < args.size(); i++) {
            String arg = args.get(i).trim();
            int eq = arg.indexOf('=');
            if (eq < 0) {
                throw new CodeGenerationException(
                        "url_for() only supports keyword arguments after the endpoint in '" + expr + "'");
            }
            String name = arg.substring(0, eq).trim();
            Object value = evaluate(arg.substring(eq + 1).trim(), scope);
            kwargs.put(name, value);
        }

        if ("static".equals(endpoint)) {
            Object filename = kwargs.get("filename");
            if (filename == null) {
                throw new CodeGenerationException(
                        "url_for('static', ...) requires a 'filename' argument in '" + expr + "'");
            }
            return "/static/" + filename;
        }

        String pathTemplate = routesByEndpoint.get(endpoint);
        if (pathTemplate == null) {
            throw new CodeGenerationException("Unknown Flask endpoint '" + endpoint + "' in url_for()");
        }
        return substituteRouteParameters(pathTemplate, kwargs, expr);
    }

    private String substituteRouteParameters(String pathTemplate, Map<String, Object> kwargs, String originalExpr) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < pathTemplate.length()) {
            char c = pathTemplate.charAt(i);
            if (c == '<') {
                int end = pathTemplate.indexOf('>', i);
                if (end < 0) {
                    throw new CodeGenerationException("Malformed route pattern '" + pathTemplate + "'");
                }
                String placeholder = pathTemplate.substring(i + 1, end);
                String paramName = placeholder.contains(":")
                        ? placeholder.substring(placeholder.indexOf(':') + 1)
                        : placeholder;
                if (!kwargs.containsKey(paramName)) {
                    throw new CodeGenerationException(
                            "url_for() is missing required route parameter '" + paramName + "' in '" + originalExpr + "'");
                }
                result.append(formatRouteValue(kwargs.get(paramName)));
                i = end + 1;
            } else {
                result.append(c);
                i++;
            }
        }
        return result.toString();
    }

    private Object formatRouteValue(Object value) {
        if (value instanceof Double) {
            double d = (Double) value;
            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                return (long) d;
            }
        }
        return value;
    }

    /**
     * Splits on a top-level occurrence of the given operator/separator
     * character, ignoring one inside quotes or nested parentheses.
     */
    private List<String> splitTopLevel(String text, char separator) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\'' && !inDoubleQuote) {
                inSingleQuote = !inSingleQuote;
            } else if (c == '"' && !inSingleQuote) {
                inDoubleQuote = !inDoubleQuote;
            } else if (!inSingleQuote && !inDoubleQuote) {
                if (c == '(') {
                    depth++;
                } else if (c == ')') {
                    depth--;
                } else if (c == separator && depth == 0) {
                    parts.add(text.substring(start, i));
                    start = i + 1;
                }
            }
        }
        parts.add(text.substring(start));
        return parts;
    }

    public boolean evaluateCondition(String expression, Map<String, Object> scope) {
        String expr = expression == null ? "" : expression.trim();

        String operator = null;
        int operatorIndex = -1;
        for (String candidate : new String[] {"==", "!="}) {
            int idx = expr.indexOf(candidate);
            if (idx >= 0) {
                operator = candidate;
                operatorIndex = idx;
                break;
            }
        }

        if (operator == null) {
            return isTruthy(evaluateOrUndefined(expr, scope));
        }

        Object left = evaluateOrUndefined(expr.substring(0, operatorIndex).trim(), scope);
        Object right = evaluateOrUndefined(expr.substring(operatorIndex + operator.length()).trim(), scope);
        boolean equal = valuesEqual(left, right);
        return "==".equals(operator) ? equal : !equal;
    }

    /**
     * Jinja's default Undefined is falsy rather than an error when tested in
     * a boolean context (e.g. "{% if error %}" when "error" was never passed
     * to render_template) - the same semantics WebSemanticVisitor already
     * relies on to treat guard-only template variables as optional context.
     * Any other CodeGenerationException (e.g. a genuinely unsupported
     * filter) still propagates normally rather than being silenced.
     */
    private Object evaluateOrUndefined(String expr, Map<String, Object> scope) {
        try {
            return evaluate(expr, scope);
        } catch (CodeGenerationException e) {
            String message = e.getMessage();
            if (message != null
                    && (message.startsWith("Undefined template variable '") || message.startsWith("Undefined property '"))) {
                return null;
            }
            throw e;
        }
    }

    private Object evaluatePath(String path, Map<String, Object> scope) {
        if (path.matches("-?\\d+(\\.\\d+)?")) {
            return path.contains(".") ? (Object) Double.parseDouble(path) : (Object) Long.parseLong(path);
        }
        if (path.length() >= 2
                && ((path.startsWith("\"") && path.endsWith("\"")) || (path.startsWith("'") && path.endsWith("'")))) {
            return path.substring(1, path.length() - 1);
        }

        String[] segments = path.split("\\.");
        if (segments.length == 0 || segments[0].isEmpty()) {
            throw new CodeGenerationException("Invalid Jinja expression path: '" + path + "'");
        }
        if (!scope.containsKey(segments[0])) {
            throw new CodeGenerationException("Undefined template variable '" + segments[0] + "'");
        }

        Object current = scope.get(segments[0]);
        for (int i = 1; i < segments.length; i++) {
            if (!(current instanceof Map)) {
                throw new CodeGenerationException(
                        "Cannot look up property '" + segments[i] + "' on a non-object value in '" + path + "'");
            }
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) current;
            if (!map.containsKey(segments[i])) {
                throw new CodeGenerationException("Undefined property '" + segments[i] + "' in '" + path + "'");
            }
            current = map.get(segments[i]);
        }
        return current;
    }

    private Object applyFilter(String filter, Object value, String originalExpression, Map<String, Object> scope) {
        if ("e".equals(filter) || "escape".equals(filter)) {
            return escapeHtml(String.valueOf(value));
        }
        if ("length".equals(filter)) {
            if (value instanceof List) {
                return ((List<?>) value).size();
            }
            if (value instanceof Map) {
                return ((Map<?, ?>) value).size();
            }
            if (value instanceof String) {
                return ((String) value).length();
            }
            throw new CodeGenerationException(
                    "Filter 'length' is not supported for the value type of '" + originalExpression + "'");
        }
        // Handles the "%.2f"|format(product.price) form (Python/Jinja's
        // %-style string formatting invoked as a filter).
        if (filter.startsWith("format(") && filter.endsWith(")")) {
            if (!(value instanceof String)) {
                throw new CodeGenerationException(
                        "Filter 'format' requires a string format pattern in '" + originalExpression + "'");
            }
            String argsText = filter.substring("format(".length(), filter.length() - 1);
            List<String> argParts = splitTopLevel(argsText, ',');
            Object[] args = new Object[argParts.size()];
            for (int i = 0; i < argParts.size(); i++) {
                Object arg = evaluate(argParts.get(i).trim(), scope);
                // Python's "%" formatting accepts ints for %f-style
                // conversions; Java's String.format does not, so numeric
                // arguments are normalized to double to match.
                args[i] = (arg instanceof Number) ? ((Number) arg).doubleValue() : arg;
            }
            try {
                return String.format((String) value, args);
            } catch (java.util.IllegalFormatException e) {
                throw new CodeGenerationException(
                        "Invalid format pattern '" + value + "' in '" + originalExpression + "'");
            }
        }
        throw new CodeGenerationException(
                "Unsupported Jinja filter '" + filter + "' in '" + originalExpression + "'");
    }

    /** Matches Jinja's default MarkupSafe escaping rules for the "e"/"escape" filter. */
    private String escapeHtml(String value) {
        StringBuilder result = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            switch (c) {
                case '&': result.append("&amp;"); break;
                case '<': result.append("&lt;"); break;
                case '>': result.append("&gt;"); break;
                case '"': result.append("&#34;"); break;
                case '\'': result.append("&#39;"); break;
                default: result.append(c);
            }
        }
        return result.toString();
    }

    private boolean valuesEqual(Object left, Object right) {
        if (left instanceof Number && right instanceof Number) {
            return ((Number) left).doubleValue() == ((Number) right).doubleValue();
        }
        return Objects.equals(left, right);
    }

    private boolean isTruthy(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (Boolean) value;
        if (value instanceof Number) return ((Number) value).doubleValue() != 0;
        if (value instanceof String) return !((String) value).isEmpty();
        if (value instanceof List) return !((List<?>) value).isEmpty();
        if (value instanceof Map) return !((Map<?, ?>) value).isEmpty();
        return true;
    }
}
