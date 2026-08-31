package AST_H_C;

import java.util.List;

public class JinjaForBlock extends JinjaBlock{

    private String loopVariable;
    private String iterableExpression;

    public JinjaForBlock(String Name, int numberOfLine, List<HtmlElement> body) {
        this(Name, numberOfLine, null, null, body);
    }

    public JinjaForBlock(String Name, int numberOfLine, String loopVariable,
                         String iterableExpression, List<HtmlElement> body) {
        super(Name, numberOfLine, body);
        this.loopVariable = loopVariable;
        this.iterableExpression = iterableExpression;
    }

    public String getLoopVariable() {
        return loopVariable;
    }

    public String getIterableExpression() {
        return iterableExpression;
    }

    @Override
    public String toString() {
        return "JinjaForBlock{" +
                "\n\tloopVariable='" + loopVariable + '\'' +
                "\n\t, iterableExpression='" + iterableExpression + '\'' +
                "\n\tbody=" + body +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
