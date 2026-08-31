package CodeGeneration;

import AST.AstNode;
import AST.Identifier;
import AST_H_C.*;
import visitor.PythonSemanticVisitor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The project's actual Code Generation phase.
 *
 * Input:  the Python AST's compile-time-constant data (via
 *         PythonDataExtractor) plus a resolved render_template() context
 *         (via resolveContext, using the existing
 *         PythonSemanticVisitor.TemplateRenderInfo metadata), and the
 *         matching template's Web/Jinja AST (AST_H_C.Node, built by
 *         HtmlVisitor).
 * Output: a final HTML string (render), which Main.java writes to
 *         generated_output/*.html.
 *
 * This is NOT the same thing as ANTLR generating grammers/*.g4 into
 * gen/grammers/*.java - that happens before the compiler runs and produces
 * the parser infrastructure the compiler itself is built from. This class
 * runs AFTER parsing, AST construction, and semantic validation, and
 * produces the project's own compiler output.
 */
public class Generator {

    private static final Set<String> VOID_ELEMENTS = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr"
    );
    private static final Pattern EXPRESSION_IN_TEXT = Pattern.compile("\\{\\{\\s*(.*?)\\s*\\}\\}");

    private final PythonDataExtractor dataExtractor = new PythonDataExtractor();
    private final JinjaExpressionEvaluator evaluator = new JinjaExpressionEvaluator();

    /** Extracts every compile-time-constant global variable from the Python AST. */
    public Map<String, Object> extractGlobals(AstNode pythonRoot) {
        return dataExtractor.extractGlobals(pythonRoot);
    }

    /**
     * Supplies the Flask route table (from @app.route decorators collected
     * by PythonSemanticVisitor) so url_for(...) calls in templates can be
     * resolved to their actual path, without ever running Flask/Jinja.
     */
    public void configureRoutes(List<PythonSemanticVisitor.RouteInfo> routes) {
        Map<String, String> byEndpoint = new LinkedHashMap<>();
        for (PythonSemanticVisitor.RouteInfo route : routes) {
            byEndpoint.putIfAbsent(route.getFunctionName(), route.getPath());
        }
        evaluator.configureRoutes(byEndpoint);
    }

    /**
     * Resolves a render_template(...) call's keyword-argument context into
     * actual runtime values, bridging the Python AST's data into the
     * template side. A keyword value that is itself a literal is evaluated
     * directly; a keyword value that is an Identifier (the common case,
     * e.g. "products=products") is looked up in the extracted globals.
     * A reference that isn't a known compile-time-constant global (e.g. a
     * for-loop variable such as "product=product" in product_details.html)
     * cannot be safely resolved and raises CodeGenerationException rather
     * than being guessed.
     */
    public Map<String, Object> resolveContext(PythonSemanticVisitor.TemplateRenderInfo render,
                                              Map<String, Object> globals) {
        Map<String, Object> context = new LinkedHashMap<>();
        for (Map.Entry<String, AstNode> entry : render.getContextValues().entrySet()) {
            String name = entry.getKey();
            AstNode valueNode = entry.getValue();
            Object resolved;
            if (valueNode instanceof Identifier) {
                String refName = ((Identifier) valueNode).getName();
                if (!globals.containsKey(refName)) {
                    throw new CodeGenerationException(
                            "Cannot resolve context variable '" + name + "' for template '"
                                    + render.getTemplateName() + "': '" + refName
                                    + "' is not a compile-time constant (likely a runtime/loop-bound value)");
                }
                resolved = globals.get(refName);
            } else {
                resolved = dataExtractor.evaluateLiteral(valueNode);
            }
            context.put(name, resolved);
        }
        return context;
    }

    /** Renders the given template AST into final HTML using the resolved context. */
    public String render(Node templateRoot, Map<String, Object> context) {
        StringBuilder out = new StringBuilder();
        Map<String, Object> scope = new LinkedHashMap<>(context);
        if (templateRoot instanceof HtmlTag) {
            List<HtmlElement> children = ((HtmlTag) templateRoot).getChildren();
            if (children != null) {
                for (HtmlElement child : children) {
                    renderElement(child, scope, out);
                }
            }
        }
        return out.toString();
    }

    private void renderElement(HtmlElement element, Map<String, Object> scope, StringBuilder out) {
        if (element instanceof HtmlTag) {
            renderTag((HtmlTag) element, scope, out);
        } else if (element instanceof HtmlText) {
            out.append(((HtmlText) element).getText());
        } else if (element instanceof JinjaExpression) {
            Object value = evaluator.evaluate(((JinjaExpression) element).getExpression(), scope);
            out.append(value == null ? "" : String.valueOf(value));
        } else if (element instanceof JinjaForBlock) {
            renderForBlock((JinjaForBlock) element, scope, out);
        } else if (element instanceof JinjaIfBlock) {
            renderIfBlock((JinjaIfBlock) element, scope, out);
        } else if (element instanceof JinjaSingleTag) {
            renderSingleTag((JinjaSingleTag) element, scope, out);
        } else if (element instanceof CSS_Style) {
            renderStyle((CSS_Style) element, out);
        }
        // Anything else is simply not emitted rather than guessed at.
    }

    private void renderTag(HtmlTag tag, Map<String, Object> scope, StringBuilder out) {
        String tagName = tag.getTagName();
        out.append('<').append(tagName);

        if (tag.getAttributes() != null) {
            for (HtmlAttribute attribute : tag.getAttributes()) {
                out.append(' ').append(attribute.getAttributeName());
                String rawValue = attribute.getAttributeValue();
                if (rawValue != null && !rawValue.isEmpty()) {
                    out.append("=\"");
                    renderAttributeValue(rawValue, scope, out);
                    out.append('"');
                }
            }
        }
        out.append('>');

        if (VOID_ELEMENTS.contains(tagName.toLowerCase())) {
            return;
        }

        if (tag.getChildren() != null) {
            for (HtmlElement child : tag.getChildren()) {
                renderElement(child, scope, out);
            }
        }
        out.append("</").append(tagName).append('>');
    }

    /**
     * Attribute values are captured by the grammar as one opaque string, so
     * a "{{ ... }}" written inside an attribute (e.g. href="/product/{{
     * product.id }}") never becomes its own JinjaExpression AST node - it
     * stays literal text inside the attribute value. This substitutes any
     * such embedded expressions using the current rendering scope so the
     * generated attribute contains the resolved value, not raw "{{ }}".
     */
    private void renderAttributeValue(String rawValue, Map<String, Object> scope, StringBuilder out) {
        Matcher matcher = EXPRESSION_IN_TEXT.matcher(rawValue);
        int lastEnd = 0;
        while (matcher.find()) {
            out.append(rawValue, lastEnd, matcher.start());
            Object value = evaluator.evaluate(matcher.group(1), scope);
            out.append(value == null ? "" : String.valueOf(value));
            lastEnd = matcher.end();
        }
        out.append(rawValue, lastEnd, rawValue.length());
    }

    private void renderForBlock(JinjaForBlock block, Map<String, Object> scope, StringBuilder out) {
        Object iterable = evaluator.evaluate(block.getIterableExpression(), scope);
        if (!(iterable instanceof List)) {
            throw new CodeGenerationException(
                    "'" + block.getIterableExpression() + "' is not a list; cannot generate this for-loop");
        }
        for (Object item : (List<?>) iterable) {
            Map<String, Object> loopScope = new LinkedHashMap<>(scope);
            loopScope.put(block.getLoopVariable(), item);
            renderBody(block.getBody(), loopScope, out);
        }
    }

    private void renderIfBlock(JinjaIfBlock block, Map<String, Object> scope, StringBuilder out) {
        if (evaluator.evaluateCondition(block.getCondition(), scope)) {
            renderBody(block.getIfBody(), scope, out);
            return;
        }
        if (block.getElifBranches() != null) {
            for (JinjaIfBlock.ElifBranch branch : block.getElifBranches()) {
                if (evaluator.evaluateCondition(branch.getCondition(), scope)) {
                    renderBody(branch.getBody(), scope, out);
                    return;
                }
            }
        }
        renderBody(block.getElseBody(), scope, out);
    }

    private void renderSingleTag(JinjaSingleTag tag, Map<String, Object> scope, StringBuilder out) {
        if ("set".equalsIgnoreCase(tag.getTagType())) {
            scope.put(tag.getTarget(), evaluator.evaluate(tag.getValue(), scope));
            return;
        }
        throw new CodeGenerationException(
                "Jinja '" + tag.getTagType() + "' is not supported by code generation "
                        + "(outside the minimal supported subset)");
    }

    private void renderStyle(CSS_Style style, StringBuilder out) {
        out.append("<style>");
        if (style.getRuleSets() != null) {
            for (CSSRuleSet ruleSet : style.getRuleSets()) {
                out.append(ruleSet.getSelector().trim()).append(" {");
                if (ruleSet.getProperties() != null) {
                    for (CssProperty property : ruleSet.getProperties()) {
                        out.append(property.getProperty()).append(": ").append(property.getValue()).append("; ");
                    }
                }
                out.append("} ");
            }
        }
        out.append("</style>");
    }

    private void renderBody(List<HtmlElement> body, Map<String, Object> scope, StringBuilder out) {
        if (body == null) return;
        for (HtmlElement element : body) {
            renderElement(element, scope, out);
        }
    }
}
