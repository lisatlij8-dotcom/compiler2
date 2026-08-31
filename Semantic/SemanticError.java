package Semantic;

public class SemanticError {

    public enum Severity {
        ERROR,
        WARNING
    }

    public enum Type {
        DUPLICATE_SYMBOL,
        UNDEFINED_VARIABLE,
        UNDEFINED_FUNCTION,
        INVALID_ASSIGNMENT,
        DUPLICATE_ROUTE,
        TEMPLATE_NOT_FOUND,
        UNDEFINED_JINJA_VARIABLE,
        DUPLICATE_HTML_ID,
        MISSING_CSS_TARGET,
        INCLUDE_NOT_FOUND,
        EXTENDS_NOT_FOUND,
        UNKNOWN_ROUTE,
        TEMPLATE_CONTEXT_MISMATCH,
        MISMATCHED_HTML_TAG
    }

    private final Type type;
    private final String message;
    private final int line;
    private final String sourceFile;
    private final Severity severity;

    public SemanticError(Type type, String message, int line, String sourceFile, Severity severity) {
        this.type = type;
        this.message = message;
        this.line = line;
        this.sourceFile = sourceFile;
        this.severity = severity;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public int getLine() {
        return line;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public Severity getSeverity() {
        return severity;
    }

    @Override
    public String toString() {
        return "[" + severity + "] " + type + " at " + sourceFile + ":" + line + " - " + message;
    }
}
