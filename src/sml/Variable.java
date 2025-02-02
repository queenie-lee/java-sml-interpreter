package sml;

import java.util.Objects;

// TODO: Write JavaDoc for the class

public class Variable {

    public record Identifier(String name) {
        public Identifier {
            Objects.requireNonNull(name);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private int value;

    public void store(int value) {
        this.value = value;
    }

    public int load() {
        return value;
    }
}
