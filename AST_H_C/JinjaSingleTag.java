package AST_H_C;

public class JinjaSingleTag  extends HtmlElement{

    private String tagType;
    private String content;
    private String target;
    private String value;

    public JinjaSingleTag(String Name, int numberOfLine) {
        this(Name, numberOfLine, null, null, null, null);
    }

    public JinjaSingleTag(String Name, int numberOfLine, String tagType, String content) {
        this(Name, numberOfLine, tagType, content, null, null);
    }

    public JinjaSingleTag(String Name, int numberOfLine, String tagType, String content,
                          String target, String value) {
        super(Name, numberOfLine);
        this.tagType = tagType;
        this.content = content;
        this.target = target;
        this.value = value;
    }

    public String getTagType() {
        return tagType;
    }

    public String getContent() {
        return content;
    }

    public String getTarget() {
        return target;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "JinjaSingleTag{" +
                "\ntagType='" + tagType + '\'' +
                "\n, content='" + content + '\'' +
                "\n, target='" + target + '\'' +
                "\n, value='" + value + '\'' +
                "\nName='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
