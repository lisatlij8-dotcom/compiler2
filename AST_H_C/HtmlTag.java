package AST_H_C;



import java.util.List;

public class HtmlTag extends HtmlElement {



    String tagName;
    private String closingTagName;
    private List<HtmlAttribute> attributes;
    List<HtmlElement> children;

    public HtmlTag(String Name, int numberOfLine, String tagName, List< HtmlAttribute> attributes, List<HtmlElement> children, String closingTagName) {
        super(Name, numberOfLine);
        this.tagName = tagName;
        this.attributes = attributes;
        this.children = children;
        this.closingTagName = closingTagName;
    }


    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getClosingTagName() {
        return closingTagName;
    }

    public void setClosingTagName(String closingTagName) {
        this.closingTagName = closingTagName;
    }

    public List< HtmlAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List< HtmlAttribute> attributes ) {
        this.attributes = attributes;
    }

    public List<HtmlElement> getChildren() {
        return children;
    }

    public void setChildren(List<HtmlElement> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "HtmlTag{" +
                "\n\ttagName='" + tagName + "\n" +
                "\t, attributes=" + attributes+ "\n"+
                "\t, children=\n\t\t" + children +""+
                '}';
    }
}
