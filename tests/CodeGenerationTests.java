import AST.AstNode;
import AST_H_C.Node;
import CodeGeneration.Generator;
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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * Focused checks for the Code Generation phase (CodeGeneration/): Python
 * literal-data extraction, render_template() context resolution, and
 * rendering the supported Jinja/HTML AST subset into final HTML.
 * Not wired into Main.java - run separately, does not affect production behavior.
 */
public class CodeGenerationTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) throws Exception {
        testExtractPrimitiveAssignment();
        testExtractListLiteral();
        testExtractDictLiteral();
        testExtractListOfProductDictionaries();
        testRenderSimpleJinjaExpression();
        testPropertyLookupProductName();
        testRenderForLoop();
        testRenderLengthCondition();
        testRenderIfElse();
        testHtmlAttributesPreserved();
        testVoidElementEmittedCorrectly();
        testRealDemoProductsRender();
        testNoUnresolvedJinjaSyntax();
        testGeneratorGatedBySemanticErrors();
        testUrlForSimpleRoutes();
        testUrlForRouteWithIntParameter();
        testUrlForStaticWithConcatenatedFilename();
        testUrlForUnknownEndpointThrows();
        testFormatFilterFormatsPrice();
        testEscapeFilterEscapesHtml();
        testUndefinedGuardVariableIsFalsyNotError();

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

