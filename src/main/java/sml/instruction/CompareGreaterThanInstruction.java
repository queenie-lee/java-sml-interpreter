package sml.instruction;

import sml.Label;

import java.util.Objects;

public non-sealed class CompareGreaterThanInstruction extends ComparisonInstruction {
    public static final String OP_CODE = "if_cmpgt";

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label       optional label (can be null)
     * @param branchLabel
     */
    public CompareGreaterThanInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE, branchLabel);
    }

//    @Override
//    protected boolean compare(int value1, int value2) {
//        return value1 > value2;
//    }
}
