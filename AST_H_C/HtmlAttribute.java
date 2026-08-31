package AST_H_C;

public class HtmlAttribute extends Node{

    private String attributeName;
    private String attributeValue;

    public HtmlAttribute(String Name, int numberOfLine, String attributeName, String attributeValue) {
        super(Name, numberOfLine);
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    @Override
    public String toString() {
        return "\n\tHtmlAttribute{" +
                "\n\t\tName='" + attributeName + '\'' +
                "\n\t\t, value='" + attributeValue + '\'' +
                "\n\t\t, numberOfLine=" + numberOfLine +
                '}';
    }
}
