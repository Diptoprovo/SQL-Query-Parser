import java.util.*;

class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Stack<String> parseStack = new Stack<>();
    private static final Map<String, Map<TokenType, List<String>>> parsingTable = new HashMap<>();

    public Parser(Lexer lexer) {
        GrammarLoader.loadParsingTable("src/parsing_table.txt", parsingTable);
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
        parseStack.push("sql_statement");
    }

    public void parseSQLStatement() {
        while (!parseStack.isEmpty()) {
            String top = parseStack.pop();
            if (parsingTable.containsKey(top)) {
                List<String> production = parsingTable.get(top).getOrDefault(currentToken.type, null);
                if (production == null) {
                    System.out.println(top);
                    throw new RuntimeException("Syntax Error: Expected a " + top +" but found token " + currentToken);
                }
                List<String> mutableProduction = new ArrayList<>(production);
                Collections.reverse(mutableProduction);
                parseStack.addAll(mutableProduction);
            } else if (top.equals(currentToken.type.name())) {
                currentToken = lexer.getNextToken();
            } else {
                throw new RuntimeException("Syntax Error: Expected " + top + " but found " + currentToken.value);
            }
        }
        System.out.println("Parsing successful!");
    }
}
