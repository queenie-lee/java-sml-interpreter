package sml;

import java.util.List;

/**
 * An interface for creating the program's instructions
 * @author Queenie Lee
 */
public interface InstructionFactory {
    /**
     * Returns a program instruction
     * @param label optional label (can be null)
     * @param programInstruction list containing data from a single instruction
     * @return a program instruction
     * @throws BadProgramError should not occur these should have been checked prior to calling this method
     */
    Instruction createInstruction(Label label, List<String> programInstruction) throws BadProgramError;

}
