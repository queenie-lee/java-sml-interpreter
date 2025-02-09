package sml.instruction;

import sml.Label;

public class AdditionInstruction extends CalculateInstruction {

    public static final String OP_CODE = "add";
    private static final int hashCodeInt = OP_CODE.hashCode();

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     */
    public AdditionInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    public boolean equals(Object o) {
        return this.getClass().equals(o.getClass());
    }

    @Override
    public int hashCode() {
        return hashCodeInt;
    }

    @Override
    protected int calculate(int value1, int value2) {
        return value1 + value2;
    }
}
