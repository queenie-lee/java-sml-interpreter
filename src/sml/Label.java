package sml;

import java.util.Objects;

// TODO: What are the benefits of using this record class?
//       Compare this approach with using class String instead of
//       Variable.Identifier, Method.Identifier and Label

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
