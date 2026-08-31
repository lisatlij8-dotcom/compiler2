package AST_H_C;

import java.util.List;

public class CSS_Style extends HtmlElement{

    private List<CSSRuleSet> ruleSets;

    public CSS_Style(String Name, int numberOfLine, List<CSSRuleSet> ruleSets) {
        super(Name, numberOfLine);
        this.ruleSets = ruleSets;
    }

    public List<CSSRuleSet> getRuleSets() {
        return ruleSets;
    }

    public void setRuleSets(List<CSSRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    @Override
    public String toString() {
        return "\nCSS_Style{" +
                "\n \truleSets=" + ruleSets +
                "\n}";
    }
}
