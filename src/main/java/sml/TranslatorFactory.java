package sml;

import java.io.IOException;
import java.util.Collection;

public interface TranslatorFactory {
    Collection<Method> readAndTranslate(String fileName) throws IOException, BadProgramError;
}
