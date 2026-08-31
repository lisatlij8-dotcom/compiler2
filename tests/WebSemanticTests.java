import AST_H_C.HtmlElement;
import AST_H_C.HtmlTag;
import AST_H_C.Node;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.HtmlVisitor;
import visitor.WebSemanticVisitor;

import java.util.List;

public class WebSemanticTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testUniqueIds();
        testDuplicateId();
        testValidCssIdSelector();
        testMissingCssIdSelector();
        testValidCssClassSelector();
        testJinjaLoopLocalVariable();
        testJinjaSetVariable();
        testSetOrdering();
        testLoopScopeEnding();
        testJinjaFiltersAndProperties();
        testIncludeExtendsMetadata();
        testLinkFormMetadata();
        testMultipleDiagnostics();
        testMatchedSimpleTagPairNoMismatch();
        testMismatchedSimpleTagPairReportsError();
        testMatchedNestedTagsNoMismatch();
        testMismatchedNestedClosingReportsError();
        testVoidElementNoMismatch();
        testMetaAndLinkNoMismatch();
        testSelfClosingSlashTagNoMismatch();
        testVoidElementFollowedBySiblingsNoMismatch();
        testMultipleMismatchesAreCollected();
        testReportedBugVoidElementDoesNotNestSiblingAsChild();
        testMultipleMetaTagsThenTitleAllSiblings();
        testVoidAndNormalElementsAreSiblingsInBody();
        testLinkFollowedByEmbeddedStyleIsValid();
        testLinkFollowedBySingleLineEmbeddedStyleIsValid();
        testProfessorStyleSingleLineCssInHead();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static Node parseAst(String html) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString(html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        var tree = parser.htmlDocument();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing: " + html);
        }
        return new HtmlVisitor().visitHtmlDocument(tree);
    }

    private static HtmlTag findChildTag(HtmlTag parent, String tagName) {
        for (HtmlElement child : parent.getChildren()) {
            if (child instanceof HtmlTag && ((HtmlTag) child).getTagName().equals(tagName)) {
                return (HtmlTag) child;
            }
        }
        return null;
    }

    private static Result analyze(String html) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString("<!DOCTYPE html>" + html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        Node ast = new HtmlVisitor().visitHtmlDocument(parser.htmlDocument());
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing: " + html);
        }

        SemanticErrorReporter reporter = new SemanticErrorReporter();
        WebSemanticVisitor visitor = new WebSemanticVisitor(reporter, "template.html");
        visitor.analyze(ast);
        return new Result(reporter, visitor);
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    private static boolean hasType(Result result, SemanticError.Type type) {
        for (SemanticError error : result.reporter.getErrors()) {
            if (error.getType() == type) return true;
        }
        return false;
    }

    private static boolean hasExternal(Result result, String name) {
        for (WebSemanticVisitor.VariableUse use : result.visitor.getExternalVariableUses()) {
            if (use.getName().equals(name)) return true;
        }
        return false;
    }

    private static void testUniqueIds() {
        Result result = analyze("<div id=\"a\"></div><div id=\"b\"></div>");
        check("unique ids do not report duplicate id", !hasType(result, SemanticError.Type.DUPLICATE_HTML_ID));
    }

    private static void testDuplicateId() {
        Result result = analyze("<div id=\"main\"></div><section id=\"main\"></section>");
        check("duplicate id reports duplicate html id", hasType(result, SemanticError.Type.DUPLICATE_HTML_ID));
    }

    private static void testValidCssIdSelector() {
        Result result = analyze("<div id=\"main\"></div><style>#main{width:10px;}</style>");
        check("valid css id selector has no missing target", !hasType(result, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testMissingCssIdSelector() {
        Result result = analyze("<style>#missing{width:10px;}</style>");
        check("missing css id selector reports missing target", hasType(result, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testValidCssClassSelector() {
        Result result = analyze("<div class=\"card featured\"></div><style>.card{width:10px;}</style>");
        check("valid css class selector has no missing target", !hasType(result, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testJinjaLoopLocalVariable() {
        Result result = analyze("{% for item in products %}{{ item.name }}{% endfor %}");
        check("loop iterable is external", hasExternal(result, "products"));
        check("loop item is local in body", !hasExternal(result, "item"));
    }

    private static void testJinjaSetVariable() {
        Result result = analyze("{% set featured = product %}{{ featured.name }}");
        check("set value is external", hasExternal(result, "product"));
        check("set target is local after set", result.visitor.getLocalDefinitions().contains("featured")
                && !hasExternal(result, "featured"));
    }

    private static void testSetOrdering() {
        Result result = analyze("{{ featured }}{% set featured = product %}");
        check("use before set is external requirement", hasExternal(result, "featured"));
    }

    private static void testLoopScopeEnding() {
        Result result = analyze("{% for item in products %}{{ item.name }}{% endfor %}{{ item.name }}");
        check("loop variable after endfor reports out of scope",
                hasType(result, SemanticError.Type.UNDEFINED_JINJA_VARIABLE));
    }

    private static void testJinjaFiltersAndProperties() {
        Result result = analyze("{{ products|length }}{{ product.name }}");
        check("filters and properties collect only root variables",
                hasExternal(result, "products")
                        && hasExternal(result, "product")
                        && !hasExternal(result, "length")
                        && !hasExternal(result, "name"));
    }

    private static void testIncludeExtendsMetadata() {
        Result result = analyze("{% extends \"base.html\" %}{% include \"card.html\" %}");
        check("include and extends metadata are collected",
                result.visitor.getExtendsTargets().size() == 1
                        && result.visitor.getExtendsTargets().get(0).getTemplateName().equals("base.html")
                        && result.visitor.getIncludeTargets().size() == 1
                        && result.visitor.getIncludeTargets().get(0).getTemplateName().equals("card.html"));
    }

    private static void testLinkFormMetadata() {
        Result result = analyze("<a href=\"/products\">Products</a><form action=\"/add\" method=\"post\"><input name=\"title\"></form>");
        check("link form and field metadata are collected",
                result.visitor.getLinks().size() == 1
                        && result.visitor.getLinks().get(0).getHref().equals("/products")
                        && result.visitor.getForms().size() == 1
                        && result.visitor.getForms().get(0).getAction().equals("/add")
                        && result.visitor.getForms().get(0).getMethod().equals("POST")
                        && result.visitor.getFormFields().size() == 1
                        && result.visitor.getFormFields().get(0).getName().equals("title"));
    }

    private static void testMultipleDiagnostics() {
        Result result = analyze("<div id=\"main\"></div><div id=\"main\"></div><style>#missing{width:10px;}</style>");
        check("multiple diagnostics are preserved",
                hasType(result, SemanticError.Type.DUPLICATE_HTML_ID)
                        && hasType(result, SemanticError.Type.MISSING_CSS_TARGET)
                        && result.reporter.getErrors().size() == 2);
    }

    private static void testMatchedSimpleTagPairNoMismatch() {
        Result result = analyze("<div></div>");
        check("correctly matched simple tag pair has no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testMismatchedSimpleTagPairReportsError() {
        Result result = analyze("<div></span>");
        check("mismatched simple tag pair reports MISMATCHED_HTML_TAG",
                hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testMatchedNestedTagsNoMismatch() {
        Result result = analyze("<div><span>Hello</span></div>");
        check("correctly matched nested tags have no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testMismatchedNestedClosingReportsError() {
        Result result = analyze("<div><span>Hello</div></span>");
        check("incorrectly crossed nested closing tags report MISMATCHED_HTML_TAG",
                hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testVoidElementNoMismatch() {
        Result result = analyze("<input type=\"text\">");
        check("void element without a closing tag has no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testMetaAndLinkNoMismatch() {
        Result result = analyze("<meta charset=\"UTF-8\"><link href=\"x.css\">");
        check("meta/link elements without closing tags have no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testSelfClosingSlashTagNoMismatch() {
        Result result = analyze("<br/><input type=\"text\" />");
        check("explicit self-closing '/>' tags have no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testVoidElementFollowedBySiblingsNoMismatch() {
        // Mirrors the real demo_flask/templates/*.html shape: consecutive void <meta> tags
        // followed by other siblings and a real parent closing tag. The
        // htmlTag grammar rule now gives void elements their own alternative
        // with no htmlElement* and no closing-tag production, so they can
        // never structurally swallow siblings as children in the first place.
        Result result = analyze(
                "<head><meta charset=\"UTF-8\"><meta name=\"viewport\"><title>Page</title></head><body></body>");
        check("void element followed by siblings inside a parent has no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testReportedBugVoidElementDoesNotNestSiblingAsChild() {
        // Exact reproduction of the reported instructor-test bug: a bare
        // <meta> (no closing tag, no "/>") followed by <title> inside <head>,
        // with <body> as a sibling of <head> under <html>.
        Node root = parseAst(
                "<html><head><meta charset=\"UTF-8\"><title>Professor Test</title></head>"
                        + "<body><div class=\"test-page\"><p>Hello</p></div></body></html>");
        HtmlTag documentRoot = (HtmlTag) root;
        HtmlTag html = (HtmlTag) documentRoot.getChildren().get(0);
        HtmlTag head = findChildTag(html, "head");
        HtmlTag body = findChildTag(html, "body");
        check("head and body are both direct children of html (body not nested inside head)",
                head != null && body != null);

        HtmlTag meta = findChildTag(head, "meta");
        HtmlTag title = findChildTag(head, "title");
        check("meta and title are both direct children of head (title not nested inside meta)",
                meta != null && title != null);
        check("meta has no children of its own", meta != null && meta.getChildren().isEmpty());

        SemanticErrorReporter reporter = new SemanticErrorReporter();
        new WebSemanticVisitor(reporter, "bug.html").analyze(root);
        check("the exact reported bug produces 0 semantic errors (no false MISMATCHED_HTML_TAG)",
                reporter.getErrorCount() == 0);
    }

    private static void testMultipleMetaTagsThenTitleAllSiblings() {
        Node root = parseAst(
                "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width\">"
                        + "<title>Hello</title></head>");
        HtmlTag head = (HtmlTag) ((HtmlTag) root).getChildren().get(0);

        int metaCount = 0;
        boolean titleFound = false;
        for (HtmlElement child : head.getChildren()) {
            if (child instanceof HtmlTag) {
                HtmlTag tag = (HtmlTag) child;
                if (tag.getTagName().equals("meta")) {
                    metaCount++;
                    check("each meta tag has no children of its own", tag.getChildren().isEmpty());
                } else if (tag.getTagName().equals("title")) {
                    titleFound = true;
                }
            }
        }
        check("both meta tags and the title tag are direct children of head",
                metaCount == 2 && titleFound);
    }

    private static void testVoidAndNormalElementsAreSiblingsInBody() {
        Node root = parseAst("<body><input type=\"text\"><div>Hello</div><br><p>World</p></body>");
        HtmlTag body = (HtmlTag) ((HtmlTag) root).getChildren().get(0);

        List<String> tagNames = new java.util.ArrayList<>();
        for (HtmlElement child : body.getChildren()) {
            if (child instanceof HtmlTag) tagNames.add(((HtmlTag) child).getTagName());
        }
        check("input, div, br, p all remain direct siblings of body",
                tagNames.equals(List.of("input", "div", "br", "p")));
    }

    private static void testLinkFollowedByEmbeddedStyleIsValid() {
        // Multi-line CSS body - this test targets void-element/<style>
        // sibling structure, not the embedded-CSS single-line lexer quirk.
        Result result = analyze(
                "<head><link href=\"x.css\"><style>\n.page {\n  margin: 0;\n}\n</style></head>");
        check("link followed by embedded style has no mismatch error and no nesting corruption",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testMultipleMismatchesAreCollected() {
        Result result = analyze("<div></span><section></p>");
        long count = 0;
        for (SemanticError error : result.reporter.getErrors()) {
            if (error.getType() == SemanticError.Type.MISMATCHED_HTML_TAG) count++;
        }
        check("multiple independent tag mismatches are all collected", count == 2);
    }

    private static void testLinkFollowedBySingleLineEmbeddedStyleIsValid() {
        // Same shape as testLinkFollowedByEmbeddedStyleIsValid, but with the
        // single-line CSS body that previously desynced the embedded-CSS
        // lexer (VALUE greedily matching the leading space after '{').
        Result result = analyze(
                "<head><link href=\"x.css\"><style>.page { margin: 0; }</style></head>");
        check("link followed by single-line embedded style has no mismatch error",
                !hasType(result, SemanticError.Type.MISMATCHED_HTML_TAG));
    }

    private static void testProfessorStyleSingleLineCssInHead() {
        Node root = parseAst(
                "<!DOCTYPE html><html><head><meta charset=\"UTF-8\">"
                        + "<style>.test-page { margin: 0; padding: 10px; }</style>"
                        + "<title>Professor Test</title></head>"
                        + "<body><div class=\"test-page\">Hello</div></body></html>");
        HtmlTag html = (HtmlTag) ((HtmlTag) root).getChildren().get(0);
        HtmlTag head = findChildTag(html, "head");
        HtmlTag body = findChildTag(html, "body");
        check("head and body are direct children of html", head != null && body != null);

        HtmlTag meta = findChildTag(head, "meta");
        HtmlTag title = findChildTag(head, "title");
        check("meta, style and title all remain proper siblings under head",
                meta != null && title != null && meta.getChildren().isEmpty());

        AST_H_C.CSS_Style style = null;
        for (HtmlElement child : head.getChildren()) {
            if (child instanceof AST_H_C.CSS_Style) style = (AST_H_C.CSS_Style) child;
        }
        check("CSS_Style AST is present under head", style != null);

        AST_H_C.CSSRuleSet testPageRule = null;
        if (style != null) {
            for (AST_H_C.CSSRuleSet ruleSet : style.getRuleSets()) {
                if (ruleSet.getSelector().trim().equals(".test-page")) testPageRule = ruleSet;
            }
        }
        check("CSS_Style AST contains the .test-page selector", testPageRule != null);

        boolean hasMargin = false;
        boolean hasPadding = false;
        if (testPageRule != null) {
            for (AST_H_C.CssProperty property : testPageRule.getProperties()) {
                if (property.getProperty().equals("margin") && property.getValue().equals("0")) hasMargin = true;
                if (property.getProperty().equals("padding") && property.getValue().equals("10px")) hasPadding = true;
            }
        }
        check("margin and padding properties are preserved correctly", hasMargin && hasPadding);

        SemanticErrorReporter reporter = new SemanticErrorReporter();
        new WebSemanticVisitor(reporter, "professor.html").analyze(root);
        check("the professor-style single-line CSS scenario has 0 semantic errors",
                reporter.getErrorCount() == 0);
    }

    private static class Result {
        private final SemanticErrorReporter reporter;
        private final WebSemanticVisitor visitor;

        private Result(SemanticErrorReporter reporter, WebSemanticVisitor visitor) {
            this.reporter = reporter;
            this.visitor = visitor;
        }
    }
}
