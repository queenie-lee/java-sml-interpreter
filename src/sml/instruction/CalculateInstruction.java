package sml.instruction;

import sml.*;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Calculate Instruction is an abstract class for all calculation-type subclasses.
 */
public abstract class CalculateInstruction extends Instruction {

    /**
     * Constructor: an instruction with a label and an opcode
     * (opcode must be an operation of the language)
     *
     * @param label  optional label (can be null)
     * @param opcode operation name
     */
    public CalculateInstruction(Label label, String opcode) {
        super(label, opcode);
    }

    /**
     * Executes the instruction in the given machine.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
     */
    @Override // create BadProgramError Exception (custom) - look at PiJ
    public Optional<Frame> execute(Machine machine) throws BadProgramError {
        Frame frame = machine.frame();
        int value1, value2;
        value1 = frame.pop();
        value2 = frame.pop();
        int result = calculate(value1, value2);
        frame.push(result);
        return Optional.of(frame.advance());
    }

    protected abstract int calculate (int value1, int value2) throws BadProgramError;

    /**
     * Returns a string representation of the operands.
     * Used in toString().
     *
     * @return a string representation of the operands
     */
    @Override
    protected String getOperandsString() {
        return "";
    }
}
