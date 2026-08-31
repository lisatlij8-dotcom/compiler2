package CodeGeneration;

import AST.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Evaluates the safe, literal subset of the Python AST - string/number/
 * boolean/None/list/dict literals, and top-level global variable
 * assignments built from them - into plain Java runtime values the
 * Generator can render with.
 *
 * This is AST evaluation of constant literals only. It never executes
 * Python code: no function calls, no arithmetic, no control flow. Anything
 * outside that literal subset raises CodeGenerationException instead of
 * being guessed at.
 */
public class PythonDataExtractor {

    /**
     * Walks the top-level statements of the Python Program AST and evaluates
     * every "name = <literal>" global assignment into a runtime value.
     * Assignments whose right-hand side is not a supported literal (e.g.
     * "app = Flask(...)") are simply omitted - not every global needs to be
     * generation data, so this is not treated as an error here.
     */
    public Map<String, Object> extractGlobals(AstNode pythonRoot) {
        Map<String, Object> globals = new LinkedHashMap<>();
        if (pythonRoot == null) {
            return globals;
        }
        for (AstNode statement : pythonRoot.getChildren()) {
            if (statement instanceof Assign) {
                Assign assign = (Assign) statement;
                if (assign.getLeft() instanceof Identifier) {
                    String name = ((Identifier) assign.getLeft()).getName();
                    try {
                        globals.put(name, evaluateLiteral(assign.getRight()));
                    } catch (CodeGenerationException notALiteral) {
                        // Not a compile-time constant - unavailable as generation
                        // data, not a code generation failure by itself.
                    }
                }
            }
        }
        return globals;
    }

    /**
     * Evaluates a single supported literal AST node into its runtime value.
     * Throws CodeGenerationException for anything outside the supported
     * subset instead of guessing.
     */
    public Object evaluateLiteral(AstNode node) {
        if (node == null) {
            return null;
        }
        if (node instanceof StringLiteral) {
            return ((StringLiteral) node).getValue();
        }
        if (node instanceof NumberLiteral) {
            return parseNumber(((NumberLiteral) node).getValue());
        }
        if (node instanceof BooleanLiteral) {
            return ((BooleanLiteral) node).getValue();
        }
        if (node instanceof NoneLiteral) {
            return null;
        }
        if (node instanceof ListLiteral) {
            List<Object> list = new ArrayList<>();
            for (AstNode element : ((ListLiteral) node).getElements()) {
                list.add(evaluateLiteral(element));
            }
            return list;
        }
        if (node instanceof DictLiteral) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (Map.Entry<AstNode, AstNode> entry : ((DictLiteral) node).getEntries().entrySet()) {
                if (!(entry.getKey() instanceof StringLiteral)) {
                    throw new CodeGenerationException(
                            "Unsupported dict key at line " + node.getLine()
                                    + ": code generation only supports string-literal dict keys");
                }
                String key = ((StringLiteral) entry.getKey()).getValue();
                map.put(key, evaluateLiteral(entry.getValue()));
            }
            return map;
        }
        throw new CodeGenerationException(
                "Unsupported Python expression at line " + node.getLine()
                        + ": code generation only evaluates literal constants "
                        + "(strings, numbers, booleans, None, lists, dicts), not "
                        + node.getClass().getSimpleName());
    }

    private Object parseNumber(String text) {
        if (text.contains(".")) {
            return Double.parseDouble(text);
        }
        return Long.parseLong(text);
    }
}
