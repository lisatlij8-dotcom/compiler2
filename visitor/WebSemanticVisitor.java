package visitor;

import AST_H_C.*;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebSemanticVisitor {

    public static class VariableUse {
        private final String name;
        private final int line;
        private final String sourceFile;
        private final boolean guarded;

        public VariableUse(String name, int line, String sourceFile, boolean guarded) {
            this.name = name;
            this.line = line;
            this.sourceFile = sourceFile;
            this.guarded = guarded;
        }

        public String getName() { return name; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }

        /**
         * True if this specific use occurs inside a "{% if <name> %}" (or
         * matching elif) guard for this same bare variable name. Under
         * Jinja's default Undefined-is-falsy semantics, a variable whose
         * every use is guarded this way is safe to omit from render_template
         * context - the guarded branch simply doesn't render.
         */
        public boolean isGuarded() { return guarded; }
    }

    public static class TemplateTarget {
        private final String templateName;
        private final int line;
        private final String sourceFile;

        public TemplateTarget(String templateName, int line, String sourceFile) {
            this.templateName = templateName;
            this.line = line;
            this.sourceFile = sourceFile;
        }

        public String getTemplateName() { return templateName; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
    }

    public static class LinkInfo {
        private final String href;
        private final int line;
        private final String sourceFile;

        public LinkInfo(String href, int line, String sourceFile) {
            this.href = href;
            this.line = line;
            this.sourceFile = sourceFile;
        }

        public String getHref() { return href; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
    }

    public static class FormInfo {
        private final String action;
        private final String method;
        private final int line;
        private final String sourceFile;

        public FormInfo(String action, String method, int line, String sourceFile) {
            this.action = action;
            this.method = method;
            this.line = line;
            this.sourceFile = sourceFile;
        }

        public String getAction() { return action; }
        public String getMethod() { return method; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
    }

    public static class FormFieldInfo {
        private final String name;
        private final int line;
        private final String sourceFile;

        public FormFieldInfo(String name, int line, String sourceFile) {
            this.name = name;
            this.line = line;
            this.sourceFile = sourceFile;
        }

        public String getName() { return name; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
    }

    public static class CssSelectorInfo {
        private final String selector;
        private final int line;
        private final String sourceFile;

        public CssSelectorInfo(String selector, int line, String sourceFile) {
            this.selector = selector;
            this.line = line;
            this.sourceFile = sourceFile;
        }

        public String getSelector() { return selector; }
        public int getLine() { return line; }
        public String getSourceFile() { return sourceFile; }
    }

    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
    private static final Set<String> JINJA_KEYWORDS = Set.of(
            "and", "or", "not", "in", "is", "true", "false", "none",
            "True", "False", "None"
    );
    // Void elements never require a closing tag. The grammar's htmlElement* is
    // greedy and has no notion of "void", so when one of these appears without
    // an explicit "/>" it swallows following siblings as children and its
    // optional closing group can latch onto an unrelated real closing tag
    // further down the document. That capture is not a genuine author error,
    // so mismatch validation is skipped for these tag names.
    private static final Set<String> VOID_ELEMENTS = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr"
    );

    private final SemanticErrorReporter reporter;
    private final String sourceFile;
    private final Map<String, Integer> htmlIds = new HashMap<>();
    private final Set<String> htmlClasses = new HashSet<>();
    private final Set<String> localDefinitions = new LinkedHashSet<>();
    private final Set<String> expiredLocalNames = new HashSet<>();
    private final ArrayDeque<Set<String>> jinjaScopes = new ArrayDeque<>();
    // Names currently guarded by an enclosing "{% if <name> %}"/"{% elif <name> %}"
    // for that same bare name - see VariableUse.isGuarded().
    private final ArrayDeque<Set<String>> guardScopes = new ArrayDeque<>();
    private final List<VariableUse> externalVariableUses = new ArrayList<>();
    private final List<TemplateTarget> includeTargets = new ArrayList<>();
    private final List<TemplateTarget> extendsTargets = new ArrayList<>();
    private final List<LinkInfo> links = new ArrayList<>();
    private final List<FormInfo> forms = new ArrayList<>();
    private final List<FormFieldInfo> formFields = new ArrayList<>();
    private final List<CssSelectorInfo> cssSelectors = new ArrayList<>();

    public WebSemanticVisitor(SemanticErrorReporter reporter, String sourceFile) {
        this.reporter = reporter;
        this.sourceFile = sourceFile;
        jinjaScopes.push(new HashSet<>());
    }

    public void analyze(Node root) {
        visit(root);
        validateCssSelectors();
    }

    public List<VariableUse> getExternalVariableUses() { return externalVariableUses; }
    public Set<String> getLocalDefinitions() { return localDefinitions; }
    public List<TemplateTarget> getIncludeTargets() { return includeTargets; }
    public List<TemplateTarget> getExtendsTargets() { return extendsTargets; }
    public List<LinkInfo> getLinks() { return links; }
    public List<FormInfo> getForms() { return forms; }
    public List<FormFieldInfo> getFormFields() { return formFields; }
    public Map<String, Integer> getHtmlIds() { return htmlIds; }
    public Set<String> getHtmlClasses() { return htmlClasses; }
    public List<CssSelectorInfo> getCssSelectors() { return cssSelectors; }

    private void visit(Node node) {
        if (node == null) return;

        if (node instanceof HtmlTag) {
            visitHtmlTag((HtmlTag) node);
        } else if (node instanceof CSS_Style) {
            CSS_Style style = (CSS_Style) node;
            if (style.getRuleSets() != null) {
                for (CSSRuleSet ruleSet : style.getRuleSets()) visit(ruleSet);
            }
        } else if (node instanceof CSSRuleSet) {
            CSSRuleSet ruleSet = (CSSRuleSet) node;
            cssSelectors.add(new CssSelectorInfo(ruleSet.getSelector().trim(), ruleSet.getNumberOfLine(), sourceFile));
        } else if (node instanceof JinjaExpression) {
            analyzeExpression(((JinjaExpression) node).getExpression(), node.getNumberOfLine());
        } else if (node instanceof JinjaForBlock) {
            visitJinjaForBlock((JinjaForBlock) node);
        } else if (node instanceof JinjaIfBlock) {
            visitJinjaIfBlock((JinjaIfBlock) node);
        } else if (node instanceof JinjaSingleTag) {
            visitJinjaSingleTag((JinjaSingleTag) node);
        }
    }

    private void visitHtmlTag(HtmlTag tag) {
        String tagName = tag.getTagName();
        String closingTagName = tag.getClosingTagName();
        if (closingTagName != null
                && !closingTagName.equalsIgnoreCase(tagName)
                && !VOID_ELEMENTS.contains(tagName.toLowerCase())) {
            reporter.error(
                    SemanticError.Type.MISMATCHED_HTML_TAG,
                    "Opening tag <" + tagName + "> is closed with </" + closingTagName + ">",
                    tag.getNumberOfLine(),
                    sourceFile
            );
        }

        String formAction = null;
        String formMethod = "GET";

        if (tag.getAttributes() != null) {
            for (HtmlAttribute attr : tag.getAttributes()) {
                String name = attr.getAttributeName();
                String value = attr.getAttributeValue();

                if ("id".equalsIgnoreCase(name)) {
                    if (htmlIds.containsKey(value)) {
                        reporter.error(
                                SemanticError.Type.DUPLICATE_HTML_ID,
                                "Duplicate HTML id '" + value + "'",
                                attr.getNumberOfLine(),
                                sourceFile
                        );
                    } else {
                        htmlIds.put(value, attr.getNumberOfLine());
                    }
                } else if ("class".equalsIgnoreCase(name)) {
                    for (String cls : value.split("\\s+")) {
                        if (!cls.isEmpty()) htmlClasses.add(cls);
                    }
                } else if ("a".equalsIgnoreCase(tagName) && "href".equalsIgnoreCase(name)) {
                    links.add(new LinkInfo(value, attr.getNumberOfLine(), sourceFile));
                } else if ("form".equalsIgnoreCase(tagName) && "action".equalsIgnoreCase(name)) {
                    formAction = value;
                } else if ("form".equalsIgnoreCase(tagName) && "method".equalsIgnoreCase(name)) {
                    formMethod = value.toUpperCase();
                } else if (isFormFieldTag(tagName) && "name".equalsIgnoreCase(name)) {
                    formFields.add(new FormFieldInfo(value, attr.getNumberOfLine(), sourceFile));
                }
            }
        }

        if ("form".equalsIgnoreCase(tagName)) {
            forms.add(new FormInfo(formAction, formMethod, tag.getNumberOfLine(), sourceFile));
            if (!"GET".equals(formMethod) && !"POST".equals(formMethod)) {
                reporter.warning(
                        SemanticError.Type.UNKNOWN_ROUTE,
                        "Unsupported form method '" + formMethod + "'",
                        tag.getNumberOfLine(),
                        sourceFile
                );
            }
        }

        if (tag.getChildren() != null) {
            for (HtmlElement child : tag.getChildren()) visit(child);
        }
    }

    private void visitJinjaSingleTag(JinjaSingleTag tag) {
        if ("set".equalsIgnoreCase(tag.getTagType())) {
            analyzeExpression(tag.getValue(), tag.getNumberOfLine());
            defineLocal(tag.getTarget());
        } else if ("include".equalsIgnoreCase(tag.getTagType())) {
            includeTargets.add(new TemplateTarget(unquote(tag.getValue()), tag.getNumberOfLine(), sourceFile));
        } else if ("extends".equalsIgnoreCase(tag.getTagType())) {
            extendsTargets.add(new TemplateTarget(unquote(tag.getValue()), tag.getNumberOfLine(), sourceFile));
        }
    }

    private void visitJinjaForBlock(JinjaForBlock block) {
        analyzeExpression(block.getIterableExpression(), block.getNumberOfLine());
        Set<String> scope = new HashSet<>();
        jinjaScopes.push(scope);
        defineLocal(block.getLoopVariable());

        if (block.getBody() != null) {
            for (HtmlElement element : block.getBody()) visit(element);
        }

        jinjaScopes.pop();
        expiredLocalNames.addAll(scope);
    }

    private void visitJinjaIfBlock(JinjaIfBlock block) {
        // Branches of the same if/elif/else are mutually exclusive at
        // runtime - only one ever renders - so an id reused across two
        // different branches is never actually a duplicate in the rendered
        // DOM. Each branch is visited from the SAME pre-block id snapshot
        // (isolating branches from each other), while a genuine duplicate
        // WITHIN a single branch is still caught normally. After all
        // branches, the union of ids any branch could introduce is kept, so
        // a real duplicate against content before/after the whole block is
        // still detected.
        Map<String, Integer> idsBeforeBranches = new HashMap<>(htmlIds);
        Map<String, Integer> idsUnionAfterBranches = new HashMap<>(idsBeforeBranches);

        if (block.getIfBody() != null) {
            visitGuardedBranch(block.getCondition(), block.getNumberOfLine(), block.getIfBody(),
                    idsBeforeBranches, idsUnionAfterBranches);
        } else {
            analyzeExpression(block.getCondition(), block.getNumberOfLine());
        }
        if (block.getElifBranches() != null) {
            for (JinjaIfBlock.ElifBranch branch : block.getElifBranches()) {
                visitGuardedBranch(branch.getCondition(), block.getNumberOfLine(), branch.getBody(),
                        idsBeforeBranches, idsUnionAfterBranches);
            }
        }
        if (block.getElseBody() != null) {
            visitGuardedBranch(null, block.getNumberOfLine(), block.getElseBody(),
                    idsBeforeBranches, idsUnionAfterBranches);
        }

        htmlIds.clear();
        htmlIds.putAll(idsUnionAfterBranches);
    }

    private void visitGuardedBranch(String condition, int line, List<HtmlElement> body,
                                    Map<String, Integer> idsBeforeBranches,
                                    Map<String, Integer> idsUnionAfterBranches) {
        htmlIds.clear();
        htmlIds.putAll(idsBeforeBranches);

        String bareGuardName = bareIdentifierOrNull(condition);
        Set<String> guardScope = new HashSet<>();
        if (bareGuardName != null) {
            guardScope.add(bareGuardName);
        }
        guardScopes.push(guardScope);

        // Analyzed INSIDE the guard scope: checking "{% if X %}" only tests
        // X's truthiness, which is safe even when X is Undefined (Jinja's
        // default Undefined is falsy) - so the condition's own bare-name
        // reference must not itself count as an unguarded (required) use.
        analyzeExpression(condition, line);

        for (HtmlElement element : body) {
            visit(element);
        }

        guardScopes.pop();

        for (Map.Entry<String, Integer> entry : htmlIds.entrySet()) {
            idsUnionAfterBranches.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }

    private String bareIdentifierOrNull(String condition) {
        if (condition == null) {
            return null;
        }
        String trimmed = condition.trim();
        return trimmed.matches("[A-Za-z_][A-Za-z0-9_]*") ? trimmed : null;
    }

    private void analyzeExpression(String expression, int line) {
        if (expression == null || expression.isBlank()) return;

        for (String identifier : extractIdentifiers(expression)) {
            if (isLocal(identifier)) continue;

            if (expiredLocalNames.contains(identifier)) {
                reporter.error(
                        SemanticError.Type.UNDEFINED_JINJA_VARIABLE,
                        "Jinja variable '" + identifier + "' is out of scope",
                        line,
                        sourceFile
                );
            } else {
                externalVariableUses.add(new VariableUse(identifier, line, sourceFile, isGuarded(identifier)));
            }
        }
    }

    private boolean isGuarded(String name) {
        for (Set<String> scope : guardScopes) {
            if (scope.contains(name)) return true;
        }
        return false;
    }

    private Set<String> extractIdentifiers(String expression) {
        Set<String> identifiers = new LinkedHashSet<>();
        Matcher matcher = IDENTIFIER_PATTERN.matcher(expression);
        while (matcher.find()) {
            String name = matcher.group();
            int start = matcher.start();
            char previous = start > 0 ? expression.charAt(start - 1) : '\0';
            if (previous == '.' || previous == '|' || JINJA_KEYWORDS.contains(name) || isInsideString(expression, start)) {
                continue;
            }
            identifiers.add(name);
        }
        return identifiers;
    }

    private boolean isInsideString(String text, int position) {
        boolean single = false;
        boolean doub = false;
        for (int i = 0; i < position; i++) {
            char c = text.charAt(i);
            if (c == '"' && !single) doub = !doub;
            if (c == '\'' && !doub) single = !single;
        }
        return single || doub;
    }

    private void defineLocal(String name) {
        if (name != null && !name.isBlank()) {
            jinjaScopes.peek().add(name);
            localDefinitions.add(name);
        }
    }

    private boolean isLocal(String name) {
        for (Set<String> scope : jinjaScopes) {
            if (scope.contains(name)) return true;
        }
        return false;
    }

    private void validateCssSelectors() {
        for (CssSelectorInfo selector : cssSelectors) {
            String text = selector.getSelector();
            if (isIdSelector(text)) {
                String id = text.substring(1);
                if (!htmlIds.containsKey(id)) {
                    missingCssTarget(text, selector.getLine());
                }
            } else if (isClassSelector(text)) {
                String cls = text.substring(1);
                if (!htmlClasses.contains(cls)) {
                    missingCssTarget(text, selector.getLine());
                }
            }
        }
    }

    /**
     * Shared with ProjectSemanticContext's project-wide standalone CSS
     * validation, so both the per-template and standalone paths classify
     * simple id/class selectors identically.
     */
    public static boolean isIdSelector(String text) {
        return text != null && text.matches("#[A-Za-z_][A-Za-z0-9_-]*");
    }

    public static boolean isClassSelector(String text) {
        return text != null && text.matches("\\.[A-Za-z_][A-Za-z0-9_-]*");
    }

    private void missingCssTarget(String selector, int line) {
        reporter.warning(
                SemanticError.Type.MISSING_CSS_TARGET,
                "CSS selector '" + selector + "' has no matching HTML target",
                line,
                sourceFile
        );
    }

    private boolean isFormFieldTag(String tagName) {
        return "input".equalsIgnoreCase(tagName)
                || "select".equalsIgnoreCase(tagName)
                || "textarea".equalsIgnoreCase(tagName)
                || "button".equalsIgnoreCase(tagName);
    }

    private String unquote(String value) {
        if (value != null && value.length() >= 2) {
            char first = value.charAt(0);
            char last = value.charAt(value.length() - 1);
            if ((first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }
}
