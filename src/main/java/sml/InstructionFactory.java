package sml;

import java.util.List;

/**
 * <p>A factory method interface for creating the program's instructions</p>
 * Combined with Dependency Injection, this allows for the program instructions (i.e. different general form)
 * to be replaced without needing to modify the source code within RunSml.
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
