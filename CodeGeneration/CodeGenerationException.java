package CodeGeneration;

/**
 * Raised when the Generator encounters something it cannot safely resolve
 * from the supported literal/data subset - e.g. a render_template context
 * value that is not a compile-time constant (a loop-bound variable), a
 * Python expression that is not a literal, or a Jinja construct outside the
 * supported minimal subset. Code Generation reports this as a clear
 * diagnostic instead of guessing or fabricating output.
 */
public class CodeGenerationException extends RuntimeException {
    public CodeGenerationException(String message) {
        super(message);
    }
}
