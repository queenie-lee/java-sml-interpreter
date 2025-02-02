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
            // TODO: Add exception handling for missing labels, etc.
            //       Produce user-friendly error messages.
            //       You may need to extend the functionality of the exception classes.
            frame = instruction.execute(this);
        }
    }

    public void setProgram(Collection<Method> methods) {
        program = SymbolTable.of(methods.stream()
                .collect(Collectors.toMap(Method::name, m -> m)));
        frame = Optional.empty();
        frame = newFrameForMethodInvocation(new Method.Identifier("@main"));
    }


    public Frame frame() {
        return frame.get();
    }

    public Optional<Frame> newFrameForMethodInvocation(Method.Identifier methodName) {
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
            // TODO: explain where exactly do local variables get their default value (0)
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
        // TODO: implement
        return "";
    }

}
