import java.io.*;
import java.util.*;

public class GrammarLoader {
    static void readGrammarFromFile(String filename, Map<String, List<List<String>>> grammar ) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("->");
                String lhs = parts[0].trim();
                String[] alternatives = parts[1].trim().split("\\|");

                List<List<String>> productions = new ArrayList<>();
                for (String alt : alternatives) {
                    productions.add(Arrays.asList(alt.trim().split("\\s+")));
                }
                grammar.put(lhs, productions);
            }
        }
    }
    static void readSetFromFile(String filename, Map<String, Set<String>> targetMap) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(":");
                String nonTerminal = parts[0].trim();
                Set<String> set = new HashSet<>(Arrays.asList(parts[1].trim().split("\\s*,\\s*")));
                targetMap.put(nonTerminal, set);
            }
        }
    }
    static void writeSetToFile(Map<String, Set<String>> setMap, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (var entry : setMap.entrySet()) {
                String key = entry.getKey();
                String value = String.join(", ", entry.getValue());
                writer.printf("%s: %s%n", key, value);
            }
            System.out.println("Written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
        }
    }

    static void writeParsingTableToFile(String filename, Map<String, Map<String, List<String>>> parsingTable) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String nonTerminal : parsingTable.keySet()) {
                for (Map.Entry<String, List<String>> entry : parsingTable.get(nonTerminal).entrySet()) {
                    String production = String.join(" ", entry.getValue());
                    writer.printf("%s -> %s : %s%n", nonTerminal, entry.getKey(), production);
                }
            }
            System.out.println("Parsing table saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing parsing table to file: " + e.getMessage());
        }
    }

    static Map<String, Map<TokenType, List<String>>> loadParsingTable(String filename, Map<String, Map<TokenType, List<String>>> table) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                String nonTerminal = parts[0].trim();
                String[] tokenAndProduction = parts[1].split(":");
                TokenType token = TokenType.valueOf(tokenAndProduction[0].trim());

                List<String> production = new ArrayList<>();
                if (tokenAndProduction.length > 1) {
                    String[] symbols = tokenAndProduction[1].trim().split("\\s+");
                    if (!(symbols.length == 1 && symbols[0].equals("ε"))) {
                        production.addAll(Arrays.asList(symbols));
                    }
                    // else: epsilon case — leave production as empty list
                }

                table.computeIfAbsent(nonTerminal, k -> new HashMap<>()).put(token, production);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }



}
