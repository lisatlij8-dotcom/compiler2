import AST.AstNode;
import AST_H_C.Node;
import Semantic.ProjectSemanticContext;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import grammers.flaskLexer;
import grammers.flaskParser;
import grammers.htmlLexer;
import grammers.htmlParser;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import visitor.HtmlVisitor;
import visitor.PythonSemanticVisitor;
import visitor.PythonVisitor;
import visitor.WebSemanticVisitor;

import java.util.ArrayList;
import java.util.List;

public class DemoCompilerTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        demoValidProgram();
        demoSyntaxError();
        demoSemanticError();

        System.out.println();
        System.out.println("DemoCompilerTests: " + passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static void demoValidProgram() {
        String python = """
                from flask import Flask, render_template

                app = Flask("demo")
                products = []

                @app.route("/")
                def home():
                    return render_template("index.html", products=products)
                """;
        String template = """
                <html>
                <body>
                {% for product in products %}
                    {{ product.name }}
                {% endfor %}
                </body>
                </html>
                """;

        DemoResult result = analyze(python, template);
        boolean ok = result.pythonSyntaxErrors == 0
                && result.webSyntaxErrors == 0
                && result.semanticReporter.getErrorCount() == 0;

        printHeader("DEMO 1 - VALID PROGRAM");
        System.out.println("Python source:");
        System.out.println(python);
        System.out.println("Template source:");
        System.out.println(template);
        System.out.println("Python syntax errors: " + result.pythonSyntaxErrors);
        System.out.println("Web syntax errors: " + result.webSyntaxErrors);
        System.out.println("Semantic errors: " + result.semanticReporter.getErrorCount());
        System.out.println("Expected: SUCCESS");
        System.out.println("Actual: " + (ok ? "SUCCESS" : "FAILED"));
        check(ok);
    }

    private static void demoSyntaxError() {
        String python = """
                from flask import Flask, render_template

                app = Flask("demo")
                products = []

                @app.route("/")
                def home():
                    return render_template("index.html", products=products)
                """;
        String invalidTemplate = """
                <html>
                <body>
                {% for product in products %}
                    {{ product.name }}
                </body>
                </html>
                """;

        DemoResult result = analyze(python, invalidTemplate);
        boolean syntaxError = result.pythonSyntaxErrors + result.webSyntaxErrors > 0;

        printHeader("DEMO 2 - SYNTAX ERROR");
        System.out.println("Invalid template source:");
        System.out.println(invalidTemplate);
        System.out.println("Python syntax errors: " + result.pythonSyntaxErrors);
        System.out.println("Web syntax errors: " + result.webSyntaxErrors);
        System.out.println("Syntax error:");
        printSyntaxErrors(result);
        System.out.println("Expected: SYNTAX ERROR");
        System.out.println("Actual: " + (syntaxError ? "SYNTAX ERROR" : "NO SYNTAX ERROR"));
        check(syntaxError);
    }

    private static void demoSemanticError() {
        String python = """
                from flask import Flask, render_template

                app = Flask("demo")
                products = []

                @app.route("/")
                def home():
                    return render_template("index.html", products=products)
                """;
        String template = """
                <html>
                <body>
                    {{ products }}
                    {{ customer }}
                </body>
                </html>
                """;

        DemoResult result = analyze(python, template);
        boolean hasContextMismatch = hasType(result.semanticReporter, SemanticError.Type.TEMPLATE_CONTEXT_MISMATCH);
        boolean semanticError = result.pythonSyntaxErrors == 0
                && result.webSyntaxErrors == 0
                && hasContextMismatch;

        printHeader("DEMO 3 - SEMANTIC ERROR");
        System.out.println("Python source:");
        System.out.println(python);
        System.out.println("Template source:");
        System.out.println(template);
        System.out.println("Python syntax errors: " + result.pythonSyntaxErrors);
        System.out.println("Web syntax errors: " + result.webSyntaxErrors);
        System.out.println();
        System.out.println("Semantic diagnostic:");
        result.semanticReporter.printAll();
        System.out.println("Expected: SEMANTIC ERROR");
        System.out.println("Actual: " + (semanticError ? "SEMANTIC ERROR" : "FAILED"));
        check(semanticError);
    }

    private static DemoResult analyze(String python, String template) {
        DemoResult result = new DemoResult();

        flaskLexer pythonLexer = new flaskLexer(CharStreams.fromString(python));
        CollectingErrorListener pythonErrors = new CollectingErrorListener("demo.py");
        pythonLexer.removeErrorListeners();
        pythonLexer.addErrorListener(pythonErrors);

        flaskParser pythonParser = new flaskParser(new CommonTokenStream(pythonLexer));
        pythonParser.removeErrorListeners();
        pythonParser.addErrorListener(pythonErrors);
        flaskParser.ProgramContext pythonTree = pythonParser.program();
        result.pythonSyntaxErrors = pythonParser.getNumberOfSyntaxErrors() + pythonErrors.lexerErrorCount;
        result.pythonSyntaxMessages.addAll(pythonErrors.messages);

        PythonSemanticVisitor pythonSemantic = null;
        if (result.pythonSyntaxErrors == 0) {
            AstNode pythonAst = new PythonVisitor().visit(pythonTree);
            SemanticErrorReporter pythonReporter = new SemanticErrorReporter();
            pythonSemantic = new PythonSemanticVisitor(pythonReporter, "demo.py");
            pythonSemantic.analyze(pythonAst);
            copyDiagnostics(pythonReporter, result.semanticReporter);
        }

        htmlLexer webLexer = new htmlLexer(CharStreams.fromString(template));
        CollectingErrorListener webErrors = new CollectingErrorListener("index.html");
        webLexer.removeErrorListeners();
        webLexer.addErrorListener(webErrors);

        htmlParser webParser = new htmlParser(new CommonTokenStream(webLexer));
        webParser.removeErrorListeners();
        webParser.addErrorListener(webErrors);
        htmlParser.HtmlDocumentContext webTree = webParser.htmlDocument();
        result.webSyntaxErrors = webParser.getNumberOfSyntaxErrors() + webErrors.lexerErrorCount;
        result.webSyntaxMessages.addAll(webErrors.messages);

        WebSemanticVisitor webSemantic = null;
        if (result.webSyntaxErrors == 0) {
            Node webAst = new HtmlVisitor().visitHtmlDocument(webTree);
            SemanticErrorReporter webReporter = new SemanticErrorReporter();
            webSemantic = new WebSemanticVisitor(webReporter, "index.html");
            webSemantic.analyze(webAst);
            copyDiagnostics(webReporter, result.semanticReporter);
        }

        if (pythonSemantic != null && webSemantic != null) {
            ProjectSemanticContext projectContext = new ProjectSemanticContext();
            projectContext.addPythonResults(pythonSemantic, "demo.py");
            projectContext.addTemplate("index.html", webSemantic, "index.html");
            SemanticErrorReporter projectReporter = new SemanticErrorReporter();
            projectContext.validate(projectReporter);
            copyDiagnostics(projectReporter, result.semanticReporter);
        }

        return result;
    }

    private static void printHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println(title);
        System.out.println("========================================");
        System.out.println();
    }

    private static void printSyntaxErrors(DemoResult result) {
        if (result.pythonSyntaxMessages.isEmpty() && result.webSyntaxMessages.isEmpty()) {
            System.out.println("No syntax errors.");
            return;
        }
        for (String message : result.pythonSyntaxMessages) {
            System.out.println(message);
        }
        for (String message : result.webSyntaxMessages) {
            System.out.println(message);
        }
    }

    private static boolean hasType(SemanticErrorReporter reporter, SemanticError.Type type) {
        for (SemanticError error : reporter.getErrors()) {
            if (error.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private static void copyDiagnostics(SemanticErrorReporter source, SemanticErrorReporter target) {
        for (SemanticError error : source.getErrors()) {
            target.report(error);
        }
    }

    private static void check(boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS");
        } else {
            failed++;
            System.out.println("FAIL");
        }
    }

    private static class DemoResult {
        private int pythonSyntaxErrors;
        private int webSyntaxErrors;
        private final List<String> pythonSyntaxMessages = new ArrayList<>();
        private final List<String> webSyntaxMessages = new ArrayList<>();
        private final SemanticErrorReporter semanticReporter = new SemanticErrorReporter();
    }

    private static class CollectingErrorListener extends BaseErrorListener {
        private final String sourceName;
        private final List<String> messages = new ArrayList<>();
        private int lexerErrorCount = 0;

        private CollectingErrorListener(String sourceName) {
            this.sourceName = sourceName;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                int charPositionInLine, String msg, RecognitionException e) {
            if (recognizer instanceof org.antlr.v4.runtime.Lexer) {
                lexerErrorCount++;
            }
            messages.add(sourceName + ":" + line + ":" + charPositionInLine + " - " + msg);
        }
    }
}
