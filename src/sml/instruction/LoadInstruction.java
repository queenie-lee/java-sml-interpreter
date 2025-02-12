package sml.instruction;

import sml.*;

import java.util.Optional;
import java.util.stream.Stream;

public class LoadInstruction extends Instruction {
    public static final String OP_CODE = "load";
    private final Variable.Identifier variable;
    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     */
    public LoadInstruction(Label label, String name) {
        super(label, OP_CODE);
        this.variable = new Variable.Identifier(name);
    }

    /**
     * Returns the stream of variables in the operands of the instruction.
     * This method must be overridden if the instruction has any variables.
     *
     * @return the stream of variables
     */
    @Override
    public Stream<Variable.Identifier> variables() {
        return Stream.of(variable);
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
        var variable_value = frame.variable(variable)
                .load()
                .orElseThrow(
                        () -> new BadProgramError("The variable " + variable.name() +
                                " does not contain a value."));
        frame.push(variable_value);
        System.out.println(variable_value);
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
        return null;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
