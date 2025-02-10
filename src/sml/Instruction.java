package sml;

// TODO: write JavaDoc for the class

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents an abstract instruction.
 *
 * @author Queenie Lee
 */
public abstract class Instruction {
    protected final Label label;
    protected final String opcode;

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label optional label (can be null)
     * @param opcode operation name
     */
    public Instruction(Label label, String opcode) {
        this.label = label;
        this.opcode = Objects.requireNonNull(opcode);
    }

    public Optional<Label> optionalLabel() {
        return Optional.ofNullable(label);
    }

    public String opcode() {
        return opcode;
    }

    /**
     * Returns the stream of variables in the operands of the instruction.
     * This method must be overridden if the instruction has any variables.
     *
     * @return the stream of variables
     */

    public Stream<Variable.Identifier> variables() {
        return Stream.of();
    }

    /**
     * Executes the instruction in the given machine.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
     */

    public abstract Optional<Frame> execute(Machine machine) throws BadProgramError;

    /**
     * Returns a string representation of the operands.
     * Used in toString().
     *
     * @return a string representation of the operands
     */

    protected abstract String getOperandsString();

    /**
     * Returns a string representation of the instruction.
     * It consists of
     *    - the optional label followed by a colon,
     *    - the opcode,
     *    - the operands (@see getOperandsString).
     *
     * @return a string representation of the instruction
     */

    @Override
    public String toString() {
        return String.join(" ",
                optionalLabel().map(s -> s + ":").orElse(""),
                opcode(),
                getOperandsString());
    }

    // Completed: Make sure the subclasses override .equals and .hashCode

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
