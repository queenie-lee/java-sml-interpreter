package sml;

public class LabelNotFoundException extends RuntimeException {
    public LabelNotFoundException(Label label, Method method) {
        super("Label " + label + " not found in " + method.name());
    }
}
