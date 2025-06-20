package sml.instruction;

import sml.*;

import java.util.Optional;

public class PrintInstruction extends Instruction {

    public static final String OP_CODE = "print";

    public PrintInstruction(Label label) {
        super(label, OP_CODE);
    }

    @Override
    public Optional<Frame> execute(Machine machine) throws BadProgramError {
        Frame frame = machine.frame();
        int value = frame.pop();
        System.out.println(value);
        return Optional.of(frame.advance());
    }

    @Override
    protected String getOperandsString() {
        return "";
    }

}
