package sml.instruction;

import sml.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * <p>An abstract class for all calculation-type subclasses.</p>
 * The calculation is performed on two integer variables, value1 and value2.
 * It is sealed, only allowing the classes mentioned after the permits clause to extend from this class.
 *
 * @author Queenie Lee
 */
public sealed abstract class CalculateInstruction extends Instruction
        permits AdditionInstruction, DivisionInstruction, MultiplicationInstruction, SubtractionInstruction {

    int value1;
    int value2;

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
     * <p>Executes the instruction in the given machine.</p>
     * In future development, an abstract class can be created between the instruction, the comparison and calculate
     * instruction classes, to further avoid code duplication.
     *
     * @param machine the machine the instruction runs on
     * @return the new frame with an update instruction index
     */
    @Override // create BadProgramError Exception (custom) - look at PiJ
    public Optional<Frame> execute(Machine machine) throws BadProgramError {
        Frame frame = machine.frame();
        CalculateInstruction instruction = this;
        this.value2 = frame.pop();
        this.value1 = frame.pop();
        int result;
        try {
            result = calculate.apply(instruction);
        } catch (ArithmeticException ex) {
            throw new BadProgramError(ex.toString());
        }
        frame.push(result);
        return Optional.of(frame.advance());
    }

    /**
     * This helper method performs pattern matching on a switch expression, applying the calculation based on the
     * object's Instruction subclass. It is obligatory to add a new case below if any extensions (i.e. new subclasses)
     * are added to this class.
     */
    Function<CalculateInstruction,Integer> calculate = c ->
            switch (c) {
                case AdditionInstruction a -> Math.addExact(a.value1, a.value2);
                case SubtractionInstruction s -> Math.subtractExact(s.value1,s.value2);
                case MultiplicationInstruction m -> Math.multiplyExact(m.value1, m.value2);
                case DivisionInstruction d -> d.value1 / d.value2;
            };

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
