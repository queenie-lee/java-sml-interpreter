package sml.instruction;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import sml.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoadInstructionTest {

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
    void loadInstructionSuccessful() throws BadProgramError {
        Variable.Identifier variable = new Variable.Identifier("Larry");
        Instruction ins0 = new LoadInstruction(null, "Larry");
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));
        machine.frame().variable(variable).store(77);

        ins0.execute(machine);
        int result = machine.frame().pop();

        assertEquals(77, result);
    }

    @Test
    void loadingVariableWithoutValue() throws BadProgramError {
        Variable.Identifier variable = new Variable.Identifier("Pete");

        Instruction ins0 = new LoadInstruction(null, variable.name());
        Instruction ins1 = new ReturnInstruction(null);

        Method m = new Method(new Method.Identifier("@main"),
                List.of(), List.of(ins0, ins1));
        machine.setProgram(List.of(m));
        BadProgramError ex = assertThrows(BadProgramError.class, () -> ins0.execute(machine));

        assertEquals("The variable " + variable.name() + " does not contain a value.", ex.getMessage());
    }

}
