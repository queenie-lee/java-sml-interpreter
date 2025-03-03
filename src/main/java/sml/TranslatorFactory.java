package sml;

import java.io.IOException;
import java.util.Collection;

/**
 * An interface for the program's translator
 * @author Queenie Lee
 */
public interface TranslatorFactory {
    /**
     * Returns a collection of methods used in the program
     * @param fileName name of the file containing the program text
     * @return a collection of methods used in the program
     * @throws IOException if file does not exist
     * @throws BadProgramError if there is an issue within the file program text
     */
    Collection<Method> readAndTranslate(String fileName) throws IOException, BadProgramError;
}
