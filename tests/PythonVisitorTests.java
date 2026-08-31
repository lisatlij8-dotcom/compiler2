import AST.*;
import grammers.flaskLexer;
import grammers.flaskParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import visitor.PythonVisitor;

import java.util.List;

/**
 * Focused, standalone checks for the PythonVisitor repairs made in Phase 0B.
 * Not wired into Main.java - run separately, does not affect production behavior.
 *
 * Each test parses a small snippet with the existing (unmodified) flaskLexer/flaskParser
 * and inspects the resulting AST node types/children via instanceof, not string matching.
 */
public class PythonVisitorTests {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {
        testArithmeticExpression();
        testComparison();
        testForLoop();
        testReturnMultiValue();
        testImportFrom();
        testListLiteral();
        testDictLiteral();
        testKeywordArgument();
        testDecorator();
        testBooleanAnd();
        testBooleanOr();
        testBooleanNot();
        testBooleanOrAndPrecedence();
        testBooleanNotAndPrecedence();
        testFlaskStyleBooleanCondition();

        System.out.println();
        System.out.println(passed + " passed, " + failed + " failed");
        if (failed > 0) System.exit(1);
    }

    private static AstNode parse(String code) {
        flaskLexer lexer = new flaskLexer(CharStreams.fromString(code));
        flaskParser parser = new flaskParser(new CommonTokenStream(lexer));
        flaskParser.ProgramContext tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() > 0) {
            throw new RuntimeException("syntax errors while parsing: " + code);
        }
        return new PythonVisitor().visit(tree);
    }

    private static AstNode firstStmt(String code) {
        return parse(code).getChildren().get(0);
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

    private static void testArithmeticExpression() {
        // len(products) + 1
        Assign assign = (Assign) firstStmt("x = len(products) + 1\n");
        AstNode right = assign.getChildren().get(1);
        check("arithmetic expression builds BinaryExpression(+) with both operands preserved",
                right instanceof BinaryExpression
                        && right.getChildren().size() == 2
                        && right.getChildren().get(0) instanceof FunctionCall
                        && right.getChildren().get(1) instanceof NumberLiteral);
    }

    private static void testComparison() {
        // product["id"] == product_id
        Assign assign = (Assign) firstStmt("x = product[\"id\"] == product_id\n");
        AstNode right = assign.getChildren().get(1);
        check("comparison expression builds BinaryExpression(==) with both operands preserved",
                right instanceof BinaryExpression
                        && right.getChildren().size() == 2
                        && right.getChildren().get(0) instanceof Subscript
                        && right.getChildren().get(1) instanceof Identifier);
    }

    private static void testForLoop() {
        AstNode stmt = firstStmt("for product in products:\n    x = 1\n");
        check("for statement builds a ForStatement node (iterator + iterable + body)",
                stmt instanceof ForStatement
                        && ((ForStatement) stmt).getIteratorId().equals("product")
                        && stmt.getChildren().size() == 3);
    }

    private static void testReturnMultiValue() {
        FunctionDef func = (FunctionDef) firstStmt("def f():\n    return \"Not Found\", 404\n");
        AstNode ret = func.getBody().get(0);
        AstNode value = ret.getChildren().get(0);
        check("multi-value return builds ReturnStatement wrapping a FLAT ListLiteral",
                ret instanceof ReturnStatement
                        && ret.getChildren().size() == 1
                        && value instanceof ListLiteral
                        && value.getChildren().size() == 2
                        && value.getChildren().get(0) instanceof StringLiteral
                        && value.getChildren().get(1) instanceof NumberLiteral);
    }

    private static void testImportFrom() {
        AstNode stmt = firstStmt("from flask import Flask, redirect\n");
        check("from-import builds an ImportStatement with one Identifier child per name",
                stmt instanceof ImportStatement
                        && stmt.getChildren().size() == 2
                        && ((Identifier) stmt.getChildren().get(0)).getName().equals("Flask")
                        && ((Identifier) stmt.getChildren().get(1)).getName().equals("redirect"));
    }

    private static void testListLiteral() {
        Assign assign = (Assign) firstStmt("x = [1, 2, 3]\n");
        AstNode right = assign.getChildren().get(1);
        check("list literal builds a FLAT ListLiteral (no nested ListLiteral wrapper)",
                right instanceof ListLiteral
                        && right.getChildren().size() == 3
                        && right.getChildren().get(0) instanceof NumberLiteral
                        && right.getChildren().get(1) instanceof NumberLiteral
                        && right.getChildren().get(2) instanceof NumberLiteral);
    }

    private static void testDictLiteral() {
        Assign assign = (Assign) firstStmt("x = {\"a\": 1, \"b\": 2}\n");
        AstNode right = assign.getChildren().get(1);
        check("dict literal builds DictLiteral with one Entry child per key/value pair",
                right instanceof DictLiteral && right.getChildren().size() == 2);
    }

    private static void testKeywordArgument() {
        FunctionCall call = (FunctionCall) firstStmt("f(a, b=2)\n");
        List<AstNode> children = call.getChildren();
        check("call arguments build separate positional + KeywordArgument nodes (no merge)",
                children.size() == 3
                        && children.get(1) instanceof Identifier
                        && children.get(2) instanceof KeywordArgument);
    }

    private static void testDecorator() {
        FunctionDef func = (FunctionDef) firstStmt(
                "@app.route(\"/add\", methods=[\"GET\", \"POST\"])\ndef add():\n    return 1\n");
        AstNode decorators = func.getChildren().get(func.getChildren().size() - 1);
        AstNode decorator = decorators.getChildren().get(0);
        check("decorator builds a Decorator node with its call arguments attached",
                decorator instanceof Decorator
                        && decorator.getChildren().size() == 2
                        && decorator.getChildren().get(1) instanceof KeywordArgument);
    }

    private static void testBooleanAnd() {
        Assign assign = (Assign) firstStmt("x = a and b\n");
        AstNode right = assign.getChildren().get(1);
        check("'a and b' builds BinaryExpression(and) with both operands preserved",
                right instanceof BinaryExpression
                        && ((BinaryExpression) right).getOperator().equals("and")
                        && ((BinaryExpression) right).getLeft() instanceof Identifier
                        && ((BinaryExpression) right).getRight() instanceof Identifier);
    }

    private static void testBooleanOr() {
        Assign assign = (Assign) firstStmt("x = a or b\n");
        AstNode right = assign.getChildren().get(1);
        check("'a or b' builds BinaryExpression(or) with both operands preserved",
                right instanceof BinaryExpression
                        && ((BinaryExpression) right).getOperator().equals("or")
                        && ((BinaryExpression) right).getLeft() instanceof Identifier
                        && ((BinaryExpression) right).getRight() instanceof Identifier);
    }

    private static void testBooleanNot() {
        Assign assign = (Assign) firstStmt("x = not a\n");
        AstNode right = assign.getChildren().get(1);
        check("'not a' builds UnaryExpression(not) wrapping the operand",
                right instanceof UnaryExpression
                        && ((UnaryExpression) right).getOperator().equals("not")
                        && ((UnaryExpression) right).getOperand() instanceof Identifier);
    }

    private static void testBooleanOrAndPrecedence() {
        // a or b and c  =>  OR(a, AND(b, c))  since 'and' binds tighter than 'or'
        Assign assign = (Assign) firstStmt("x = a or b and c\n");
        AstNode right = assign.getChildren().get(1);
        check("'a or b and c' respects and-over-or precedence: OR(a, AND(b, c))",
                right instanceof BinaryExpression
                        && ((BinaryExpression) right).getOperator().equals("or")
                        && ((BinaryExpression) right).getLeft() instanceof Identifier
                        && ((BinaryExpression) right).getRight() instanceof BinaryExpression
                        && ((BinaryExpression) ((BinaryExpression) right).getRight()).getOperator().equals("and"));
    }

    private static void testBooleanNotAndPrecedence() {
        // not a and b  =>  AND(NOT(a), b)  since 'not' binds tighter than 'and'
        Assign assign = (Assign) firstStmt("x = not a and b\n");
        AstNode right = assign.getChildren().get(1);
        check("'not a and b' respects not-over-and precedence: AND(NOT(a), b)",
                right instanceof BinaryExpression
                        && ((BinaryExpression) right).getOperator().equals("and")
                        && ((BinaryExpression) right).getLeft() instanceof UnaryExpression
                        && ((UnaryExpression) ((BinaryExpression) right).getLeft()).getOperator().equals("not")
                        && ((BinaryExpression) right).getRight() instanceof Identifier);
    }

    private static void testFlaskStyleBooleanCondition() {
        AstNode stmt = firstStmt(
                "if request.method == \"POST\" and request.form.get(\"name\"):\n    x = 1\n");
        check("realistic Flask 'and' condition parses and builds an IfStatement",
                stmt instanceof IfStatement
                        && ((IfStatement) stmt).getCondition() instanceof BinaryExpression
                        && ((BinaryExpression) ((IfStatement) stmt).getCondition()).getOperator().equals("and"));
    }
}
