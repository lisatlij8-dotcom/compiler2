package AST;

public class UnaryExpression extends AstNode {

    private final String operator;
    private final AstNode operand;

    public UnaryExpression(String operator, AstNode operand, int line) {
        super("UnaryOp(" + operator + ")", line);
        this.operator = operator;
        this.operand = operand;

        addChild(operand);
    }

    public String getOperator() {
        return operator;
    }

    public AstNode getOperand() {
        return operand;
    }
}
