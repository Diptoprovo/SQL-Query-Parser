import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class SymbolTable {
    private final Map<String, String> symbols = new LinkedHashMap<>();

    public void add(String name, String type) {
        symbols.putIfAbsent(name, type);
    }

    public void print() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/symbol_table.txt"))) {
            writer.write("--- Symbol Table ---\n");
            for (Map.Entry<String, String> entry : symbols.entrySet()) {
                writer.write(String.format("%-15s : %-10s%n", entry.getKey(), entry.getValue()));
            }
            System.out.println("Symbol table written to symbol_table.txt");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void clear() {
        symbols.clear();
    }
}
