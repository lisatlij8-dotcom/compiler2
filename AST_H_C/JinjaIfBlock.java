package AST_H_C;

import java.util.ArrayList;
import java.util.List;

public class JinjaIfBlock extends JinjaBlock{

    public static class ElifBranch {
        private final String condition;
        private final List<HtmlElement> body;

        public ElifBranch(String condition, List<HtmlElement> body) {
            this.condition = condition;
            this.body = body;
        }

        public String getCondition() {
            return condition;
        }

        public List<HtmlElement> getBody() {
            return body;
        }

        @Override
        public String toString() {
            return "ElifBranch{" +
                    "\n\tcondition='" + condition + '\'' +
                    "\n\t, body=" + body +
                    "\n}";
        }
    }

    private String condition;
    private List<ElifBranch> elifBranches;
    private List<HtmlElement> elseBody;

    public JinjaIfBlock(String Name, int numberOfLine, List<HtmlElement> body) {
        this(Name, numberOfLine, null, body, new ArrayList<>(), new ArrayList<>());
    }

    public JinjaIfBlock(String Name, int numberOfLine, String condition,
                        List<HtmlElement> body, List<ElifBranch> elifBranches,
                        List<HtmlElement> elseBody) {
        super(Name, numberOfLine, body);
        this.condition = condition;
        this.elifBranches = elifBranches;
        this.elseBody = elseBody;
    }

    public String getCondition() {
        return condition;
    }

    public List<HtmlElement> getIfBody() {
        return body;
    }

    public List<ElifBranch> getElifBranches() {
        return elifBranches;
    }

    public List<HtmlElement> getElseBody() {
        return elseBody;
    }

    @Override
    public String toString() {
        return "JinjaIfBlock{" +
                "\n\tcondition='" + condition + '\'' +
                "\n\tbody=" + body +
                "\n\t, elifBranches=" + elifBranches +
                "\n\t, elseBody=" + elseBody +
                "\n, Name='" + Name + '\'' +
                "\n, numberOfLine=" + numberOfLine +
                '}';
    }
}
