package AST_H_C;

import java.util.List;

public class JinjaTag extends HtmlElement{

    String tagContent;
    List<HtmlElement> body;

    public JinjaTag(String Name, int numberOfLine, String tagContent, List<HtmlElement> body) {
        super(Name, numberOfLine);
        this.tagContent = tagContent;
        this.body = body;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public List<HtmlElement> getBody() {
        return body;
    }

    public void setBody(List<HtmlElement> body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "JinjaTag{" +
                "\n\ttagContent='" + tagContent + '\'' +
                "\n, body=" + body +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
