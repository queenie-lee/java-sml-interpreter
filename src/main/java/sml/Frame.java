package sml;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Part of the stack, created by a method invocation.</p>
 * A frame stores:
 * * values of method arguments and local variables
 * * operands of the instruction in the operand stack
 * * the value of the program counter
 */
public class Frame {
    private final Method method;
    private int programCounter; // mutable

    /**
     * The value of each argument is mutable.
     * The mutator methods are arguments and variable
     * */
    private final SymbolTable<Variable.Identifier, Variable> arguments;
    /**
     * The value of each variable is mutable
     * The mutator method are localVariables and variable
     * */
    private final SymbolTable<Variable.Identifier, Variable> localVariables;
    /**
     * The values in the stack are mutable
     * The mutator methods are pop and push
     * */
    private final Deque<Integer> stack;

    private final Frame invoker;

    public Frame(Method method, Frame invoker) {
        this.method = Objects.requireNonNull(method);
        this.programCounter = 0;

        this.arguments = SymbolTable.of(method.arguments().stream()
                .collect(Collectors.toMap(v -> v, v -> new Variable())));
        this.localVariables = SymbolTable.of(method.localVariables().stream()
                .collect(Collectors.toMap(v -> v, v -> new Variable())));
        this.stack = new ArrayDeque<>();

        this.invoker = invoker;
    }

    /**
     * Returns a frame for the next instruction in the method
     * of the current frame.
     *
     * @return the frame for the next instruction
     */
    public Frame advance() {
        return setProgramCounter(programCounter + 1);
    }

    /**
     * Returns a frame for the instruction with a given label
     * in the method of the current frame.
     *
     * @return the frame for the instruction with a given label
     */
    public Frame jumpTo(Label label) {
        Optional<Integer> pc = method.labels().get(label);
        if (pc.isEmpty())
            throw new LabelNotFoundException(label, method);

        return setProgramCounter(pc.get());
    }

    private Frame setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
        Objects.checkIndex(programCounter, method.instructions().size());
        return this;
    }

    public Method method() {
        return method;
    }

    public Instruction currentInstruction() {
        return method.instructions().get(programCounter);
    }

    public int programCounter() {
        return programCounter;
    }

    /**
     * Returns an optional frame that for the invoker method.
     * The invoker is empty only for the main method.
     *
     * @return the optional invoker program counter
     */
    public Optional<Frame> invoker() {
        return Optional.ofNullable(invoker);
    }

    public SymbolTable<Variable.Identifier, Variable> arguments() {
        return arguments;
    }

    public SymbolTable<Variable.Identifier, Variable> localVariables() {
        return localVariables;
    }

    public Variable variable(Variable.Identifier identifier) {
        return localVariables.get(identifier)
                .or(() -> arguments.get(identifier))
                .orElseThrow(() -> new VariableNotFoundException(identifier));
    }

    public int pop() throws BadProgramError {
        try {
            return stack.pop();
        } catch (NoSuchElementException ex) {
            throw new BadProgramError("Not enough values on the stack.");
        }
    }

    public void push(int value) {
        stack.push(value);
    }

    @Override
    public String toString() {
        return method.name() + ", l "
                + programCounter
                + Optional.ofNullable(invoker)
                .map(pc -> " (" + pc + ")")
                .orElse("");
    }
}
