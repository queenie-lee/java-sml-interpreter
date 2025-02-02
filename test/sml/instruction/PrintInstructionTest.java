package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintInstructionTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private Machine machine;

    @BeforeEach
    void setUp() {
        machine = new Machine();
    }

    @AfterEach
    void tearDown() {
        machine = null;
    }

    @Test
    void printInstruction() {
        Instruction ins0 = new PrintInstruction(null);
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));
        System.setOut(new PrintStream(outContent));

        machine.frame().push(42);
        ins0.execute(machine);

        assertEquals("42\n", outContent.toString());
    }
}
