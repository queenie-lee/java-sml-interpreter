package sml;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the machine, the context in which programs run.
 * <p>
 * An instance contains the program (as a table with methods) and
 * a program counter. The program counter represents the current
 * instruction and the call stack in the program execution.
 */
public final class Machine {

    private SymbolTable<Method.Identifier, Method> program;

    /**
     * Contains the current method name (with list of instructions), its arguments and local variables,
     * the operand stack and the program counter (the index of the instruction to be executed next)
     */
    // The current frame contains the current method name (with a list of instructions),
    // its arguments and local variables, the operand stack and
    // the program counter (the index of the instruction to be executed next)
    private Optional<Frame> frame;

    /**
     * Execute the program starting from method "main".
     * Precondition: the program has been stored properly.
     */
    public void execute() {
        while (frame.isPresent()) {
            Frame f = frame.get();
            Instruction instruction = f.currentInstruction();
            System.out.println("[" + f + "] " + instruction);
            try {
                frame = instruction.execute(this);
            } catch (BadProgramError ex) {
                System.out.println("There is a problem with your program.");
                ex.printMessage();
            }
        }
    }

    public void setProgram(Collection<Method> methods) {
        program = SymbolTable.of(methods.stream()
                .collect(Collectors.toMap(Method::name, m -> m)));
        frame = Optional.empty();
        try {
            frame = newFrameForMethodInvocation(new Method.Identifier("@main"));
        } catch (BadProgramError ex) {
            throw new AssertionError("Logic error. This should never happen while creating Frame for the Main method. " + ex);
        }
    }


    public Frame frame() {
        return frame.orElse(null);
    }

    public Optional<Frame> newFrameForMethodInvocation(Method.Identifier methodName) throws BadProgramError {
        Method method = program.get(methodName)
                .orElseThrow(() -> new MethodNotFoundException(methodName));

        Frame newFrame = new Frame(method, frame.orElse(null));

        if (frame.isPresent()) {
            Frame currentFrame = frame.get();
            // the order of arguments is important
            for (Variable.Identifier var : newFrame.method().arguments()) {
                int value = currentFrame.pop();
                Variable variable = newFrame.arguments().get(var)
                        .orElseThrow(() -> new AssertionError("Variable " + var + " not found (can never happen)"));
                variable.store(value);
            }
            // no need to initialise local variables as it is already done
            // in the constructor of Frame
            // Local variables do not have a default value of 0. The default value is empty Optional.
        }
        return Optional.of(newFrame);
    }


    /**
     * String representation of the program under execution.
     *
     * @return pretty formatted version of the code.
     */
    @Override
    public String toString() {
        return "Machine: " +
                "Program: " + program.toString() +
                ", Frame: " + frame.toString();
    }

}
