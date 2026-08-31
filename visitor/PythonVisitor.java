package visitor;

import AST.*;
import grammers.flaskParser;
import grammers.flaskParserBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import java.util.*;

public class PythonVisitor extends flaskParserBaseVisitor<AstNode> {

    @Override
    public AstNode visitProgram(flaskParser.ProgramContext ctx) {
        List<AstNode> statements = new ArrayList<>();
        for (var stmt : ctx.statement()) {
            AstNode node = visit(stmt);
            if (node != null) statements.add(node);
        }
        return new Program(ctx.start.getLine(), statements);
    }




    @Override
    public AstNode visitImportStmtRule(flaskParser.ImportStmtRuleContext ctx) {
        return visit(ctx.importStmt());
    }

    @Override
    public AstNode visitImportStmt(flaskParser.ImportStmtContext ctx) {
        if (ctx.FROM() != null) {
            String module = ctx.dotted_name().getText();
            List<String> names = new ArrayList<>();
            for (var item : ctx.importList().importItem()) {
                names.add(item.ID(0).getText());
            }
            return new ImportStatement(module, names, ctx.start.getLine());
        }

        String module = ctx.dotted_name().getText();
        return new ImportStatement(module, Collections.emptyList(), ctx.start.getLine());
    }



    @Override
    public AstNode visitAssignStmtRule(flaskParser.AssignStmtRuleContext ctx) {
        return visit(ctx.assignStmt());
    }


    @Override
    public AstNode visitReturnStmtRule(flaskParser.ReturnStmtRuleContext ctx) {
        return visit(ctx.returnStmt());
    }

    @Override
    public AstNode visitReturnStmt(flaskParser.ReturnStmtContext ctx) {
        if (ctx.expression().isEmpty()) {
            return new ReturnStatement(null, ctx.start.getLine());
        }
        if (ctx.expression().size() > 1) {
            List<AstNode> items = new ArrayList<>();
            for (var e : ctx.expression()) items.add(visit(e));
            AstNode tupleLike = new ListLiteral(items, ctx.start.getLine());
            return new ReturnStatement(tupleLike, ctx.start.getLine());
        }
        return new ReturnStatement(visit(ctx.expression(0)), ctx.start.getLine());
    }


    @Override
    public AstNode visitExpressionStmtRule(flaskParser.ExpressionStmtRuleContext ctx) {
        return visit(ctx.exprStmt());
    }

    @Override
    public AstNode visitExprStmt(flaskParser.ExprStmtContext ctx) {
        return visit(ctx.expression());
    }


    public AstNode visitFunctionDefRule(flaskParser.FunctionDefRuleContext ctx) {
        return visit(ctx.functionDef());
    }


    public AstNode visitIfStmtRule(flaskParser.IfStmtRuleContext ctx) {
        return visit(ctx.ifStmt());
    }


    public AstNode visitForStmtRule(flaskParser.ForStmtRuleContext ctx) {
        return visit(ctx.forStmt());
    }

    @Override
    public AstNode visitForStmt(flaskParser.ForStmtContext ctx) {
        String variable = ctx.ID().getText();
        AstNode iterable = visit(ctx.expression());
        List<AstNode> body = extractBlock(ctx.block());
        return new ForStatement(variable, iterable, body, ctx.start.getLine());
    }


    @Override
    public AstNode visitIdentifierPrimary(flaskParser.IdentifierPrimaryContext ctx) {
        return new Identifier(ctx.ID().getText(), ctx.start.getLine());
    }

    @Override
    public AstNode visitNumberPrimary(flaskParser.NumberPrimaryContext ctx) {
        return new NumberLiteral(ctx.NUMBER().getText(), ctx.start.getLine());
    }

    @Override
    public AstNode visitStringPrimary(flaskParser.StringPrimaryContext ctx) {
        String raw = ctx.STRING().getText();
        return new StringLiteral(raw.substring(1, raw.length() - 1), ctx.start.getLine());
    }

    @Override
    public AstNode visitTruePrimary(flaskParser.TruePrimaryContext ctx) {
        return new BooleanLiteral(true, ctx.start.getLine());
    }

    @Override
    public AstNode visitFalsePrimary(flaskParser.FalsePrimaryContext ctx) {
        return new BooleanLiteral(false, ctx.start.getLine());
    }

    @Override
    public AstNode visitNonePrimary(flaskParser.NonePrimaryContext ctx) {
        return new NoneLiteral(ctx.start.getLine());
    }

    @Override
    public AstNode visitParenPrimary(flaskParser.ParenPrimaryContext ctx) {
        return (ctx.expression() != null) ? visit(ctx.expression()) : null;
    }

