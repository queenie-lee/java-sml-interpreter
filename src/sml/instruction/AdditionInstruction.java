package sml.instruction;

import sml.BadProgramError;
import sml.Label;

import java.util.NoSuchElementException;

public class AdditionInstruction extends CalculateInstruction {

    public static final String OP_CODE = "add";

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
    protected int calculate(int value1, int value2) {
        return Math.addExact(value1, value2);
    }
}
