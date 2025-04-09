import java.io.IOException;
import java.util.*;

public class LL1ParserTableGenerator {
    // Grammar: Non-terminal -> List of productions (each a list of symbols)
    static Map<String, List<List<String>>> grammar = new HashMap<>();

    // FIRST and FOLLOW sets
    static Map<String, Set<String>> first = new HashMap<>();
    static Map<String, Set<String>> follow = new HashMap<>();

    // Parsing table
    static Map<String, Map<String, List<String>>> parsingTable = new HashMap<>();

    public static void main(String[] args)throws IOException {
//        defineGrammar();
//        defineFirstSets();
//        defineFollowSets();
        GrammarLoader.readGrammarFromFile("src/LL1_Grammar.txt", grammar);
        GrammarLoader.readSetFromFile("src/first_set.txt", first);
        GrammarLoader.readSetFromFile("src/follow_set.txt", follow);
        generateParsingTable();
        printParsingTable();
        GrammarLoader.writeParsingTableToFile("src/parsing_table.txt", parsingTable);
    }

    static void generateParsingTable() {
        for (var entry : grammar.entrySet()) {
            String nonTerminal = entry.getKey();
            List<List<String>> productions = entry.getValue();

            for (List<String> production : productions) {
                Set<String> firstSet = computeFirstOfString(production);

                for (String terminal : firstSet) {
                    if (!terminal.equals("ε")) {
                        addToTable(nonTerminal, terminal, production);
                    }
                }

                if (firstSet.contains("ε")) {
                    Set<String> followSet = follow.get(nonTerminal);
                    for (String terminal : followSet) {
                        addToTable(nonTerminal, terminal, production);
                    }
                }
            }
        }
    }

    static Set<String> computeFirstOfString(List<String> symbols) {
        Set<String> result = new HashSet<>();
        for (String symbol : symbols) {
            Set<String> symbolFirst = first.getOrDefault(symbol, Set.of(symbol));
            result.addAll(symbolFirst);
            if (!symbolFirst.contains("ε")) {
                result.remove("ε");
                break;
            }
        }
        if (symbols.isEmpty() || symbols.stream().allMatch(s -> first.getOrDefault(s, Set.of(s)).contains("ε"))) {
            result.add("ε");
        }
        return result;
    }

    static void addToTable(String nonTerminal, String terminal, List<String> production) {
        parsingTable.putIfAbsent(nonTerminal, new HashMap<>());
        Map<String, List<String>> row = parsingTable.get(nonTerminal);
        if (row.containsKey(terminal)) {
            System.err.println("Conflict at [" + nonTerminal + ", " + terminal + "]");
        }
        row.put(terminal, production);
    }

    static void printParsingTable() {
        for (String nonTerminal : parsingTable.keySet()) {
            System.out.println("Non-Terminal: " + nonTerminal);
            for (Map.Entry<String, List<String>> entry : parsingTable.get(nonTerminal).entrySet()) {
                System.out.println("  Token: " + entry.getKey() + " -> " + entry.getValue());
            }
        }
    }
}
