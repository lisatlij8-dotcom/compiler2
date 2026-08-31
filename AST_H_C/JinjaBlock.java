package AST_H_C;

import java.util.List;

public abstract class JinjaBlock extends HtmlElement{

    protected List<HtmlElement> body;

    public JinjaBlock(String Name, int numberOfLine, List<HtmlElement> body) {
        super(Name, numberOfLine);
        this.body = body;
    }

    public List<HtmlElement> getBody() {
        return body;
    }

    public void setBody(List<HtmlElement> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "JinjaBlock{" +
                "\n\tbody=" + body +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }


}
