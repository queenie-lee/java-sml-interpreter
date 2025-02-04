package sml.instruction;

import sml.*;

import java.util.Objects;
import java.util.Optional;

public class GotoInstruction extends Instruction {
    public static final String OP_CODE = "goto";

    private final Label branchLabel;

    public GotoInstruction(Label label, Label branchLabel) {
        super(label, OP_CODE);
        this.branchLabel = Objects.requireNonNull(branchLabel);
    }

    @Override
    public Optional<Frame> execute(Machine machine) {
        Frame frame = machine.frame();
        return Optional.of(frame.jumpTo(branchLabel));
    }

    @Override
    protected String getOperandsString() {
        return branchLabel.toString();
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
