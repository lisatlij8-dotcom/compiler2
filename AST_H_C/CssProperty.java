package AST_H_C;

public class CssProperty extends Node{

    private  String property;
    private  String value;

    public CssProperty(String Name, int numberOfLine, String property, String value) {
        super(Name, numberOfLine);
        this.property = property;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CssProperty{" +

                "\n,Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                "\n, property='" + property + '\'' +
                "\n, value='" + value + '\'' +
                '}';
    }
}
