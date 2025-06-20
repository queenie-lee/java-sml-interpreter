package sml;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * <p>Represents an abstract instruction.</p>
 * The general form of the instruction is: [label:] opcode parameter-list
 * <b>label</b>: the optional label for the instruction. Other instructions may "jump" to this label.
 * <b>opcode</b>: the actual instruction name (operation code).
 * <b>parameter-list</b>: the comma-separated list of parameters for the instruction. These can be <b>integers</b>,
 * <b>variables</b>, <b>labels</b> for branching instructions, and <b>method names</b> used for method invocation.
 * <p>
 * Note that all classes extending from instruction must contain a public variable <b>OP_CODE</b> (operation code)
 * representing the actual instruction name.
 * @author Queenie Lee
 */

public abstract class Instruction {
    protected final Label label;
    protected final String opcode;
    private Integer hashCodeInt = null;

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
                // There is an extra space at the beginning of each instruction if no label exists.
                optionalLabel().map(s -> s + ":").orElse(""),
                opcode(),
                getOperandsString());
    }

    /* equals and hashCode methods are found in the Instruction abstract class below, rather than in subclasses.
    This decision was made to remove the amount of boilerplate code in subclasses. */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Instruction otherInstruction) {
            /* Instructions are uniquely identified by their toString() representation, as it contains
            label, opcode and parameter-list. Therefore, two Instruction objects are equal if and only if
            they have the same toString(). */
            return o.toString().equals(otherInstruction.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        /* hashCode is generated when the method is first invoked, subsequent calls will simply return
        the stored variable, improving performance. */
        if (hashCodeInt == null) {
            /* Analogous to the equals method above, the toString() representation uniquely identifies
            every Instruction. */
            this.hashCodeInt = this.toString().hashCode();
        }
        return hashCodeInt;
    }
}
