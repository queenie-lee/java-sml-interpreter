package sml;

import sml.instruction.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * This class ....
 * <p>
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 *
 * @author ...
 */
public final class Translator {

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

    private static final String ITEM_SEPARATOR = ",";
    private static final String METHOD_LABEL = "@";

    public Collection<Method> readAndTranslate(String fileName) throws IOException {

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
    private Instruction getInstruction(Label label) {
        String opcode = scan();
        if (opcode.isEmpty())
            return null;

        return switch (opcode) {
            case GotoInstruction.OP_CODE -> {
                String s = scan();
                yield new GotoInstruction(label, new Label(s));
            }
            case ReturnInstruction.OP_CODE -> new ReturnInstruction(label);
            case InvokeInstruction.OP_CODE -> {
                String s = scan();
                yield new InvokeInstruction(label, new Method.Identifier(s));
            }
            case PrintInstruction.OP_CODE -> new PrintInstruction(label);
            case AdditionInstruction.OP_CODE -> new AdditionInstruction(label);
            case SubtractionInstruction.OP_CODE -> new SubtractionInstruction(label);
            case MultiplicationInstruction.OP_CODE -> new MultiplicationInstruction(label);
            case DivisionInstruction.OP_CODE -> new DivisionInstruction(label);
            case CompareEqualInstruction.OP_CODE -> {
                String s = scan();
                yield new CompareEqualInstruction(label, new Label(s));
            }
            case CompareGreaterThanInstruction.OP_CODE -> {
                String s = scan();
                yield new CompareGreaterThanInstruction(label, new Label(s));
            }


            // TODO: add code for all other types of instructions

            // TODO: Then, replace the switch by using the Reflection API

            // TODO: Next, use dependency injection to allow this machine class
            //       to work with different sets of opcodes (different CPUs)

            default -> {
                yield  null;
            } //new IllegalArgumentException("Unknown instruction: " + opcode);
        };
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