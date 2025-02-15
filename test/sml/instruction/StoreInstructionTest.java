package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoreInstructionTest {
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
    void storeInstructionIsSuccessful() throws BadProgramError {
        Variable.Identifier variable = new Variable.Identifier("Bob");
        Optional<Integer> value = Optional.of(55);

        Instruction ins0 = new StoreInstruction(null, variable.name());
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));
        machine.frame().push(value.get());
        assertEquals(Set.of(variable), m.localVariables()); // Checks that variable "Bob" exists in the Frame's local variables
        ins0.execute(machine);
        assertEquals(value, machine.frame().variable(variable).load()); // Checks that the Frame has a variable "Bob" with the value Optional.of(55).
    }

}
