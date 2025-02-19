package sml;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// TODO: Write JavaDoc for the class

public class Method {

    public record Identifier(String name) {

        public Identifier {
            if (name.charAt(0) != '@')
                throw new IllegalArgumentException("Method identifier name must start with @");
            name = name.substring(1);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Identifier name;
    private final List<Variable.Identifier> arguments;
    private final Set<Variable.Identifier> localVariables;
    private final List<Instruction> instructions;
    private final SymbolTable<Label, Integer> labels;

    public Method(Identifier name, List<Variable.Identifier> arguments, List<Instruction> instructions) {
        this.name = name;
        this.arguments = List.copyOf(arguments);
        this.instructions = List.copyOf(instructions);

        Map<Variable.Identifier, Long> argumentOccurrences = this.arguments.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));

        if (argumentOccurrences.entrySet().stream().anyMatch(e -> e.getValue() > 1))
            throw new IllegalArgumentException("Duplicate arguments: " +
                    argumentOccurrences.entrySet().stream()
                            .filter(e -> e.getValue() > 1)
                            .map(Map.Entry::getKey)
                            .toList());

        this.localVariables = this.instructions.stream()
                .flatMap(Instruction::variables)
                .filter(v -> !argumentOccurrences.containsKey(v))
                .collect(Collectors.toSet());

        // must contain at least one instruction (at least a return instruction)
        if (this.instructions.isEmpty())
            throw new IllegalArgumentException("No instructions found");

        this.labels = SymbolTable.of(IntStream.range(0, this.instructions.size())
                .mapToObj(idx -> this.instructions
                        .get(idx)
                        .optionalLabel()
                        .stream()
                        .map(label -> Map.entry(label, idx)))
                .flatMap(s -> s)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public Identifier name() {
        return name;
    }

    public List<Instruction> instructions() {
        return instructions;
    }

    public SymbolTable<Label, Integer> labels() {
        return labels;
    }

    public List<Variable.Identifier> arguments() {
        return arguments;
    }

    public Set<Variable.Identifier> localVariables() {
        return localVariables;
    }

    /**
     * Returns a string representation of the method.
     *      * It consists of
     *      *    - the optional label followed by a colon,
     *      *    - the list of arguments in parentheses followed by a colon,
     *      *    - the list of instructions - maximum defined by INST_LIMIT.
     *      * Format: Method {name} (arg1, arg2, arg3): inst1, inst2, ...
     *
     * @return a string representation of the method
     */
    @Override
    public String toString() {
        final int INST_LIMIT = 2;
        String nameString = name.toString();
        String argsString = arguments.stream()
                .map(Variable.Identifier::toString)
                .collect(Collectors.joining(", ", "(", ")"));
        String instructionsString = instructions.stream()
                .map(Instruction::toString)
                .limit(INST_LIMIT)
                .collect(Collectors.joining(", "));
        if (instructions.size() > INST_LIMIT) {
            instructionsString += ", ...";
        }
        return "Method " + nameString + " " + argsString + ": " + instructionsString;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (object instanceof Method method) {
            return Objects.equals(name, method.name) && Objects.equals(arguments, method.arguments) && Objects.equals(instructions, method.instructions);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, arguments, instructions);
    }

}
