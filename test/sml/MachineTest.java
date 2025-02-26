package sml;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sml.instruction.LoadInstruction;
import sml.instruction.PrintInstruction;
import sml.instruction.PushInstruction;
import sml.instruction.StoreInstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MachineTest {
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
    void toStringTest() throws BadProgramError {
        Instruction ins0 = new PushInstruction(null, 70);
        Instruction ins1 = new StoreInstruction(new Label("L9"), "m");
        Instruction ins2 = new PrintInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(new Variable.Identifier("m"), new Variable.Identifier("n")),
                List.of(ins0, ins1, ins2));
        machine.setProgram(List.of(m));

        ins0.execute(machine);

        assertEquals("Machine: Program: [main -> Method main (m, n):  push 70, L9: store m, ...], Frame: Optional[main, l 1]", machine.toString());
    }
}
