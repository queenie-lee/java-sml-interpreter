package sml.instruction;

import sml.BadProgramError;
import sml.Label;

public class DivisionInstruction extends CalculateInstruction {
    public static final String OP_CODE = "div";

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label optional label (can be null)
     */
    public DivisionInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    protected int calculate(int value1, int value2) {
        return value1 / value2;
    }
}
