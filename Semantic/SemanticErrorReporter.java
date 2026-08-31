package Semantic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemanticErrorReporter {

    private final List<SemanticError> diagnostics = new ArrayList<>();

    public void report(SemanticError error) {
        if (error != null) {
            diagnostics.add(error);
        }
    }

    public void error(SemanticError.Type type, String message, int line, String sourceFile) {
        report(new SemanticError(type, message, line, sourceFile, SemanticError.Severity.ERROR));
    }

    public void warning(SemanticError.Type type, String message, int line, String sourceFile) {
        report(new SemanticError(type, message, line, sourceFile, SemanticError.Severity.WARNING));
    }

    public List<SemanticError> getErrors() {
        return Collections.unmodifiableList(diagnostics);
    }

    public boolean hasErrors() {
        for (SemanticError diagnostic : diagnostics) {
            if (diagnostic.getSeverity() == SemanticError.Severity.ERROR) {
                return true;
            }
        }
        return false;
    }

    public int getErrorCount() {
        int count = 0;
        for (SemanticError diagnostic : diagnostics) {
            if (diagnostic.getSeverity() == SemanticError.Severity.ERROR) {
                count++;
            }
        }
        return count;
    }

    public int getWarningCount() {
        int count = 0;
        for (SemanticError diagnostic : diagnostics) {
            if (diagnostic.getSeverity() == SemanticError.Severity.WARNING) {
                count++;
            }
        }
        return count;
    }

    public void clear() {
        diagnostics.clear();
    }

    public void printAll() {
        if (diagnostics.isEmpty()) {
            System.out.println("No semantic diagnostics.");
            return;
        }
        for (SemanticError diagnostic : diagnostics) {
            System.out.println(diagnostic);
        }
    }
}