    private static AstNode parsePython(String code) {
        flaskLexer lexer = new flaskLexer(CharStreams.fromString(code));
        flaskParser parser = new flaskParser(new CommonTokenStream(lexer));
        flaskParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("python syntax errors while parsing: " + code);
        }
        return new PythonVisitor().visit(tree);
    }

    private static Node parseHtml(String html) {
        htmlLexer lexer = new htmlLexer(CharStreams.fromString(html));
        htmlParser parser = new htmlParser(new CommonTokenStream(lexer));
        htmlParser.HtmlDocumentContext tree = parser.htmlDocument();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("html syntax errors while parsing: " + html);
        }
        return new HtmlVisitor().visitHtmlDocument(tree);
    }

    // Exercises Code Generation end-to-end against the one real application
    // in the project (demo_flask) rather than a synthetic fixture, since
    // this specifically verifies generation against real, non-trivial
    // Python + Jinja source - not just the supported-syntax subset.
    private static String renderRealIndexHtml() throws Exception {
        AstNode pythonRoot = parsePython(Files.readString(Paths.get("demo_flask/app.py")));
        Node templateRoot = parseHtml(Files.readString(Paths.get("demo_flask/templates/index.html")));

        SemanticErrorReporter reporter = new SemanticErrorReporter();
        PythonSemanticVisitor pythonSemanticVisitor = new PythonSemanticVisitor(reporter, "demo_flask/app.py");
        pythonSemanticVisitor.analyze(pythonRoot);

        Generator generator = new Generator();
        generator.configureRoutes(pythonSemanticVisitor.getRoutes());
        Map<String, Object> globals = generator.extractGlobals(pythonRoot);

        PythonSemanticVisitor.TemplateRenderInfo indexRender = null;
        for (PythonSemanticVisitor.TemplateRenderInfo render : pythonSemanticVisitor.getTemplateRenders()) {
            if (render.getTemplateName().equals("index.html")) {
                indexRender = render;
            }
        }
        if (indexRender == null) {
            throw new IllegalStateException("render_template(\"index.html\", ...) not found in demo_flask/app.py");
        }

        Map<String, Object> context = generator.resolveContext(indexRender, globals);
        return generator.render(templateRoot, context);
    }

    private static void testExtractPrimitiveAssignment() {
        AstNode root = parsePython("name = \"Apple\"\ncount = 5\n");
        Map<String, Object> globals = new Generator().extractGlobals(root);
        check("primitive string and number assignments are extracted",
                "Apple".equals(globals.get("name")) && Long.valueOf(5L).equals(globals.get("count")));
    }

    private static void testExtractListLiteral() {
        AstNode root = parsePython("items = [1, 2, 3]\n");
        Map<String, Object> globals = new Generator().extractGlobals(root);
        Object items = globals.get("items");
        check("list literal is extracted as a List",
                items instanceof List
                        && ((List<?>) items).size() == 3
                        && ((List<?>) items).get(0).equals(1L));
    }

    private static void testExtractDictLiteral() {
        AstNode root = parsePython("product = {\"id\": 1, \"name\": \"Apple\"}\n");
        Map<String, Object> globals = new Generator().extractGlobals(root);
        Object product = globals.get("product");
        check("dict literal is extracted as a Map",
                product instanceof Map
                        && Long.valueOf(1L).equals(((Map<?, ?>) product).get("id"))
                        && "Apple".equals(((Map<?, ?>) product).get("name")));
    }

    private static void testExtractListOfProductDictionaries() {
        AstNode root = parsePython(
                "products = [\n"
                        + "    {\"id\": 1, \"name\": \"Apple\", \"price\": 15.99},\n"
                        + "    {\"id\": 2, \"name\": \"Milk\", \"price\": 12.50},\n"
                        + "]\n");
        Map<String, Object> globals = new Generator().extractGlobals(root);
        Object products = globals.get("products");
        check("list of product dictionaries is extracted with the right size",
                products instanceof List && ((List<?>) products).size() == 2);

        @SuppressWarnings("unchecked")
        Map<String, Object> first = (Map<String, Object>) ((List<?>) products).get(0);
        check("first product dictionary has correct fields",
                "Apple".equals(first.get("name")) && Double.valueOf(15.99).equals(first.get("price")));
    }

    private static void testRenderSimpleJinjaExpression() {
        Node html = parseHtml("<p>{{ name }}</p>");
        String output = new Generator().render(html, Map.of("name", "Apple"));
        check("simple Jinja expression renders the resolved value", output.contains("Apple"));
    }

    private static void testPropertyLookupProductName() {
        Node html = parseHtml("<p>{{ product.name }}</p>");
        String output = new Generator().render(html, Map.of("product", Map.of("name", "Apple")));
        check("dotted property lookup 'product.name' resolves correctly", output.contains("<p>Apple</p>"));
    }

    private static void testRenderForLoop() {
        Node html = parseHtml("<ul>{% for item in items %}<li>{{ item.name }}</li>{% endfor %}</ul>");
        Map<String, Object> context = Map.of(
                "items", List.of(Map.of("name", "Apple"), Map.of("name", "Milk")));
        String output = new Generator().render(html, context);
        check("for-loop renders once per item, in order",
                output.indexOf("Apple") < output.indexOf("Milk")
                        && output.contains("<li>Apple</li>")
                        && output.contains("<li>Milk</li>"));
    }

    private static void testRenderLengthCondition() {
        Node html = parseHtml("{% if items|length == 0 %}<p>Empty</p>{% endif %}");
        String emptyOutput = new Generator().render(html, Map.of("items", List.of()));
        String fullOutput = new Generator().render(html, Map.of("items", List.of("x")));
        check("length filter condition renders the true branch only when the list is empty",
                emptyOutput.contains("Empty") && !fullOutput.contains("Empty"));
    }

    private static void testRenderIfElse() {
        Node html = parseHtml(
                "{% if items|length == 0 %}<p>Empty</p>{% else %}<p>Has items</p>{% endif %}");
        String emptyOutput = new Generator().render(html, Map.of("items", List.of()));
        String fullOutput = new Generator().render(html, Map.of("items", List.of("x")));
        check("if/else renders the correct branch in both directions",
                emptyOutput.contains("Empty") && !emptyOutput.contains("Has items")
                        && fullOutput.contains("Has items") && !fullOutput.contains("Empty"));
    }

    private static void testHtmlAttributesPreserved() {
        Node html = parseHtml("<div id=\"main\" class=\"card\">Hello</div>");
        String output = new Generator().render(html, Map.of());
        check("HTML attributes are preserved in generated output",
                output.contains("id=\"main\"") && output.contains("class=\"card\""));
    }

    private static void testVoidElementEmittedCorrectly() {
        Node html = parseHtml("<meta charset=\"UTF-8\"><input type=\"text\"><br><img src=\"x.jpg\">");
        String output = new Generator().render(html, Map.of());
        check("void elements are emitted without invalid closing tags",
                output.contains("<meta charset=\"UTF-8\">")
                        && output.contains("<input type=\"text\">")
                        && output.contains("<br>")
                        && output.contains("<img src=\"x.jpg\">")
                        && !output.contains("</meta>") && !output.contains("</input>")
                        && !output.contains("</br>") && !output.contains("</img>"));
    }

    private static void testRealDemoProductsRender() throws Exception {
        String html = renderRealIndexHtml();
        check("generated real index.html contains all 4 real demo_flask products",
                html.contains("sun glasses") && html.contains("belt")
                        && html.contains("miu miu handbag") && html.contains("heels"));
        check("generated real index.html contains the correct formatted prices",
                html.contains("15.99") && html.contains("40.00")
                        && html.contains("150.00") && html.contains("180.00"));
        check("generated real index.html resolves url_for(...) product/edit routes",
                html.contains("/product/1") && html.contains("/edit/1"));
    }

    private static void testNoUnresolvedJinjaSyntax() throws Exception {
        String html = renderRealIndexHtml();
        check("generated output has no unresolved '{{' or '{%' for the supported constructs",
                !html.contains("{{") && !html.contains("{%"));
    }

    private static void testUrlForSimpleRoutes() {
        Node html = parseHtml(
                "<a href=\"{{ url_for('home') }}\">Home</a>"
                        + "<a href=\"{{ url_for('add_product') }}\">Add</a>"
                        + "<form action=\"{{ url_for('delete_product') }}\"></form>");
        Generator generator = new Generator();
        generator.configureRoutes(List.of(
                new PythonSemanticVisitor.RouteInfo("/", "home", 1, List.of("GET")),
                new PythonSemanticVisitor.RouteInfo("/add", "add_product", 2, List.of("GET")),
                new PythonSemanticVisitor.RouteInfo("/delete", "delete_product", 3, List.of("POST"))));
        String output = generator.render(html, Map.of());
        check("url_for('home') resolves to '/'", output.contains("href=\"/\""));
        check("url_for('add_product') resolves to '/add'", output.contains("href=\"/add\""));
        check("url_for('delete_product') resolves to '/delete'", output.contains("action=\"/delete\""));
    }

    private static void testUrlForRouteWithIntParameter() {
        Node html = parseHtml(
                "<a href=\"{{ url_for('product_details', product_id=product.id) }}\">Details</a>");
        Generator generator = new Generator();
        generator.configureRoutes(List.of(
                new PythonSemanticVisitor.RouteInfo(
                        "/product/<int:product_id>", "product_details", 1, List.of("GET"))));
        String output = generator.render(html, Map.of("product", Map.of("id", 5L)));
        check("url_for() substitutes an <int:...> route parameter with the actual value",
                output.contains("href=\"/product/5\""));
    }

    private static void testUrlForStaticWithConcatenatedFilename() {
        Node html = parseHtml(
                "<img src=\"{{ url_for('static', filename='uploads/' + product.image) }}\">");
        Generator generator = new Generator();
        String output = generator.render(html, Map.of("product", Map.of("image", "cat.jpg")));
        check("url_for('static', filename=... + ...) concatenates and resolves under /static/",
                output.contains("src=\"/static/uploads/cat.jpg\""));
    }

    private static void testUrlForUnknownEndpointThrows() {
        Node html = parseHtml("<a href=\"{{ url_for('does_not_exist') }}\">X</a>");
        Generator generator = new Generator();
        boolean threw = false;
        try {
            generator.render(html, Map.of());
        } catch (CodeGeneration.CodeGenerationException e) {
            threw = true;
        }
        check("url_for() for an unknown endpoint raises rather than guessing", threw);
    }

    private static void testFormatFilterFormatsPrice() {
        Node html = parseHtml("<p>{{ \"%.2f\"|format(price) }}</p>");
        String output = new Generator().render(html, Map.of("price", 40.0));
        check("\"%.2f\"|format(price) formats a float to two decimal places",
                output.contains("<p>40.00</p>"));
    }

    private static void testEscapeFilterEscapesHtml() {
        Node html = parseHtml("<p>{{ name|e }}</p>");
        String output = new Generator().render(html, Map.of("name", "Tom & <Jerry>"));
        check("'|e' filter HTML-escapes special characters",
                output.contains("Tom &amp; &lt;Jerry&gt;") && !output.contains("<Jerry>"));
    }

    private static void testUndefinedGuardVariableIsFalsyNotError() {
        Node html = parseHtml("{% if error %}<p>{{ error }}</p>{% endif %}<p>ok</p>");
        String output = new Generator().render(html, Map.of());
        check("a variable absent from context is treated as falsy in a condition, not an error",
                output.contains("<p>ok</p>") && !output.contains("{{ error }}"));
    }

    private static void testGeneratorGatedBySemanticErrors() {
        // Mirrors Main.runCodeGeneration's exact gating condition - generation
        // is only attempted when "!finalReporter.hasErrors()". This verifies
        // that condition correctly evaluates to false the moment a semantic
        // error is present, which is what actually blocks generation in Main.
        SemanticErrorReporter reporter = new SemanticErrorReporter();
        reporter.error(SemanticError.Type.UNDEFINED_VARIABLE, "x is not defined", 1, "test.py");
        boolean canGenerate = !reporter.hasErrors();
        check("Main's generation gate is false when a semantic error is present", !canGenerate);
    }
}
