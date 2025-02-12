package sml.instruction;

import sml.*;

import java.util.Optional;

public class PushInstruction extends Instruction {
    public static final String OP_CODE = "push";

    private final int value;
    private final int hashCodeInt;
    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     */
    public PushInstruction(Label label, int value) {
        super(label, OP_CODE);
        this.value = value;
        this.hashCodeInt = this.value + OP_CODE.hashCode();
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
        frame.push(value);
        System.out.println(value);
        return Optional.of(frame.advance());
    }

    /**
     * Returns a string representation of the operands.
     * Used in toString().
     *
     * @return a string representation of the operands
     */
    @Override
    protected String getOperandsString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PushInstruction otherInstruction) {
            return this.value == otherInstruction.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return hashCodeInt;
    }
}
