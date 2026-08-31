package visitor;

import AST.*;
import Semantic.SemanticError;
import Semantic.SemanticErrorReporter;
import SymbolTable.Scope;
import SymbolTable.Symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PythonSemanticVisitor {

    public static class RouteInfo {
        private final String path;
        private final String functionName;
        private final int line;
        private final List<String> methods;

        public RouteInfo(String path, String functionName, int line, List<String> methods) {
            this.path = path;
            this.functionName = functionName;
            this.line = line;
            this.methods = methods;
        }

        public String getPath() {
            return path;
        }

        public String getFunctionName() {
            return functionName;
        }

        public int getLine() {
            return line;
        }

        public List<String> getMethods() {
            return methods;
        }
    }

    public static class TemplateRenderInfo {
        private final String templateName;
        private final String functionName;
        private final int line;
        private final Map<String, AstNode> contextValues;

        public TemplateRenderInfo(String templateName, String functionName, int line,
                                  Map<String, AstNode> contextValues) {
            this.templateName = templateName;
            this.functionName = functionName;
            this.line = line;
            this.contextValues = contextValues;
        }

        public String getTemplateName() {
            return templateName;
        }

        public String getFunctionName() {
            return functionName;
        }

        public int getLine() {
            return line;
        }

        public Map<String, AstNode> getContextValues() {
            return contextValues;
        }
    }

    // "__name__" and "__file__" are implicit Python module-level globals,
    // always available with no import/definition required in any module -
    // not project-specific data, so they belong alongside the other
    // language builtins rather than being flagged as undefined.
    private static final Set<String> BUILTINS = Set.of(
            "print", "len", "range", "str", "int", "float", "list", "dict",
            "__name__", "__file__"
    );

    private final SemanticErrorReporter reporter;
    private final String sourceFile;
    private final Scope globalScope = new Scope(null, "Global", "Global");
    private final List<RouteInfo> routes = new ArrayList<>();
    private final List<TemplateRenderInfo> templateRenders = new ArrayList<>();
    private final Set<String> routeKeys = new HashSet<>();
    private Scope currentScope = globalScope;
    private String currentFunctionName = null;
    private int functionDepth = 0;

    public PythonSemanticVisitor(SemanticErrorReporter reporter, String sourceFile) {
        this.reporter = reporter;
        this.sourceFile = sourceFile;
    }

    public void analyze(AstNode root) {
        visit(root);
    }

    public Scope getGlobalScope() {
        return globalScope;
    }

    public List<RouteInfo> getRoutes() {
        return routes;
    }

    public List<TemplateRenderInfo> getTemplateRenders() {
        return templateRenders;
    }

    private void visit(AstNode node) {
        if (node == null) return;

        if (node instanceof Program) {
            visitChildren(node);
        } else if (node instanceof ImportStatement) {
            visitImport((ImportStatement) node);
        } else if (node instanceof Assign) {
            visitAssign((Assign) node);
        } else if (node instanceof FunctionDef) {
            visitFunctionDef((FunctionDef) node);
        } else if (node instanceof ForStatement) {
            visitForStatement((ForStatement) node);
        } else if (node instanceof IfStatement) {
            visitIfStatement((IfStatement) node);
        } else if (node instanceof ReturnStatement) {
            visitReturnStatement((ReturnStatement) node);
        } else {
            visitExpression(node);
        }
    }

    private void visitImport(ImportStatement node) {
        Set<String> seen = new HashSet<>();
        for (String name : node.getImportedNames()) {
            if (!seen.add(name) || currentScope.isDefinedLocally(name)) {
                duplicate(name, node.getLine(), "Duplicate imported name '" + name + "'");
            } else {
                currentScope.define(name, "Imported Name", node.getLine());
            }
        }
    }

    private void visitAssign(Assign node) {
        visitExpression(node.getRight());
        if (node.getLeft() instanceof Identifier) {
            Identifier id = (Identifier) node.getLeft();
            if (!currentScope.isDefinedLocally(id.getName())) {
                currentScope.define(id.getName(), "Variable", id.getLine());
            }
        } else {
            visitAssignmentTarget(node.getLeft());
        }
    }

    private void visitFunctionDef(FunctionDef node) {
        if (currentScope.isDefinedLocally(node.getName())) {
            duplicate(node.getName(), node.getLine(), "Duplicate function name '" + node.getName() + "'");
        } else {
            currentScope.define(node.getName(), "Function", node.getLine());
        }

        collectRoutes(node);

        Scope previous = currentScope;
        String previousFunction = currentFunctionName;
        currentScope = new Scope(previous, "Local (" + node.getName() + ")", "Function");
        currentFunctionName = node.getName();
        functionDepth++;

        Set<String> params = new HashSet<>();
        for (String param : node.getParameters()) {
            if (!params.add(param) || currentScope.isDefinedLocally(param)) {
                duplicate(param, node.getLine(), "Duplicate parameter '" + param + "'");
            } else {
                currentScope.define(param, "Parameter", node.getLine());
            }
        }

        for (AstNode stmt : node.getBody()) {
            visit(stmt);
        }

        functionDepth--;
        currentFunctionName = previousFunction;
        currentScope = previous;
    }

    private void visitForStatement(ForStatement node) {
        visitExpression(node.getIterable());
        if (!currentScope.isDefinedLocally(node.getIteratorId())) {
            currentScope.define(node.getIteratorId(), "Variable (Loop)", node.getLine());
        }
        for (AstNode stmt : node.getBody()) {
            visit(stmt);
        }
    }

    private void visitIfStatement(IfStatement node) {
        visitExpression(node.getCondition());
        for (AstNode stmt : node.getIfBody()) {
            visit(stmt);
        }
    }

    private void visitReturnStatement(ReturnStatement node) {
        if (functionDepth == 0) {
            reporter.error(
                    SemanticError.Type.INVALID_ASSIGNMENT,
                    "Return statement outside function",
                    node.getLine(),
                    sourceFile
            );
        }
        visitExpression(node.getValue());
    }

    private void visitExpression(AstNode node) {
        if (node == null) return;

        if (node instanceof Identifier) {
            checkIdentifier((Identifier) node);
        } else if (node instanceof FunctionCall) {
            visitFunctionCall((FunctionCall) node);
        } else if (node instanceof AttributeAccess) {
            visitExpression(((AttributeAccess) node).getTarget());
        } else if (node instanceof BinaryExpression) {
            BinaryExpression binary = (BinaryExpression) node;
            visitExpression(binary.getLeft());
            visitExpression(binary.getRight());
        } else if (node instanceof UnaryExpression) {
            visitExpression(((UnaryExpression) node).getOperand());
        } else if (node instanceof Subscript) {
            Subscript subscript = (Subscript) node;
            visitExpression(subscript.getTarget());
            visitExpression(subscript.getIndex());
        } else if (node instanceof ListLiteral) {
            for (AstNode child : ((ListLiteral) node).getElements()) {
                visitExpression(child);
            }
        } else if (node instanceof DictLiteral) {
            for (Map.Entry<AstNode, AstNode> entry : ((DictLiteral) node).getEntries().entrySet()) {
                visitExpression(entry.getKey());
                visitExpression(entry.getValue());
            }
        } else if (node instanceof KeywordArgument) {
            visitExpression(((KeywordArgument) node).getValue());
        } else if (node instanceof ReturnStatement) {
            visitReturnStatement((ReturnStatement) node);
        }
    }

    private void visitFunctionCall(FunctionCall call) {
        AstNode function = call.getFunction();

        if (function instanceof Identifier) {
            Identifier id = (Identifier) function;
            Symbol symbol = currentScope.lookup(id.getName());
            if (symbol == null && !BUILTINS.contains(id.getName())) {
                reporter.error(
                        SemanticError.Type.UNDEFINED_FUNCTION,
                        "Function '" + id.getName() + "' is not defined",
                        id.getLine(),
                        sourceFile
                );
            }
            if ("render_template".equals(id.getName())) {
                collectRenderTemplate(call);
            }
        } else {
            visitExpression(function);
        }

        for (AstNode arg : call.getArguments()) {
            visitExpression(arg);
        }
    }

    private void visitAssignmentTarget(AstNode node) {
        if (node instanceof AttributeAccess) {
            visitExpression(((AttributeAccess) node).getTarget());
        } else if (node instanceof Subscript) {
            Subscript subscript = (Subscript) node;
            visitExpression(subscript.getTarget());
            visitExpression(subscript.getIndex());
        } else {
            visitExpression(node);
        }
    }

    private void checkIdentifier(Identifier id) {
        if (currentScope.lookup(id.getName()) == null && !BUILTINS.contains(id.getName())) {
            reporter.error(
                    SemanticError.Type.UNDEFINED_VARIABLE,
                    "Variable '" + id.getName() + "' is not defined",
                    id.getLine(),
                    sourceFile
            );
        }
    }

    private void collectRoutes(FunctionDef function) {
        for (AstNode child : function.getChildren()) {
            if (!"Decorators".equals(getNodeLabel(child))) {
                continue;
            }
            for (AstNode decoratorNode : child.getChildren()) {
                if (!(decoratorNode instanceof Decorator)) {
                    continue;
                }
                Decorator decorator = (Decorator) decoratorNode;
                if (!"app.route".equals(decorator.getName())) {
                    continue;
                }
                RouteInfo route = buildRouteInfo(decorator, function.getName());
                if (route == null) {
                    continue;
                }
                routes.add(route);
                for (String method : route.getMethods()) {
                    String key = route.getPath() + "#" + method;
                    if (!routeKeys.add(key)) {
                        reporter.error(
                                SemanticError.Type.DUPLICATE_ROUTE,
                                "Duplicate Flask route '" + route.getPath() + "' for method " + method,
                                route.getLine(),
                                sourceFile
                        );
                    }
                }
            }
        }
    }

    private RouteInfo buildRouteInfo(Decorator decorator, String functionName) {
        List<AstNode> args = decorator.getChildren();
        if (args.isEmpty() || !(args.get(0) instanceof StringLiteral)) {
            return null;
        }

        String path = ((StringLiteral) args.get(0)).getValue();
        List<String> methods = new ArrayList<>();
        for (AstNode arg : args) {
            if (arg instanceof KeywordArgument) {
                KeywordArgument keyword = (KeywordArgument) arg;
                if ("methods".equals(keyword.getName()) && keyword.getValue() instanceof ListLiteral) {
                    for (AstNode methodNode : ((ListLiteral) keyword.getValue()).getElements()) {
                        if (methodNode instanceof StringLiteral) {
                            methods.add(((StringLiteral) methodNode).getValue());
                        }
                    }
                }
            }
        }
        if (methods.isEmpty()) {
            methods.add("GET");
        }
        return new RouteInfo(path, functionName, decorator.getLine(), methods);
    }

    private void collectRenderTemplate(FunctionCall call) {
        List<AstNode> args = call.getArguments();
        if (args.isEmpty() || !(args.get(0) instanceof StringLiteral)) {
            return;
        }

        Map<String, AstNode> context = new LinkedHashMap<>();
        for (int i = 1; i < args.size(); i++) {
            AstNode arg = args.get(i);
            if (arg instanceof KeywordArgument) {
                KeywordArgument keyword = (KeywordArgument) arg;
                context.put(keyword.getName(), keyword.getValue());
            }
        }
        templateRenders.add(new TemplateRenderInfo(
                ((StringLiteral) args.get(0)).getValue(),
                currentFunctionName,
                call.getLine(),
                context
        ));
    }

    private void visitChildren(AstNode node) {
        for (AstNode child : node.getChildren()) {
            visit(child);
        }
    }

    private String getNodeLabel(AstNode node) {
        String text = node.toString();
        int lineIndex = text.indexOf(" (line ");
        return lineIndex >= 0 ? text.substring(0, lineIndex) : text;
    }

    private void duplicate(String name, int line, String message) {
        reporter.error(SemanticError.Type.DUPLICATE_SYMBOL, message, line, sourceFile);
    }
}
