package AST_H_C;

import java.util.List;

public class CSSRuleSet  extends Node{
    private String selector;
    private List<CssProperty> properties;

    public CSSRuleSet(String Name, int numberOfLine, String selector, List<CssProperty> properties) {
        super(Name, numberOfLine);
        this.selector = selector;
        this.properties = properties;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public List<CssProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<CssProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "CSSRuleSet{" +

                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                "\n\t, selector='" + selector + '\'' +
                "\n\t, properties=" + properties +
                '}';
    }
}
