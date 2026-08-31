package AST_H_C;

public class HtmlText extends HtmlElement {
    private String text;

    public HtmlText(String Name, int numberOfLine, String text) {
        super(Name, numberOfLine);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "HtmlText{" +
                "\n, text='" + text + '\'' +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
