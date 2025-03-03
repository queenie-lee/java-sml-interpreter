package sml;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class ....
 * <p>
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 *
 * @author Queenie Lee
 */
@Component("translator")
public final class SMLTranslator implements TranslatorFactory {

    // line contains the characters in the current line that's not been processed yet
    private String line = "";

    private static class State {
        final Method.Identifier methodName;
        final List<Instruction> instructions;
        final List<Variable.Identifier> arguments;

        State(Method.Identifier methodName) {
            this.methodName = methodName;
            instructions = new ArrayList<>();
            arguments = new ArrayList<>();
        }

        Method createMethod() {
            return new Method(methodName, arguments, instructions);
        }

        void addArgument(String name) {
            Variable.Identifier id = new Variable.Identifier(name);
            arguments.add(id);
        }
    }

    @Autowired
    private InstructionFactory instructionFactory = null;

    private static final String ITEM_SEPARATOR = ",";
    private static final String METHOD_LABEL = "@";

    public Collection<Method> readAndTranslate(String fileName) throws IOException, BadProgramError {

        Collection<Method> methods = new ArrayList<>();

        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            // each iteration processes the contents of line
            // and reads the next input line into "line"
            State state = null;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String labelString = getLabel();
                if (labelString != null && labelString.startsWith(METHOD_LABEL)) {
                    if (state != null)
                        methods.add(state.createMethod());

                    state = new State(new Method.Identifier(labelString));
                    for (String s = scan(); !s.isEmpty(); s = scan()) {

                        String variable = s.endsWith(ITEM_SEPARATOR)
                                ? s.substring(0, s.length() - 1).trim()
                                : s;

                        state.addArgument(variable);

                        if (!s.endsWith(ITEM_SEPARATOR))
                            break;
                    }
                }
                else {
                    Label label = labelString != null
                            ? new Label(labelString)
                            : null;

                    Instruction instruction = getInstruction(label);
                    if (instruction != null) {
                        if (state != null)
                            state.instructions.add(instruction);
                        else
                            throw new IllegalArgumentException("Instructions cannot appear outside methods " + labelString + " " + instruction);
                    }
                }
            }
            if (state != null)
                methods.add(state.createMethod());
        }
        return methods;
    }

    /**
     * Translates the current line into an instruction with the given label
     * <p>
     * The input line should consist of a single SML instruction,
     * with its label already removed.
     *
     * @param label the instruction label
     * @return the new instruction
     */
    private Instruction getInstruction(Label label) throws BadProgramError {
        List<String> instruction = new ArrayList<>();
        while (!line.isEmpty()) {
            String word = scan();
            instruction.add(word);
        }
        return instructionFactory.createInstruction(label, instruction);
    }

    private String getLabel() {
        String word = scan();
        if (word.endsWith(":"))
            return word.substring(0, word.length() - 1);

        // undo scanning the word
        line = word + " " + line;
        return null;
    }

    /**
     * Returns the first word of line and remove it from line.
     * If there is no word, return "".
     */
    private String scan() {
        line = line.trim();

        int whiteSpacePosition = 0;
        while (whiteSpacePosition < line.length()) {
            if (Character.isWhitespace(line.charAt(whiteSpacePosition)))
                break;
            whiteSpacePosition++;
        }

        String word = line.substring(0, whiteSpacePosition);
        line = line.substring(whiteSpacePosition);
        return word;
    }
}