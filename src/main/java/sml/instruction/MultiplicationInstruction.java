package sml.instruction;

import sml.Label;

public class MultiplicationInstruction extends CalculateInstruction {
    public static final String OP_CODE = "mul";

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     */
    public MultiplicationInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected int calculate(int value1, int value2) {
        return Math.multiplyExact(value1, value2);
    }
}
