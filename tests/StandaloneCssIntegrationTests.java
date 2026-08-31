import AST_H_C.CSS_Style;
import AST_H_C.Node;
import Semantic.ProjectSemanticContext;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import grammers.cssLexer;
import grammers.cssParser;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.CssVisitor;
import visitor.HtmlVisitor;
import visitor.WebSemanticVisitor;
import visitor.WebSymbolTableVisitor;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Focused checks for standalone .css integration: parsing via the standalone
 * cssLexer/cssParser, AST construction via CssVisitor, symbol collection via
 * WebSymbolTableVisitor, and project-wide selector validation against HTML
 * ids/classes collected across templates via ProjectSemanticContext.
 * Not wired into Main.java - run separately, does not affect production behavior.
 */
public class StandaloneCssIntegrationTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) throws Exception {
        testValidStandaloneCssParses();
        testInvalidStandaloneCssReportsSyntaxError();
        testMultipleRulesBuildAst();
        testSelectorSymbolCollection();
        testStandaloneIdMatchesHtmlId();
        testStandaloneClassMatchesHtmlClass();
        testMissingStandaloneSelectorReportsWarning();
        testSelectorValidAcrossDifferentTemplate();
        testNoCssFilesDoesNotReportErrors();
        testMultipleCssFilesAreBothValidated();
        testDescendantAndCompoundSelectorsPreserveSpacing();
        testMultiTokenValuePreservedWithSpacing();
        testMediaRuleFlattenedIntoRuleSets();
        testRealCssFilesParseWithZeroErrors();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
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

    private static int parseCssSyntaxErrors(String css) {
        cssLexer lexer = new cssLexer(CharStreams.fromString(css));
        cssParser parser = new cssParser(new CommonTokenStream(lexer));
        parser.stylesheet();
        return parser.getNumberOfSyntaxErrors();
    }

