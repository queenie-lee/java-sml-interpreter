package sml;

import org.junit.jupiter.api.Test;
import sml.instruction.LoadInstruction;
import sml.instruction.PrintInstruction;
import sml.instruction.PushInstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTest {
    @Test
    void toStringTest() {
        Instruction ins0 = new LoadInstruction(null, "m");
        Instruction ins1 = new PushInstruction(new Label("L9"), 70);
        Instruction ins2 = new PrintInstruction(null);

        Method m = new Method(new Method.Identifier("@test"),
                List.of(new Variable.Identifier("m"), new Variable.Identifier("n")),
                List.of(ins0, ins1, ins2));
        assertEquals(m.toString(), "Method test (m, n):  load m, L9: push 70, ...");
    }
}
