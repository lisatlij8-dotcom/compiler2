package AST;

import java.util.List;

public class IfStatement extends AstNode {
    private final AstNode condition;
    private final List<AstNode> body;

    public IfStatement(AstNode condition, List<AstNode> body, int line) {
        super("If", line);
        this.condition = condition;
        this.body = body;

        addChild(condition);
        for (AstNode stmt : body) {
            addChild(stmt);
        }
    }


    public List<AstNode> getIfBody() {
        return body;
    }

    public AstNode getCondition() {
        return condition;
    }
}
