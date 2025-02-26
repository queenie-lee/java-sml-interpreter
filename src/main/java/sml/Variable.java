package sml;

import java.util.Objects;
import java.util.Optional;

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

    /**
     * Value is an Optional
     * If there is no value stored within it, it should not return 0.
     */
    private Optional<Integer> value = Optional.empty();

    public void store(int value) {
        this.value = Optional.of(value);
    }

    public Optional<Integer> load() {
        return value;
    }
}
