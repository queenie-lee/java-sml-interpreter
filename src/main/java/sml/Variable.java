package sml;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a variable within an instruction
 *
 * @author Queenie Lee
 */
public class Variable {

    /**
     * The variable name is wrapped within the variable identifier record class, ensuring type safety.
     * This provides better code readability and ensures related errors can be caught at compile time.
     * @param name name of the variable
     */
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