    @Override
    public AstNode visitListPrimary(flaskParser.ListPrimaryContext ctx) {
        return visit(ctx.listLiteral());
    }

    @Override
    public AstNode visitDictPrimary(flaskParser.DictPrimaryContext ctx) {
        return visit(ctx.dictLiteral());
    }

    @Override
    public AstNode visitListLiteral(flaskParser.ListLiteralContext ctx) {
        List<AstNode> elements = new ArrayList<>();
        for (var expr : ctx.expression()) {
            addFlattenedElements(expr, elements);
        }
        return new ListLiteral(elements, ctx.start.getLine());
    }

    /**
     * The grammar's `expression` rule has its own internal comma-chain
     * (comparison (COMMA comparison)*), which is ambiguous with a comma-separated
     * outer collection (list elements, positional call arguments): ANTLR's parser
     * greedily absorbs sibling elements into a single expression's comparison list
     * whenever nothing disambiguates the boundary (e.g. "[dict1, dict2]" parses as
     * ONE expression holding two comparisons, not two sibling expressions).
     * This flattens that back into separate AST elements instead of nesting them.
     */
    private void addFlattenedElements(flaskParser.ExpressionContext expr, List<AstNode> target) {
        if (expr.or_test().size() > 1) {
            for (var t : expr.or_test()) {
                target.add(visit(t));
            }
        } else {
            target.add(visit(expr));
        }
    }

    @Override
    public AstNode visitDictLiteral(flaskParser.DictLiteralContext ctx) {
        Map<AstNode, AstNode> entries = new LinkedHashMap<>();
        for (var entry : ctx.dictEntry()) {
            AstNode key = visit(entry.expression(0));
            AstNode value = visit(entry.expression(1));
            entries.put(key, value);
        }
        return new DictLiteral(entries, ctx.start.getLine());
    }

    @Override
    public AstNode visitAssignStmt(flaskParser.AssignStmtContext ctx) {
        AstNode left = visit(ctx.expression(0));
        AstNode right = visit(ctx.expression(1));
        return new Assign(left, right, ctx.start.getLine());
    }

    @Override
    public AstNode visitFunctionDef(flaskParser.FunctionDefContext ctx) {
        String name = ctx.ID().getText();
        List<String> params = new ArrayList<>();
        if (ctx.paramList() != null) {
            for (TerminalNode id : ctx.paramList().ID()) {
                params.add(id.getText());
            }
        }
        List<AstNode> body = extractBlock(ctx.block());
        FunctionDef func = new FunctionDef(name, params, body, ctx.start.getLine());

        if (ctx.decorator() != null && !ctx.decorator().isEmpty()) {
            AstNode decorators = new AstNode("Decorators", ctx.start.getLine()) {};
            for (var dec : ctx.decorator()) {
                decorators.addChild(visit(dec));
            }
            func.addChild(decorators);
        }

        return func;
    }

    @Override
    public AstNode visitDecorator(flaskParser.DecoratorContext ctx) {
        String name = ctx.dotted_name().getText();
        List<AstNode> args = buildArgumentList(ctx.argumentList());
        return new Decorator(name, args, ctx.start.getLine());
    }

    @Override
    public AstNode visitIfStmt(flaskParser.IfStmtContext ctx) {
        AstNode cond = visit(ctx.expression(0));
        List<AstNode> ifBlock = extractBlock(ctx.block(0));
        IfStatement ifStmt = new IfStatement(cond, ifBlock, ctx.start.getLine());

        // Elif logic
        for (int i = 1; i < ctx.block().size(); i++) {
            if (i >= ctx.expression().size()) {
                List<AstNode> elseBlock = extractBlock(ctx.block(i));
                ifStmt.addChild(new AstNode("ElseBody", ctx.start.getLine()) {{
                    for(AstNode s : elseBlock) addChild(s);
                }});
            } else {
                AstNode elifCond = visit(ctx.expression(i));
                List<AstNode> elifBlock = extractBlock(ctx.block(i));
                ifStmt.addChild(new BinaryExpression(elifCond, "elif", new Program(0, elifBlock), 0));
            }
        }
        return ifStmt;
    }

    @Override
    public AstNode visitExpression(flaskParser.ExpressionContext ctx) {
        if (ctx.or_test().size() > 1) {
            List<AstNode> items = new ArrayList<>();
            for (var t : ctx.or_test()) items.add(visit(t));
            return new ListLiteral(items, ctx.start.getLine());
        }
        return visit(ctx.or_test(0));
    }

    @Override
    public AstNode visitOr_test(flaskParser.Or_testContext ctx) {
        AstNode left = visit(ctx.and_test(0));
        for (int i = 1; i < ctx.and_test().size(); i++) {
            AstNode right = visit(ctx.and_test(i));
            left = new BinaryExpression(left, "or", right, ctx.start.getLine());
        }
        return left;
    }

