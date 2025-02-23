package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sml.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ComparisonInstructionTest {

    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of(new CompareEqualInstruction(null, new Label ("L1")), 10, 10, 2),
                Arguments.of(new CompareEqualInstruction(null, new Label ("L1")), 10, -10, 1),
                Arguments.of(new CompareGreaterThanInstruction(null, new Label ("L1")), 10, -10, 1),
                Arguments.of(new CompareGreaterThanInstruction(null, new Label ("L1")), -10, 10, 2)
        );
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void validComparisonInstructions (Instruction ins0, int value1, int value2, int programCounter) throws BadProgramError {
        Instruction ins1 = new PrintInstruction(null);
        Instruction ins2 = new ReturnInstruction(new Label("L1"));
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));

        machine.frame().push(value2);
        machine.frame().push(value1); // flipped order so that value1 is at the top of the stack

        Optional<Frame> frame = ins0.execute(machine);
        assertEquals(programCounter, frame.get().programCounter());
    }

    private static Stream<Arguments> argumentsForMissingLabel() {
        return Stream.of(
                Arguments.of(new CompareEqualInstruction(null, new Label ("L1")), 10, 10, "L1"),
                Arguments.of(new CompareGreaterThanInstruction(null, new Label ("L1")), 10, -10, "L1")
        );
    }
    @ParameterizedTest
    @MethodSource("argumentsForMissingLabel")
    void missingLabelComparisonInstructions (Instruction ins0, int value1, int value2, String label) {
        Instruction ins1 = new PrintInstruction(null);
        Instruction ins2 = new ReturnInstruction(null);
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));

        machine.frame().push(value1);
        machine.frame().push(value2);

        LabelNotFoundException ex = assertThrows(LabelNotFoundException.class, () ->  ins0.execute(machine));
        // TODO: improve the test by adding fields for the label and method in the exception class
        assertEquals("Label " + label + " not found in main", ex.getMessage());
    }
}
