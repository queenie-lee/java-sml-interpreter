package sml;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SymbolTableTest {

    @Test
    void toStringTest() {
        Map<Integer,Integer> testMap = Map.of(
                1, 100,
                2, 10
        );
        var st = SymbolTable.of(testMap);
        assertTrue(st.toString().equals("[1 -> 100, 2 -> 10]") ||
                st.toString().equals("[2 -> 10, 1 -> 100]"));
    }
}
