package AST;

//public class FunctionDef extends AstNode {
//    private String functionName;      // اسم التابع
//    private List<String> parameters;  // قائمة أسماء البارامترات (IDs)
//    private List<AstNode> body;       // قائمة التعليمات داخل التابع
//    private List<Decorator> decorators = new ArrayList<>();
//    public void setDecorators(List<Decorator> decorators) {
//        this.decorators = decorators;
//    }
//    public FunctionDef(String functionName, List<String> parameters, List<AstNode> body, int linenumber) {
//
//        super("FunctionDefinition", linenumber);
//        this.functionName = functionName;
//        this.parameters = parameters;
//        this.body = body;
//    }
//
//    // Getters
//    public String getFunctionName() { return functionName; }
//    public List<String> getParameters() { return parameters; }
//    public List<AstNode> getBody() { return body; }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Function: ").append(functionName)
//                .append(" Params: ").append(parameters);
//
//        if (body != null && !body.isEmpty()) {
//            sb.append(" {\n");
//            for (AstNode node : body) {
//                // إضافة مسافة بادئة (Indentation) لترتيب الشكل
//                sb.append("      --> ").append(node.toString()).append("\n");
//            }
//            sb.append("   }");
//        }
//        return sb.toString();
//    }
//}
import java.util.List;

public class FunctionDef extends AstNode {
    private String name;
    private List<String> params;
    private List<AstNode> body;

    public FunctionDef(String name, List<String> params, List<AstNode> body, int line) {
        super("FunctionDef(" + name + ")", line);
        this.name = name;
        this.params = params;
        this.body = body;

        if (params != null) {
            for (String p : params) {
                addChild(new Identifier(p, line));
            }
        }

        if (body != null) {
            for (AstNode stmt : body) {
                addChild(stmt);
            }
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return params;
    }

    public List<AstNode> getBody() {
        return body;
    }
}
