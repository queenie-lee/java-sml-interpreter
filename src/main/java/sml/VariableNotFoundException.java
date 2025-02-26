package sml;

public class VariableNotFoundException extends RuntimeException {
    public VariableNotFoundException(Variable.Identifier var) {
        super("Variable " + var + " not found");
    }
}
