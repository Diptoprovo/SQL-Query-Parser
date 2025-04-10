import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class LL1FirstFollow {

    static Map<String, List<List<String>>> grammar = new HashMap<>();
    static Map<String, Set<String>> first = new HashMap<>();
    static Map<String, Set<String>> follow = new HashMap<>();
    static String startSymbol = "sql_statement";

    public static void main(String[] args) throws IOException {
        GrammarLoader.readGrammarFromFile("src/LL1_Grammar.txt", grammar);
        computeFirstSets();
        computeFollowSets();
        printSets();
        GrammarLoader.writeSetToFile(first, "src/first_set.txt");
        GrammarLoader.writeSetToFile(follow, "src/follow_set.txt");
    }

    static void computeFirstSets() {
        for (String nonTerminal : grammar.keySet()) {
            first.put(nonTerminal, new HashSet<>());
        }

        boolean changed;
        do {
            changed = false;
            for (var entry : grammar.entrySet()) {
                String lhs = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    for (int i = 0; i < production.size(); i++) {
                        String symbol = production.get(i);
                        Set<String> firstSet = getFirst(symbol);

                        if (firstSet.contains("ε")) {
                            changed |= first.get(lhs).addAll(removeEpsilon(firstSet));
                            if (i == production.size() - 1) {
                                changed |= first.get(lhs).add("ε");
                            }
                        } else {
                            changed |= first.get(lhs).addAll(firstSet);
                            break;
                        }
                    }
                }
            }
        } while (changed);
    }

    static void computeFollowSets() {
        for (String nonTerminal : grammar.keySet()) {
            follow.put(nonTerminal, new HashSet<>());
        }
        follow.get(startSymbol).add("$");

        boolean changed;
        do {
            changed = false;
            for (var entry : grammar.entrySet()) {
                String lhs = entry.getKey();
                for (List<String> production : entry.getValue()) {
                    for (int i = 0; i < production.size(); i++) {
                        String B = production.get(i);
                        if (!grammar.containsKey(B)) continue;

                        Set<String> trailer = new HashSet<>();
                        for (int j = i + 1; j < production.size(); j++) {
                            String beta = production.get(j);
                            Set<String> firstBeta = getFirst(beta);
                            trailer.addAll(removeEpsilon(firstBeta));
                            if (firstBeta.contains("ε")) continue;
                            else break;
                        }

                        if (trailer.isEmpty() || allNullable(production.subList(i + 1, production.size()))) {
                            trailer.addAll(follow.get(lhs));
                        }

                        changed |= follow.get(B).addAll(trailer);
                    }
                }
            }
        } while (changed);
    }

    static Set<String> getFirst(String symbol) {
        if (!grammar.containsKey(symbol)) {
            return Set.of(symbol); // terminal
        }
        return first.get(symbol);
    }

    static Set<String> removeEpsilon(Set<String> set) {
        Set<String> result = new HashSet<>(set);
        result.remove("ε");
        return result;
    }

    static boolean allNullable(List<String> symbols) {
        for (String sym : symbols) {
            if (!getFirst(sym).contains("ε")) return false;
        }
        return true;
    }

    static void printSets() {
        System.out.println("FIRST sets:");
        for (var entry : first.entrySet()) {
            System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
        }

        System.out.println("\nFOLLOW sets:");
        for (var entry : follow.entrySet()) {
            System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
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

}
