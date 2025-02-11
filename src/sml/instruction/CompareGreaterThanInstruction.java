package sml.instruction;

import sml.Label;

import java.util.Objects;

public class CompareGreaterThanInstruction extends ComparisonInstruction {
    public static final String OP_CODE = "if_cmpgt";

    private final int hashCodeInt;

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label       optional label (can be null)
     * @param branchLabel
     */
    public CompareGreaterThanInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE, branchLabel);
        this.hashCodeInt = Objects.nonNull(label) ? label.hashCode() + OP_CODE.hashCode() : OP_CODE.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CompareGreaterThanInstruction otherInstruction) {
            return this.branchLabel.equals(otherInstruction.branchLabel) && this.label.equals(otherInstruction.label);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCodeInt;
    }

    @Override
    protected boolean compare(int value1, int value2) {
        return value1 > value2;
    }
}
