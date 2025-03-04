package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * <p>An abstract class for all comparison-type subclasses.</p>
 * The comparison is performed on two integer variables, value1 and value2.
 * It is sealed, only allowing the classes mentioned after the permits clause to extend from this class.
 *
 * @author Queenie Lee
 */
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
     * <p>Executes the instruction in the given machine.</p>
     * In future development, an abstract class can be created between the instruction, the comparison and calculate
     * instruction classes, to further avoid code duplication.
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

    /**
     * This helper method performs pattern matching on a switch expression, applying the comparison based on the
     * object's Instruction subclass. It is obligatory to add a new case below if any extensions (i.e. new subclasses)
     * are added to this class.
     */
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