    @Override
    public AstNode visitAnd_test(flaskParser.And_testContext ctx) {
        AstNode left = visit(ctx.not_test(0));
        for (int i = 1; i < ctx.not_test().size(); i++) {
            AstNode right = visit(ctx.not_test(i));
            left = new BinaryExpression(left, "and", right, ctx.start.getLine());
        }
        return left;
    }

    @Override
    public AstNode visitNot_test(flaskParser.Not_testContext ctx) {
        if (ctx.NOT() != null) {
            AstNode operand = visit(ctx.not_test());
            return new UnaryExpression("not", operand, ctx.start.getLine());
        }
        return visit(ctx.comparison());
    }

    @Override
    public AstNode visitComparison(flaskParser.ComparisonContext ctx) {
        // comparison : arith_expr ((EQ|NEQ|LT|GT|LE|GE|(ASSIGN ASSIGN)) arith_expr)*
        AstNode left = visit(ctx.arith_expr(0));
        if (ctx.arith_expr().size() == 1) return left;

        int child = 1;
        for (int i = 1; i < ctx.arith_expr().size(); i++) {
            StringBuilder op = new StringBuilder();
            while (ctx.getChild(child) != ctx.arith_expr(i)) {
                op.append(ctx.getChild(child).getText());
                child++;
            }
            AstNode right = visit(ctx.arith_expr(i));
            left = new BinaryExpression(left, op.toString(), right, ctx.start.getLine());
            child++;
        }
        return left;
    }

    @Override
    public AstNode visitArith_expr(flaskParser.Arith_exprContext ctx) {
        AstNode left = visit(ctx.term(0));
        if (ctx.term().size() == 1) return left;

        for (int i = 1; i < ctx.term().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText(); // + or -
            AstNode right = visit(ctx.term(i));
            left = new BinaryExpression(left, op, right, ctx.start.getLine());
        }
        return left;
    }

    @Override
    public AstNode visitTerm(flaskParser.TermContext ctx) {
        AstNode left = visit(ctx.factor(0));
        if (ctx.factor().size() == 1) return left;

        for (int i = 1; i < ctx.factor().size(); i++) {
            String op = ctx.getChild(2 * i - 1).getText(); // * or /
            AstNode right = visit(ctx.factor(i));
            left = new BinaryExpression(left, op, right, ctx.start.getLine());
        }
        return left;
    }

    @Override
    public AstNode visitFactor(flaskParser.FactorContext ctx) {
        AstNode current = visit(ctx.primary());
        for (var tr : ctx.trailer()) {
            if (tr.LPAREN() != null) {
                List<AstNode> args = buildArgumentList(tr.argumentList());
                current = new FunctionCall(current, args, tr.start.getLine());
            } else if (tr.LBRACK() != null) {
                current = new Subscript(current, visit(tr.expression()), tr.start.getLine());
            } else if (tr.DOT() != null) {
                current = new AttributeAccess(current, tr.ID().getText(), tr.start.getLine());
            }
        }
        return current;
    }

    @Override
    public AstNode visitArgument(flaskParser.ArgumentContext ctx) {
        // argument : ID ASSIGN expression | expression
        if (ctx.ID() != null && ctx.ASSIGN() != null) {
            return new KeywordArgument(ctx.ID().getText(), visit(ctx.expression()), ctx.start.getLine());
        }
        return visit(ctx.expression());
    }

    /**
     * Builds a call/decorator argument list, applying the same comma-ambiguity
     * flattening as addFlattenedElements(): consecutive bare positional arguments
     * (e.g. "foo(a, b)") are just as susceptible to being absorbed into one
     * argument's expression as list-literal elements are. Keyword arguments
     * (ID ASSIGN expression) are unaffected since "ID ASSIGN" cannot continue an
     * expression's own comma chain, so they are always parsed as separate arguments.
     */
    private List<AstNode> buildArgumentList(flaskParser.ArgumentListContext ctx) {
        List<AstNode> args = new ArrayList<>();
        if (ctx == null) return args;
        for (var arg : ctx.argument()) {
            if (arg.ID() != null && arg.ASSIGN() != null) {
                args.add(visit(arg));
            } else {
                addFlattenedElements(arg.expression(), args);
            }
        }
        return args;
    }

    private List<AstNode> extractBlock(flaskParser.BlockContext ctx) {
        List<AstNode> list = new ArrayList<>();
        for (var stmt : ctx.statement()) {
            AstNode node = visit(stmt);
            if (node != null) list.add(node);
        }
        return list;
    }
}
