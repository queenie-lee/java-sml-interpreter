package sml;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: write JavaDoc for the class

public class SymbolTable<K, V> {
    private final Map<K, V> map;

    private SymbolTable(Map<K, V> map) {
        this.map = Map.copyOf(map);
    }

    /**
     * Constructs a new symbol table from a given map.
     * Note tha the iteration order of entries in the map is not preserved.
     *
     * @param map a map containing entries
     * @return a new symbol table containing the entries of the map
     * @param <K> the type of keys in the symbol table
     * @param <V> the type of value in the symbol table
     */

    public static <K, V> SymbolTable<K, V> of(Map<K, V> map) {
        return new SymbolTable<>(map);
    }

    /**
     * Returns the optional value associated with the key.
     *
     * @param key key
     * @return the optional value associated with the key (empty if none)
     */

    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    /**
     * Returns a string representation of the symbol table in the form
     * "[key1 ->  value1, key2 -> value2, ..., keyn -> valuen]"
     *
     * @return a string representation of the symbol table
     */
    @Override
    public String toString() {
        return map.entrySet()
                .stream()
                .map(m -> m.getKey().toString() + " -> " + m.getValue().toString())
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
