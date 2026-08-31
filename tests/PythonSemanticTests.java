import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import AST.AstNode;
import grammers.flaskLexer;
import grammers.flaskParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.PythonSemanticVisitor;
import visitor.PythonVisitor;

public class PythonSemanticTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testValidVariableUse();
        testUseBeforeDefinition();
        testUndefinedIdentifierInsideExpression();
        testFunctionScope();
        testUndefinedInsideFunction();
        testParentGlobalLookup();
        testDuplicateParameters();
        testReassignment();
        testDirectUndefinedFunctionCall();
        testAttributeCall();
        testMultipleErrors();
        testDuplicateFlaskRoute();
        testRenderTemplateExtraction();
        testUndefinedVariableInsideAndExpression();
        testUndefinedVariableInsideOrExpression();
        testUndefinedVariableInsideNotExpression();
        testBooleanExpressionWithAllDefinedVariablesHasNoErrors();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static Result analyze(String code) {
        flaskLexer lexer = new flaskLexer(CharStreams.fromString(code));
        flaskParser parser = new flaskParser(new CommonTokenStream(lexer));
        flaskParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing: " + code);
        }

        AstNode ast = new PythonVisitor().visit(tree);
        SemanticErrorReporter reporter = new SemanticErrorReporter();
        PythonSemanticVisitor visitor = new PythonSemanticVisitor(reporter, "semantic_test.py");
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
            if (error.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private static long countType(Result result, SemanticError.Type type) {
        long count = 0;
        for (SemanticError error : result.reporter.getErrors()) {
            if (error.getType() == type) {
                count++;
            }
        }
        return count;
    }

    private static void testValidVariableUse() {
        Result result = analyze("x = 5\nprint(x)\n");
        check("valid variable use has no semantic errors", result.reporter.getErrorCount() == 0);
    }

    private static void testUseBeforeDefinition() {
        Result result = analyze("print(x)\nx = 5\n");
        check("use before definition reports undefined variable",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testUndefinedIdentifierInsideExpression() {
        Result result = analyze("y = x + 1\n");
        check("undefined identifier inside expression reports undefined variable",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testFunctionScope() {
        Result result = analyze("def f(a):\n    b = a\n    return b\n");
        check("function parameter and local variable resolve", result.reporter.getErrorCount() == 0);
    }

    private static void testUndefinedInsideFunction() {
        Result result = analyze("def f(a):\n    return missing\n");
        check("undefined inside function reports undefined variable",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testParentGlobalLookup() {
        Result result = analyze("x = 5\ndef f():\n    return x\n");
        check("function can resolve parent global variable", result.reporter.getErrorCount() == 0);
    }

    private static void testDuplicateParameters() {
        Result result = analyze("def f(a, a):\n    return a\n");
        check("duplicate parameters report duplicate symbol",
                hasType(result, SemanticError.Type.DUPLICATE_SYMBOL));
    }

    private static void testReassignment() {
        Result result = analyze("x = 1\nx = 2\n");
        check("normal reassignment is not a duplicate symbol",
                !hasType(result, SemanticError.Type.DUPLICATE_SYMBOL));
    }

    private static void testDirectUndefinedFunctionCall() {
        Result result = analyze("missing_function()\n");
        check("direct unknown call reports undefined function",
                hasType(result, SemanticError.Type.UNDEFINED_FUNCTION));
    }

    private static void testAttributeCall() {
        Result result = analyze("obj = 1\nobj.method()\n");
        check("attribute call does not report method as undefined function",
                !hasType(result, SemanticError.Type.UNDEFINED_FUNCTION));
    }

    private static void testMultipleErrors() {
        Result result = analyze("print(x)\ny = z + 1\nmissing_function()\n");
        check("multiple semantic errors are collected",
                result.reporter.getErrorCount() >= 3
                        && hasType(result, SemanticError.Type.UNDEFINED_VARIABLE)
                        && hasType(result, SemanticError.Type.UNDEFINED_FUNCTION));
    }

    private static void testDuplicateFlaskRoute() {
        Result result = analyze(
                "app = 1\n" +
                "@app.route(\"/same\")\n" +
                "def one():\n" +
                "    return 1\n" +
                "@app.route(\"/same\")\n" +
                "def two():\n" +
                "    return 2\n");
        check("duplicate Flask route reports duplicate route",
                hasType(result, SemanticError.Type.DUPLICATE_ROUTE));
    }

    private static void testUndefinedVariableInsideAndExpression() {
        Result result = analyze("defined_var = 1\nif defined_var and missing_var:\n    x = 1\n");
        check("undefined variable inside 'and' expression is still detected",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testUndefinedVariableInsideOrExpression() {
        Result result = analyze("defined_var = 1\nif defined_var or missing_var:\n    x = 1\n");
        check("undefined variable inside 'or' expression is still detected",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testUndefinedVariableInsideNotExpression() {
        Result result = analyze("if not missing_var:\n    x = 1\n");
        check("undefined variable inside 'not' expression is still detected",
                hasType(result, SemanticError.Type.UNDEFINED_VARIABLE));
    }

    private static void testBooleanExpressionWithAllDefinedVariablesHasNoErrors() {
        Result result = analyze(
                "a = 1\nb = 2\nif a and b:\n    x = 1\nif a or b:\n    y = 1\nif not a:\n    z = 1\n");
        check("boolean expression with all variables defined has no semantic errors",
                result.reporter.getErrorCount() == 0);
    }

    private static void testRenderTemplateExtraction() {
        Result result = analyze(
                "from flask import render_template\n" +
                "products = []\n" +
                "def home():\n" +
                "    return render_template(\"index.html\", products=products)\n");
        check("render_template metadata is collected",
                result.visitor.getTemplateRenders().size() == 1
                        && result.visitor.getTemplateRenders().get(0).getTemplateName().equals("index.html")
                        && result.visitor.getTemplateRenders().get(0).getContextValues().containsKey("products"));
    }

    private static class Result {
        private final SemanticErrorReporter reporter;
        private final PythonSemanticVisitor visitor;

        private Result(SemanticErrorReporter reporter, PythonSemanticVisitor visitor) {
            this.reporter = reporter;
            this.visitor = visitor;
        }
    }
}
