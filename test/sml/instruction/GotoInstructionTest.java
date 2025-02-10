package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sml.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class GotoInstructionTest {
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
    void validGotoInstruction() throws BadProgramError {
        Instruction ins0 = new GotoInstruction(null, new Label("L1"));
        Instruction ins1 = new ReturnInstruction(null);
        Instruction ins2 = new ReturnInstruction(new Label("L1"));

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));
        Optional<Frame> frame = ins0.execute(machine);
        assertEquals(2, frame.get().programCounter());
    }

    @Test
    void missingLabelGotoInstruction() {
        Instruction ins0 = new GotoInstruction(null, new Label("L2"));
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));
        LabelNotFoundException ex = assertThrows(LabelNotFoundException.class, () ->  ins0.execute(machine));
        // TODO: improve the test by adding fields for the label and method in the exception class
        assertEquals("Label L2 not found in main", ex.getMessage());
    }
}