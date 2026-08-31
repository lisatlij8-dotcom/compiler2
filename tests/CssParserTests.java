import grammers.cssLexer;
import grammers.cssParser;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class CssParserTests {
    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testOneRule();
        testMultipleRules();
        testOneLineDeclaration();
        testMultipleProperties();
        testExistingEmbeddedCssTemplates();
        testEmbeddedSingleLineWithSpaces();
        testEmbeddedSingleLineNoSpaces();
        testEmbeddedMultiplePropertiesOneLine();
        testEmbeddedMultilineStillWorks();
        testEmbeddedCssFollowedByNormalHtml();
        testEmbeddedMultipleRulesOneLine();
        testArbitraryHyphenatedProperty();
        testKeywordValue();
        testMultiTokenValue();
        testGroupedSelector();
        testCompoundSelector();
        testPseudoClass();
        testPseudoElement();
        testRgbaFunction();
        testLinearGradientFunction();
        testRepeatMinmaxFunctions();
        testMediaWithNestedRules();
        testRealDemoCssSnippet();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static int parseCss(String css) {
        cssLexer lexer = new cssLexer(CharStreams.fromString(css));
        cssParser parser = new cssParser(new CommonTokenStream(lexer));
        parser.stylesheet();
        return parser.getNumberOfSyntaxErrors();
    }

    private static int parseHtmlSource(String html) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString(html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        parser.htmlDocument();
        return parser.getNumberOfSyntaxErrors();
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

    private static void testOneRule() {
        check("standalone css parses one multiline rule",
                parseCss(".card {\n    width: 10px;\n}\n") == 0);
    }

    private static void testMultipleRules() {
        check("standalone css parses multiple rules",
                parseCss("#main {\n    width: 10px;\n}\n\n.card {\n    margin: 0;\n}\n") == 0);
    }

    private static void testOneLineDeclaration() {
        check("standalone css parses one-line declaration",
                parseCss(".card { width: 10px; }\n") == 0);
    }

    private static void testMultipleProperties() {
        check("standalone css parses multiple properties",
                parseCss(".card {\n    width: 10px;\n    margin: 0;\n    color: red;\n}\n") == 0);
    }

    private static void testExistingEmbeddedCssTemplates() {
        // Inline fixture reproducing the shape of a real multi-page template
        // with an embedded <style> block (multiple selectors, multiple
        // multi-line rules) - the real demo_flask templates no longer embed
        // CSS (it now lives in static/css/*.css, parsed by the standalone
        // grammar and covered separately by StandaloneCssIntegrationTests),
        // so this stays a self-contained fixture rather than depending on
        // any external file. Sticks to the plain numeric/percentage values
        // the embedded-CSS-in-HTML mode is proven to support elsewhere in
        // this file - hyphenated keywords, colors and pseudo-classes are
        // standalone-grammar territory, already covered by parseCss(...)
        // tests below (testArbitraryHyphenatedProperty, testPseudoClass...).
        String templateShapedHtml =
                "<html><head><style>\n"
                        + "  body {\n"
                        + "    margin: 0;\n"
                        + "  }\n"
                        + "  .card {\n"
                        + "    width: 100%;\n"
                        + "    padding: 10px;\n"
                        + "  }\n"
                        + "</style></head><body><div class=\"card\">Hello</div></body></html>";
        check("embedded css inside a full template-shaped document still parses",
                parseHtmlSource(templateShapedHtml) == 0);
    }

    private static void testEmbeddedSingleLineWithSpaces() {
        check("embedded single-line CSS with spaces parses with 0 syntax errors",
                parseHtmlSource("<head><style>.page { margin: 0; }</style></head>") == 0);
    }

    private static void testEmbeddedSingleLineNoSpaces() {
        check("embedded single-line CSS without spaces parses with 0 syntax errors",
                parseHtmlSource("<head><style>.page{margin:0;}</style></head>") == 0);
    }

    private static void testEmbeddedMultiplePropertiesOneLine() {
        check("embedded single-line CSS with multiple properties parses with 0 syntax errors",
                parseHtmlSource("<head><style>.card { width: 100%; padding: 10px; }</style></head>") == 0);
    }

    private static void testEmbeddedMultilineStillWorks() {
        check("embedded multiline CSS still parses with 0 syntax errors",
                parseHtmlSource("<head><style>\n.page {\n    margin: 0;\n}\n</style></head>") == 0);
    }

    private static void testEmbeddedCssFollowedByNormalHtml() {
        check("embedded single-line CSS followed by normal HTML parses with 0 syntax errors",
                parseHtmlSource(
                        "<head><style>.page { margin: 0; }</style></head>"
                                + "<body><div class=\"page\">Hello</div></body>") == 0);
    }

    private static void testEmbeddedMultipleRulesOneLine() {
        check("embedded single-line CSS with multiple rules parses with 0 syntax errors",
                parseHtmlSource("<style>.a { margin: 0; } .b { padding: 5px; }</style>") == 0);
    }

    private static void testArbitraryHyphenatedProperty() {
        check("arbitrary hyphenated property (outside any fixed whitelist) parses with 0 syntax errors",
                parseCss(".a {\n    grid-template-columns: 10px;\n}\n") == 0);
    }

    private static void testKeywordValue() {
        check("bare keyword value parses with 0 syntax errors",
                parseCss(".a {\n    display: flex;\n}\n") == 0);
    }

    private static void testMultiTokenValue() {
        check("multi-token value parses with 0 syntax errors",
                parseCss(".a {\n    border: 1px solid #e6ded2;\n}\n") == 0);
    }

    private static void testGroupedSelector() {
        check("grouped selector parses with 0 syntax errors",
                parseCss("h1, h2, h3 {\n    margin: 0;\n}\n") == 0);
    }

    private static void testCompoundSelector() {
        check("compound selector parses with 0 syntax errors",
                parseCss(".button.hero-add {\n    color: white;\n}\n") == 0);
    }

    private static void testPseudoClass() {
        check("pseudo-class selector parses with 0 syntax errors",
                parseCss("button:disabled {\n    cursor: not-allowed;\n}\n") == 0);
    }

    private static void testPseudoElement() {
        check("pseudo-element selector parses with 0 syntax errors",
                parseCss(".a::before {\n    content: \"\";\n}\n") == 0);
    }

    private static void testRgbaFunction() {
        check("rgba() function value parses with 0 syntax errors",
                parseCss(".a {\n    color: rgba(15, 23, 42, 0.62);\n}\n") == 0);
    }

    private static void testLinearGradientFunction() {
        check("linear-gradient() function value with nested rgba() parses with 0 syntax errors",
                parseCss(".a {\n    background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(0, 0, 0, 0));\n}\n") == 0);
    }

    private static void testRepeatMinmaxFunctions() {
        check("repeat()/minmax() function values parse with 0 syntax errors",
                parseCss(".a {\n    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));\n}\n") == 0);
    }

    private static void testMediaWithNestedRules() {
        check("@media block with nested rules parses with 0 syntax errors",
                parseCss("@media (max-width: 700px) {\n    main {\n        padding: 20px;\n    }\n}\n") == 0);
    }

    private static void testRealDemoCssSnippet() {
        check("a complete real demo CSS snippet combining multiple new features parses with 0 syntax errors",
                parseCss(
                        ".store-hero {\n"
                                + "  display: flex;\n"
                                + "  background:\n"
                                + "    linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(246, 240, 229, 0.72)),\n"
                                + "    #ffffff;\n"
                                + "}\n"
                                + ".store-hero::after {\n"
                                + "  content: \"\";\n"
                                + "}\n"
                                + "h1, h2, h3 {\n"
                                + "  margin: 0;\n"
                                + "}\n"
                                + "button.danger:disabled {\n"
                                + "  cursor: not-allowed;\n"
                                + "}\n"
                                + "@media (max-width: 700px) {\n"
                                + "  main {\n"
                                + "    padding: 20px 14px;\n"
                                + "  }\n"
                                + "}\n") == 0);
    }
}
