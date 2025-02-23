package sml;

import java.util.List;

public interface InstructionFactory {
    Instruction createInstruction(Label label, List<String> programInstruction) throws BadProgramError;

}
