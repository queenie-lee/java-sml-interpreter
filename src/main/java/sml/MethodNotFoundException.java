package sml;

public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException(Method.Identifier method) {
        super("Method not found: " + method);
    }
}
