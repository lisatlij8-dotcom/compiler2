package AST_H_C;

public class JinjaExpression extends HtmlElement{

    private String Expression;



    public JinjaExpression(String Name, int numberOfLine, String Expression) {
        super(Name, numberOfLine);
        this.Expression = Expression;
    }

    public String getExpression() {
        return Expression;
    }

    public void setExpression(String expression) {
        Expression = expression;
    }

    @Override
    public String toString() {
        return "JinjaExpression{" +
                "\nExpression='" + Expression + '\'' +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + getNumberOfLine() +
                '}';
    }
}
