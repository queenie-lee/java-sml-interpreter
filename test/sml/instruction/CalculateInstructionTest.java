package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sml.*;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculateInstructionTest {

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
                Arguments.of(new AdditionInstruction(null), 5),
                Arguments.of(new SubtractionInstruction(null), 1),
                Arguments.of(new MultiplicationInstruction(null), 6),
                Arguments.of(new DivisionInstruction(null), 1)
        );
    }
    @ParameterizedTest
    @MethodSource("arguments")
    public void validCalculationInstruction(Instruction ins0, int expectedValue) throws BadProgramError {
        Instruction ins1 = new PrintInstruction(null);
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));

        machine.frame().push(1000);
        machine.frame().push(100);
        machine.frame().push(2);
        machine.frame().push(3);
        var result = ins0.execute(machine);
        var value = result.get().pop();

        assertEquals(expectedValue, value);
    }

    @ParameterizedTest
    @MethodSource("arguments")
    void stackTooSmall(Instruction ins0, int expectedValue) {
        Instruction ins1 = new PrintInstruction(null);
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));

        machine.frame().push(3);

        BadProgramError thrown = assertThrows(
                BadProgramError.class,
                () -> ins0.execute(machine),
                "Expected execute(machine) to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Not enough values on the stack."));
    }

    @Test
    void divideByZero() {
        Instruction ins0 = new DivisionInstruction(null);
        Instruction ins1 = new PrintInstruction(null);
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));

        machine.frame().push(0);
        machine.frame().push(3);

        BadProgramError thrown = assertThrows(
                BadProgramError.class,
                () -> ins0.execute(machine),
                "Expected execute(machine) to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("/ by zero"));
    }

    private static Stream<Arguments> argumentsForOverflowTest() {
        return Stream.of(
                Arguments.of(new AdditionInstruction(null), Integer.MAX_VALUE, 10),
                Arguments.of(new AdditionInstruction(null), Integer.MIN_VALUE, -10),
                Arguments.of(new SubtractionInstruction(null), -10, Integer.MAX_VALUE),
                Arguments.of(new SubtractionInstruction(null), 10, Integer.MIN_VALUE),
                Arguments.of(new MultiplicationInstruction(null), Integer.MAX_VALUE, 10),
                Arguments.of(new MultiplicationInstruction(null), Integer.MAX_VALUE, -10)
        );
    }
    @ParameterizedTest
    @MethodSource("argumentsForOverflowTest")
    void calculationResultIsAnOverflowNumber (Instruction ins0, int value1, int value2) {
        Instruction ins1 = new PrintInstruction(null);
        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));

        machine.frame().push(value2);
        machine.frame().push(value1); // flipped order so that value1 is at the top of the stack

        BadProgramError thrown = assertThrows(
                BadProgramError.class,
                () -> ins0.execute(machine),
                "Expected execute(machine) to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("overflow"));
    }
}