    private static CSS_Style parseCssAst(String css) {
        cssLexer lexer = new cssLexer(CharStreams.fromString(css));
        cssParser parser = new cssParser(new CommonTokenStream(lexer));
        cssParser.StylesheetContext tree = parser.stylesheet();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing css: " + css);
        }
        return new CssVisitor().visitStylesheet(tree);
    }

    private static WebSemanticVisitor analyzeHtml(String html, String sourceFile, SemanticErrorReporter reporter) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString(html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        htmlParser.HtmlDocumentContext tree = parser.htmlDocument();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing html: " + html);
        }
        Node ast = new HtmlVisitor().visitHtmlDocument(tree);
        WebSemanticVisitor visitor = new WebSemanticVisitor(reporter, sourceFile);
        visitor.analyze(ast);
        return visitor;
    }

    private static boolean hasType(SemanticErrorReporter reporter, SemanticError.Type type) {
        for (SemanticError error : reporter.getErrors()) {
            if (error.getType() == type) return true;
        }
        return false;
    }

    private static long countType(SemanticErrorReporter reporter, SemanticError.Type type) {
        long count = 0;
        for (SemanticError error : reporter.getErrors()) {
            if (error.getType() == type) count++;
        }
        return count;
    }

    private static void testValidStandaloneCssParses() {
        check("valid standalone CSS parses with 0 syntax errors",
                parseCssSyntaxErrors("#main {\n    width: 100%;\n}\n") == 0);
    }

    private static void testInvalidStandaloneCssReportsSyntaxError() {
        check("invalid standalone CSS reports a syntax error",
                parseCssSyntaxErrors(".card {\n    color red;\n}\n") > 0);
    }

    private static void testMultipleRulesBuildAst() {
        CSS_Style ast = parseCssAst("#main {\n    width: 100%;\n}\n\n.card {\n    padding: 10px;\n}\n");
        check("multiple standalone rules build two CSSRuleSet nodes with preserved selectors",
                ast.getRuleSets().size() == 2
                        && ast.getRuleSets().get(0).getSelector().equals("#main")
                        && ast.getRuleSets().get(1).getSelector().equals(".card"));
    }

    private static void testSelectorSymbolCollection() {
        WebSymbolTableVisitor.clearReport();
        CSS_Style ast = parseCssAst(".product-item {\n    color: red;\n}\n");
        new WebSymbolTableVisitor("styles.css").build(ast);

        boolean found = false;
        for (var symbol : WebSymbolTableVisitor.getSymbols()) {
            if (symbol.getType().equals("CSS_SELECTOR") && symbol.getName().equals(".product-item")) {
                found = true;
            }
        }
        check("standalone CSS selector is collected as a CSS_SELECTOR symbol", found);
    }

    private static void testStandaloneIdMatchesHtmlId() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor visitor = analyzeHtml(
                "<div id=\"main\" class=\"card\">Hello</div>", "index.html", htmlReporter);
        context.addTemplate("index.html", visitor, "index.html");

        CSS_Style css = parseCssAst("#main {\n    width: 100%;\n}\n");
        context.addStandaloneCss("styles.css", css.getRuleSets());

        context.validate(projectReporter);
        check("standalone #id matching an HTML id has no MISSING_CSS_TARGET",
                !hasType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testStandaloneClassMatchesHtmlClass() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor visitor = analyzeHtml(
                "<div id=\"main\" class=\"card\">Hello</div>", "index.html", htmlReporter);
        context.addTemplate("index.html", visitor, "index.html");

        CSS_Style css = parseCssAst(".card {\n    padding: 10px;\n}\n");
        context.addStandaloneCss("styles.css", css.getRuleSets());

        context.validate(projectReporter);
        check("standalone .class matching an HTML class has no MISSING_CSS_TARGET",
                !hasType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testMissingStandaloneSelectorReportsWarning() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor visitor = analyzeHtml(
                "<div class=\"card\"></div>", "index.html", htmlReporter);
        context.addTemplate("index.html", visitor, "index.html");

        CSS_Style css = parseCssAst(".card {\n    color: red;\n}\n\n.missing {\n    color: blue;\n}\n");
        context.addStandaloneCss("styles.css", css.getRuleSets());

        context.validate(projectReporter);
        check(".card is valid and .missing produces exactly one MISSING_CSS_TARGET warning",
                countType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET) == 1);
    }

    private static void testSelectorValidAcrossDifferentTemplate() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor indexVisitor = analyzeHtml(
                "<div class=\"product-card\">Item</div>", "index.html", htmlReporter);
        context.addTemplate("index.html", indexVisitor, "index.html");

        WebSemanticVisitor addVisitor = analyzeHtml(
                "<form action=\"/add\"></form>", "add_product.html", htmlReporter);
        context.addTemplate("add_product.html", addVisitor, "add_product.html");

        CSS_Style css = parseCssAst(".product-card {\n    padding: 10px;\n}\n");
        context.addStandaloneCss("styles.css", css.getRuleSets());

        context.validate(projectReporter);
        check("selector present in only one of several templates is valid project-wide",
                !hasType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testNoCssFilesDoesNotReportErrors() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor visitor = analyzeHtml("<div id=\"main\"></div>", "index.html", htmlReporter);
        context.addTemplate("index.html", visitor, "index.html");

        context.validate(projectReporter);
        check("no standalone CSS files means no MISSING_CSS_TARGET is introduced",
                !hasType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET));
    }

    private static void testMultipleCssFilesAreBothValidated() {
        SemanticErrorReporter htmlReporter = new SemanticErrorReporter();
        SemanticErrorReporter projectReporter = new SemanticErrorReporter();
        ProjectSemanticContext context = new ProjectSemanticContext();

        WebSemanticVisitor visitor = analyzeHtml(
                "<div id=\"main\" class=\"card\"></div>", "index.html", htmlReporter);
        context.addTemplate("index.html", visitor, "index.html");

        CSS_Style base = parseCssAst("#main {\n    width: 100%;\n}\n");
        CSS_Style products = parseCssAst(".card {\n    padding: 10px;\n}\n\n.missing {\n    color: red;\n}\n");
        context.addStandaloneCss("base.css", base.getRuleSets());
        context.addStandaloneCss("products.css", products.getRuleSets());

        context.validate(projectReporter);
        check("multiple standalone CSS files are both parsed and their selectors validated",
                countType(projectReporter, SemanticError.Type.MISSING_CSS_TARGET) == 1);
    }

    private static void testDescendantAndCompoundSelectorsPreserveSpacing() {
        // The grammar does not structurally distinguish a compound run from
        // a descendant combination (both are just simpleSelector+), but
        // CssVisitor reconstructs the exact original source text via a
        // character-stream interval, so the two remain distinguishable by
        // their preserved spacing.
        CSS_Style ast = parseCssAst(".a {\n    color: red;\n}\n.a b {\n    color: blue;\n}\n.a.b {\n    color: green;\n}\n");
        check("descendant selector '.a b' keeps its space",
                ast.getRuleSets().get(1).getSelector().equals(".a b"));
        check("compound selector '.a.b' keeps no space, distinct from the descendant form",
                ast.getRuleSets().get(2).getSelector().equals(".a.b"));
    }

    private static void testMultiTokenValuePreservedWithSpacing() {
        CSS_Style ast = parseCssAst(".a {\n    border: 1px solid #e6ded2;\n}\n");
        check("multi-token value 'border: 1px solid #e6ded2' is preserved with its original spacing",
                ast.getRuleSets().get(0).getProperties().get(0).getValue().equals("1px solid #e6ded2"));
    }

    private static void testMediaRuleFlattenedIntoRuleSets() {
        CSS_Style ast = parseCssAst("@media (max-width: 700px) {\n    .a {\n        color: red;\n    }\n}\n");
        check("rules nested inside @media are flattened into the same CSSRuleSet list",
                ast.getRuleSets().size() == 1 && ast.getRuleSets().get(0).getSelector().equals(".a"));
    }

    private static void testRealCssFilesParseWithZeroErrors() throws Exception {
        String[] files = {
                "demo_flask/static/css/index.css",
                "demo_flask/static/css/add_product.css",
                "demo_flask/static/css/product_details.css",
                "demo_flask/static/css/edit_product.css"
        };
        boolean allZero = true;
        for (String path : files) {
            String css = Files.readString(Paths.get(path));
            if (parseCssSyntaxErrors(css) != 0) {
                allZero = false;
            }
        }
        check("all four real demo_flask CSS files parse with 0 syntax errors", allZero);
    }
}
