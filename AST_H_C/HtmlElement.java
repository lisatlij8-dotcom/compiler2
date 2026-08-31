package AST_H_C;

public abstract class HtmlElement extends Node {

    public HtmlElement(String Name, int numberOfLine) {
        super(Name, numberOfLine);
    }

    @Override
    public String toString() {
        return "HtmlElement{" +
                "\nName='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
