package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public sealed abstract class ComparisonInstruction extends Instruction
        permits CompareEqualInstruction, CompareGreaterThanInstruction {

    protected final Label branchLabel;
    int value1;
    int value2;

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     * @param opcode operation name
     */
    public ComparisonInstruction(Label label, String opcode, Label branchLabel) {
        super(label, opcode);
        this.branchLabel = Objects.requireNonNull(branchLabel);
    }

    /**
     * Executes the instruction in the given machine.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
     */
    @Override
    public Optional<Frame> execute(Machine machine) throws BadProgramError {
        Frame frame = machine.frame();
        ComparisonInstruction instruction = this;
        this.value2 = frame.pop();
        this.value1 = frame.pop();
        boolean result = compare.apply(instruction);
        return result ? Optional.of(frame.jumpTo(branchLabel)) : Optional.of(frame.advance());
    }

    Function<ComparisonInstruction, Boolean> compare = c ->
            switch (c) {
                case CompareEqualInstruction eq -> eq.value1 == eq.value2;
                case CompareGreaterThanInstruction gt -> gt.value1 > gt.value2;
            };

    /**
     * Returns a string representation of the operands.
     * Used in toString().
     *
     * @return a string representation of the operands
     */
    @Override
    protected String getOperandsString() {
        return branchLabel.toString();
    }

}
