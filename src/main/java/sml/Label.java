package sml;

import java.util.Objects;

/**
 * Used as an indicator for other instructions to "jump" to a particular label.
 *
 * The label name is wrapped within the Label record class, ensuring type safety.
 * This provides better code readability and ensures related errors can be caught at compile time.
 * @param label label, a sequence of non-whitespace characters
 */
public record Label(String label) {

    public Label {
        Objects.requireNonNull(label);
    }

    @Override
    public String toString() {
        return label;
    }

    // TODO: Do we need to override .equals and .hashCode here?
    //       These two methods would be needed for the Instruction subclasses.
}
