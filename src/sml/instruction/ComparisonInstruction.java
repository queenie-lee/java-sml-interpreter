package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;

public abstract class ComparisonInstruction extends Instruction  {

    protected final Label branchLabel;
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
        int value1 = frame.pop();
        int value2 = frame.pop();
        boolean result = compare(value1, value2);
        return result ? Optional.of(frame.jumpTo(branchLabel)) : Optional.of(frame.advance());
    }

    protected abstract boolean compare(int value1, int value2);

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
