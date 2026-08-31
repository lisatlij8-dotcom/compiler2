package Semantic;

import AST_H_C.CSSRuleSet;
import visitor.PythonSemanticVisitor;
import visitor.WebSemanticVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ProjectSemanticContext {

    private static final Set<String> TEMPLATE_GLOBALS = Set.of("url_for");

    private final List<RouteRecord> routes = new ArrayList<>();
    private final List<RenderRecord> renders = new ArrayList<>();
    private final Map<String, TemplateRecord> templates = new HashMap<>();
    private final List<StandaloneCssRecord> standaloneCssFiles = new ArrayList<>();
    private final Set<String> emittedDiagnostics = new HashSet<>();

    public void addPythonResults(PythonSemanticVisitor visitor, String sourceFile) {
        for (PythonSemanticVisitor.RouteInfo route : visitor.getRoutes()) {
            routes.add(new RouteRecord(
                    route.getPath(),
                    route.getFunctionName(),
                    route.getLine(),
                    sourceFile,
                    route.getMethods()
            ));
        }
        for (PythonSemanticVisitor.TemplateRenderInfo render : visitor.getTemplateRenders()) {
            renders.add(new RenderRecord(
                    render.getTemplateName(),
                    render.getFunctionName(),
                    render.getLine(),
                    sourceFile,
                    render.getContextValues().keySet()
            ));
        }
    }

    public void addTemplate(String templateName, WebSemanticVisitor visitor, String sourceFile) {
        templates.put(templateName, new TemplateRecord(templateName, sourceFile, visitor));
    }

    public void addStandaloneCss(String sourceFile, List<CSSRuleSet> ruleSets) {
        standaloneCssFiles.add(new StandaloneCssRecord(sourceFile, ruleSets));
    }

    public List<RouteRecord> getRoutes() {
        return routes;
    }

    public List<RenderRecord> getRenders() {
        return renders;
    }

    public Map<String, TemplateRecord> getTemplates() {
        return templates;
    }

    public void validate(SemanticErrorReporter reporter) {
        emittedDiagnostics.clear();
        validateRenderedTemplates(reporter);
        validateTemplateReferences(reporter);
        validateRoutes(reporter);
        validateStandaloneCss(reporter);
    }

    private void validateRenderedTemplates(SemanticErrorReporter reporter) {
        for (RenderRecord render : renders) {
            TemplateRecord template = templates.get(render.getTemplateName());
            if (template == null) {
                reportOnce(
                        reporter,
                        SemanticError.Type.TEMPLATE_NOT_FOUND,
                        "Template '" + render.getTemplateName() + "' was not registered",
                        render.getLine(),
                        render.getSourceFile(),
                        render.getTemplateName()
                );
                continue;
            }

            Set<String> provided = render.getContextKeys();
            for (String required : template.getRequiredVariableNames()) {
                if (TEMPLATE_GLOBALS.contains(required)) {
                    continue;
                }
                if (!provided.contains(required)) {
                    reportOnce(
                            reporter,
                            SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH,
                            "Template '" + render.getTemplateName() + "' requires variable '" + required + "'",
                            render.getLine(),
                            render.getSourceFile(),
                            render.getTemplateName() + ":" + required
                    );
                }
            }
        }
    }

    private void validateTemplateReferences(SemanticErrorReporter reporter) {
        for (TemplateRecord template : templates.values()) {
            for (WebSemanticVisitor.TemplateTarget include : template.getIncludeTargets()) {
                if (!templates.containsKey(include.getTemplateName())) {
                    reportOnce(
                            reporter,
                            SemanticError.Type.INCLUDE_NOT_FOUND,
                            "Included template '" + include.getTemplateName() + "' was not registered",
                            include.getLine(),
                            include.getSourceFile(),
                            include.getTemplateName()
                    );
                }
            }
            for (WebSemanticVisitor.TemplateTarget parent : template.getExtendsTargets()) {
                if (!templates.containsKey(parent.getTemplateName())) {
                    reportOnce(
                            reporter,
                            SemanticError.Type.EXTENDS_NOT_FOUND,
                            "Extended template '" + parent.getTemplateName() + "' was not registered",
                            parent.getLine(),
                            parent.getSourceFile(),
                            parent.getTemplateName()
                    );
                }
            }
        }
    }

    private void validateRoutes(SemanticErrorReporter reporter) {
        for (TemplateRecord template : templates.values()) {
            for (WebSemanticVisitor.LinkInfo link : template.getLinks()) {
                validatePath(reporter, link.getHref(), "GET", link.getLine(), link.getSourceFile(), "link");
            }
            for (WebSemanticVisitor.FormInfo form : template.getForms()) {
                validatePath(reporter, form.getAction(), form.getMethod(), form.getLine(), form.getSourceFile(), "form");
            }
        }
    }

    private void validatePath(SemanticErrorReporter reporter, String path, String method,
                              int line, String sourceFile, String label) {
        if (shouldSkipRoute(path)) {
            return;
        }

        RouteRecord matchingPath = null;
        for (RouteRecord route : routes) {
            if (matchesRoute(route.getPath(), path)) {
                matchingPath = route;
                if (route.supportsMethod(method)) {
                    return;
                }
            }
        }

        String message = matchingPath == null
                ? "Unknown " + label + " route '" + path + "'"
                : "Route '" + path + "' does not support method " + method;
        reportOnce(
                reporter,
                SemanticError.Type.UNKNOWN_ROUTE,
                message,
                line,
                sourceFile,
                path + ":" + method
        );
    }

    private boolean shouldSkipRoute(String path) {
        if (path == null || path.isBlank()) return true;
        String trimmed = path.trim();
        return trimmed.startsWith("http://")
                || trimmed.startsWith("https://")
                || trimmed.startsWith("#")
                || trimmed.startsWith("javascript:")
                || trimmed.contains("{{")
                || trimmed.contains("}}")
                || trimmed.contains("url_for");
    }

    private boolean matchesRoute(String routePattern, String path) {
        if (routePattern.equals(path)) {
            return true;
        }
        if (!routePattern.contains("<")) {
            return false;
        }

        StringBuilder regex = new StringBuilder();
        int index = 0;
        while (index < routePattern.length()) {
            int start = routePattern.indexOf('<', index);
            if (start < 0) {
                regex.append(Pattern.quote(routePattern.substring(index)));
                break;
            }

            regex.append(Pattern.quote(routePattern.substring(index, start)));
            int end = routePattern.indexOf('>', start);
            if (end < 0) {
                return false;
            }

            String parameter = routePattern.substring(start + 1, end);
            regex.append(parameter.startsWith("int:") ? "\\d+" : "[^/]+");
            index = end + 1;
        }
        return path.matches(regex.toString());
    }

    private void validateStandaloneCss(SemanticErrorReporter reporter) {
        if (standaloneCssFiles.isEmpty()) {
            return;
        }

        Set<String> projectIds = new HashSet<>();
        Set<String> projectClasses = new HashSet<>();
        for (TemplateRecord template : templates.values()) {
            projectIds.addAll(template.getHtmlIds());
            projectClasses.addAll(template.getHtmlClasses());
        }

        for (StandaloneCssRecord css : standaloneCssFiles) {
            for (CSSRuleSet ruleSet : css.getRuleSets()) {
                String selector = ruleSet.getSelector() == null ? "" : ruleSet.getSelector().trim();
                String target = null;
                if (WebSemanticVisitor.isIdSelector(selector) && !projectIds.contains(selector.substring(1))) {
                    target = selector;
                } else if (WebSemanticVisitor.isClassSelector(selector) && !projectClasses.contains(selector.substring(1))) {
                    target = selector;
                }
                if (target != null) {
                    reportOnce(
                            reporter,
                            SemanticError.Type.MISSING_CSS_TARGET,
                            "CSS selector '" + target + "' has no matching HTML target",
                            ruleSet.getNumberOfLine(),
                            css.getSourceFile(),
                            target,
                            SemanticError.Severity.WARNING
                    );
                }
            }
        }
    }

    private void reportOnce(SemanticErrorReporter reporter, SemanticError.Type type, String message,
                            int line, String sourceFile, String subject) {
        reportOnce(reporter, type, message, line, sourceFile, subject, SemanticError.Severity.ERROR);
    }

    private void reportOnce(SemanticErrorReporter reporter, SemanticError.Type type, String message,
                            int line, String sourceFile, String subject, SemanticError.Severity severity) {
        String key = type + "|" + severity + "|" + sourceFile + "|" + line + "|" + subject;
        if (emittedDiagnostics.add(key)) {
            if (severity == SemanticError.Severity.WARNING) {
                reporter.warning(type, message, line, sourceFile);
            } else {
                reporter.error(type, message, line, sourceFile);
            }
        }
    }

    public static class RouteRecord {
        private final String path;
        private final String functionName;
        private final int line;
        private final String sourceFile;
        private final List<String> methods;

        public RouteRecord(String path, String functionName, int line, String sourceFile, List<String> methods) {
            this.path = path;
            this.functionName = functionName;
            this.line = line;
            this.sourceFile = sourceFile;
            this.methods = methods;
        }

        public String getPath() { return path; }
        public String getFunctionName() { return functionName; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
        public List<String> getMethods() { return methods; }

        private boolean supportsMethod(String method) {
            String normalized = method == null || method.isBlank() ? "GET" : method.toUpperCase();
            for (String routeMethod : methods) {
                if (routeMethod.equalsIgnoreCase(normalized)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class RenderRecord {
        private final String templateName;
        private final String functionName;
        private final int line;
        private final String sourceFile;
        private final Set<String> contextKeys;

        public RenderRecord(String templateName, String functionName, int line,
                            String sourceFile, Set<String> contextKeys) {
            this.templateName = templateName;
            this.functionName = functionName;
            this.line = line;
            this.sourceFile = sourceFile;
            this.contextKeys = new HashSet<>(contextKeys);
        }

        public String getTemplateName() { return templateName; }
        public String getFunctionName() { return functionName; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
        public Set<String> getContextKeys() { return contextKeys; }
    }

    public static class TemplateRecord {
        private final String templateName;
        private final String sourceFile;
        private final WebSemanticVisitor visitor;

        public TemplateRecord(String templateName, String sourceFile, WebSemanticVisitor visitor) {
            this.templateName = templateName;
            this.sourceFile = sourceFile;
            this.visitor = visitor;
        }

        public String getTemplateName() { return templateName; }
        public String getSourceFile() { return sourceFile; }
        public List<WebSemanticVisitor.VariableUse> getExternalVariableUses() {
            return visitor.getExternalVariableUses();
        }
        /**
         * Names of external variables that must actually be supplied to
         * render_template(...) for this template. A variable whose every
         * use is guarded by "{% if <name> %}" for that same bare name is
         * excluded - Jinja's default Undefined-is-falsy semantics make
         * omitting it runtime-safe, so it is not "required" even though the
         * template references it.
         */
        public Set<String> getRequiredVariableNames() {
            Set<String> required = new LinkedHashSet<>();
            for (WebSemanticVisitor.VariableUse use : visitor.getExternalVariableUses()) {
                if (!use.isGuarded()) {
                    required.add(use.getName());
                }
            }
            return required;
        }
        public List<WebSemanticVisitor.TemplateTarget> getIncludeTargets() {
            return visitor.getIncludeTargets();
        }
        public List<WebSemanticVisitor.TemplateTarget> getExtendsTargets() {
            return visitor.getExtendsTargets();
        }
        public List<WebSemanticVisitor.LinkInfo> getLinks() {
            return visitor.getLinks();
        }
        public List<WebSemanticVisitor.FormInfo> getForms() {
            return visitor.getForms();
        }
        public Set<String> getHtmlIds() {
            return visitor.getHtmlIds().keySet();
        }
        public Set<String> getHtmlClasses() {
            return visitor.getHtmlClasses();
        }
    }

    public static class StandaloneCssRecord {
        private final String sourceFile;
        private final List<CSSRuleSet> ruleSets;

        public StandaloneCssRecord(String sourceFile, List<CSSRuleSet> ruleSets) {
            this.sourceFile = sourceFile;
            this.ruleSets = ruleSets;
        }

        public String getSourceFile() { return sourceFile; }
        public List<CSSRuleSet> getRuleSets() { return ruleSets; }
    }
}
