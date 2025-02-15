package sml.instruction;

import sml.Label;

import java.util.Objects;

public class CompareEqualInstruction extends ComparisonInstruction{

    public static final String OP_CODE = "if_cmpeq";

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label       optional label (can be null)
     * @param branchLabel
     */
    public CompareEqualInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE, branchLabel);
    }

    @Override
    protected boolean compare(int value1, int value2) {
        return value1 == value2;
    }
}
