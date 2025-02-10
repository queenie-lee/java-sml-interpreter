package sml.instruction;

import sml.*;

import java.util.Optional;

public class ReturnInstruction extends Instruction {

    public static final String OP_CODE = "return";

    public ReturnInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    public Optional<Frame> execute(Machine machine) throws BadProgramError {
        Frame frame = machine.frame();
        int value = frame.pop();
        Optional<Frame> optionalInvoker = frame.invoker();
        if (optionalInvoker.isPresent()) {
            Frame invoker = optionalInvoker.get();
            invoker.push(value);
            return Optional.of(invoker.advance());
        }
        return optionalInvoker;
    }

    @Override
    protected String getOperandsString() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
