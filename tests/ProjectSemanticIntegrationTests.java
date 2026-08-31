import AST.AstNode;
import AST_H_C.Node;
import Semantic.ProjectSemanticContext;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import grammers.flaskLexer;
import grammers.flaskParser;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.HtmlVisitor;
import visitor.PythonSemanticVisitor;
import visitor.PythonVisitor;
import visitor.WebSemanticVisitor;

public class ProjectSemanticIntegrationTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testExistingTemplate();
        testMissingTemplate();
        testCorrectJinjaContext();
        testMissingContext();
        testMultipleRenderers();
        testJinjaLocalVariable();
        testMissingInclude();
        testExistingInclude();
        testMissingExtends();
        testValidHrefRoute();
        testInvalidHrefRoute();
        testValidFormMethod();
        testWrongFormMethod();
        testExternalUrl();
        testSimpleDynamicRoute();
        testMultipleProjectErrors();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static PythonSemanticVisitor analyzePython(String code) {
        flaskLexer lexer = new flaskLexer(CharStreams.fromString(code));
        flaskParser parser = new flaskParser(new CommonTokenStream(lexer));
        flaskParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("python syntax errors while parsing: " + code);
        }

        AstNode ast = new PythonVisitor().visit(tree);
        PythonSemanticVisitor visitor = new PythonSemanticVisitor(new SemanticErrorReporter(), "app.py");
        visitor.analyze(ast);
        return visitor;
    }

    private static WebSemanticVisitor analyzeTemplate(String fileName, String html) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString("<!DOCTYPE html>" + html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        Node ast = new HtmlVisitor().visitHtmlDocument(parser.htmlDocument());
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("html syntax errors while parsing: " + html);
        }

        WebSemanticVisitor visitor = new WebSemanticVisitor(new SemanticErrorReporter(), fileName);
        visitor.analyze(ast);
        return visitor;
    }

    private static Result validate(String python, Template... templates) {
        ProjectSemanticContext context = new ProjectSemanticContext();
        context.addPythonResults(analyzePython(python), "app.py");
        for (Template template : templates) {
            context.addTemplate(template.name, analyzeTemplate(template.name, template.html), template.name);
        }
        SemanticErrorReporter reporter = new SemanticErrorReporter();
        context.validate(reporter);
        return new Result(context, reporter);
    }

    private static Template template(String name, String html) {
        return new Template(name, html);
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

    private static long countType(Result result, SemanticError.Type type) {
        long count = 0;
        for (SemanticError error : result.reporter.getErrors()) {
            if (error.getType() == type) count++;
        }
        return count;
    }

    private static boolean hasType(Result result, SemanticError.Type type) {
        return countType(result, type) > 0;
    }

    private static String renderFunction(String templateCall) {
        return "from flask import render_template\n"
                + "products = []\n"
                + "title = \"Title\"\n"
                + "def home():\n"
                + "    return " + templateCall + "\n";
    }

    private static void testExistingTemplate() {
        Result result = validate(
                renderFunction("render_template(\"index.html\")"),
                template("index.html", "<p>ok</p>"));
        check("existing template has no template-not-found", !hasType(result, SemanticError.Type.TEMPLATE_NOT_FOUND));
    }

    private static void testMissingTemplate() {
        Result result = validate(renderFunction("render_template(\"missing.html\")"));
        check("missing rendered template reports template not found",
                hasType(result, SemanticError.Type.TEMPLATE_NOT_FOUND));
    }

    private static void testCorrectJinjaContext() {
        Result result = validate(
                renderFunction("render_template(\"index.html\", products=products)"),
                template("index.html", "{{ products }}"));
        check("correct jinja context has no mismatch",
                !hasType(result, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH));
    }

    private static void testMissingContext() {
        Result result = validate(
                renderFunction("render_template(\"index.html\", products=products)"),
                template("index.html", "{{ products }}{{ title }}"));
        check("missing jinja context reports mismatch",
                hasType(result, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH));
    }

    private static void testMultipleRenderers() {
        String python = "from flask import render_template\n"
                + "products = []\n"
                + "def ok():\n"
                + "    return render_template(\"index.html\", products=products)\n"
                + "def bad():\n"
                + "    return render_template(\"index.html\")\n";
        Result result = validate(python, template("index.html", "{{ products }}"));
        check("same template is validated per render call",
                countType(result, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH) == 1);
    }

    private static void testJinjaLocalVariable() {
        Result result = validate(
                renderFunction("render_template(\"index.html\", products=products)"),
                template("index.html", "{% for item in products %}{{ item.name }}{% endfor %}"));
        check("loop variable is not required from Flask",
                !hasMismatchSubject(result, "item")
                        && !hasType(result, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH));
    }

    private static void testMissingInclude() {
        Result result = validate(
                renderFunction("render_template(\"index.html\")"),
                template("index.html", "{% include \"card.html\" %}"));
        check("missing include reports include not found", hasType(result, SemanticError.Type.INCLUDE_NOT_FOUND));
    }

    private static void testExistingInclude() {
        Result result = validate(
                renderFunction("render_template(\"index.html\")"),
                template("index.html", "{% include \"card.html\" %}"),
                template("card.html", "<p>card</p>"));
        check("existing include is valid", !hasType(result, SemanticError.Type.INCLUDE_NOT_FOUND));
    }

    private static void testMissingExtends() {
        Result result = validate(
                renderFunction("render_template(\"index.html\")"),
                template("index.html", "{% extends \"base.html\" %}"));
        check("missing extends reports extends not found", hasType(result, SemanticError.Type.EXTENDS_NOT_FOUND));
    }

    private static void testValidHrefRoute() {
        Result result = validate(
                "app = 1\n@app.route(\"/add\")\ndef add():\n    return 1\n",
                template("index.html", "<a href=\"/add\">Add</a>"));
        check("known href route is valid", !hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testInvalidHrefRoute() {
        Result result = validate(
                "app = 1\n@app.route(\"/add\")\ndef add():\n    return 1\n",
                template("index.html", "<a href=\"/missing\">Missing</a>"));
        check("unknown href route reports unknown route", hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testValidFormMethod() {
        Result result = validate(
                "app = 1\n@app.route(\"/add\", methods=[\"POST\"])\ndef add():\n    return 1\n",
                template("index.html", "<form action=\"/add\" method=\"post\"></form>"));
        check("known form route and method are valid", !hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testWrongFormMethod() {
        Result result = validate(
                "app = 1\n@app.route(\"/add\")\ndef add():\n    return 1\n",
                template("index.html", "<form action=\"/add\" method=\"post\"></form>"));
        check("wrong form method reports unknown route", hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testExternalUrl() {
        Result result = validate(
                "app = 1\n@app.route(\"/add\")\ndef add():\n    return 1\n",
                template("index.html", "<a href=\"https://example.com\">External</a>"));
        check("external url is skipped", !hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testSimpleDynamicRoute() {
        Result result = validate(
                "app = 1\n@app.route(\"/product/<int:product_id>\")\ndef detail(product_id):\n    return 1\n",
                template("index.html", "<a href=\"/product/12\">Details</a>"));
        check("simple dynamic route matches concrete local path",
                !hasType(result, SemanticError.Type.UNKNOWN_ROUTE));
    }

    private static void testMultipleProjectErrors() {
        Result result = validate(
                "from flask import render_template\n"
                        + "def missing():\n"
                        + "    return render_template(\"missing.html\")\n"
                        + "def incomplete():\n"
                        + "    return render_template(\"index.html\")\n",
                template("index.html", "{{ title }}{% include \"card.html\" %}<a href=\"/missing\">Missing</a>"));
        check("multiple project errors are retained",
                hasType(result, SemanticError.Type.TEMPLATE_NOT_FOUND)
                        && hasType(result, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH)
                        && hasType(result, SemanticError.Type.INCLUDE_NOT_FOUND)
                        && hasType(result, SemanticError.Type.UNKNOWN_ROUTE)
                        && result.reporter.getErrorCount() >= 4);
    }

    private static boolean hasMismatchSubject(Result result, String subject) {
        for (SemanticError error : result.reporter.getErrors()) {
            if (error.getType() == SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH
                    && error.getMessage().contains("'" + subject + "'")) {
                return true;
            }
        }
        return false;
    }

    private static class Template {
        private final String name;
        private final String html;

        private Template(String name, String html) {
            this.name = name;
            this.html = html;
        }
    }

    private static class Result {
        private final ProjectSemanticContext context;
        private final SemanticErrorReporter reporter;

        private Result(ProjectSemanticContext context, SemanticErrorReporter reporter) {
            this.context = context;
            this.reporter = reporter;
        }
    }
}
