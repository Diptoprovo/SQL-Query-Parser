import java.util.*;
class Parser {
    private final Lexer lexer;
    private Token currentToken;
    private final Stack<String> parseStack = new Stack<>();
    private final SymbolTable symbolTable;
    private static final Map<String, Map<TokenType, List<String>>> parsingTable = new HashMap<>();
    public Parser(Lexer lexer, SymbolTable symbolTable) {
        GrammarLoader.loadParsingTable("src/parsing_table.txt", parsingTable);
        this.lexer = lexer;
        this.symbolTable = symbolTable;
        this.currentToken = lexer.getNextToken();
        parseStack.push("sql_statement");
    }

    public void parseSQLStatement() {
        while (!parseStack.isEmpty()) {
            String top = parseStack.pop();
            if (parsingTable.containsKey(top)) {
                List<String> production = parsingTable.get(top).getOrDefault(currentToken.type, null);
                if (production == null) {
                    throw new RuntimeException("Syntax Error: Expected a " + top +" but found token " + currentToken);
                }
                List<String> mutableProduction = new ArrayList<>(production);
                Collections.reverse(mutableProduction);
                parseStack.addAll(mutableProduction);
            } else if (top.equals(currentToken.type.name())) {
                handleSymbolTableLogic(currentToken);  // Track symbols
                currentToken = lexer.getNextToken();
            } else {
                throw new RuntimeException("Syntax Error: Expected " + top + " but found " + currentToken.value);
            }
        }
        System.out.println("Parsing successful!");
        symbolTable.print();
    }

    private boolean insideInsert = false;
    private boolean insideColumnList = false;
    private boolean insideDelete = false;
    private boolean expectTable = false;
    private boolean expectColumn = false;
    private boolean insideWhere = false;

    private void handleSymbolTableLogic(Token token) {
        switch (token.type) {
            case SELECT -> {
                expectColumn = true;
                insideInsert = false;
                insideDelete = false;
            }
            case INSERT -> {
                insideInsert = true;
                insideDelete = false;
            }
            case DELETE -> {
                insideDelete = true;
                insideInsert = false;
            }
            case INTO -> {
                if (insideInsert) {
                    expectTable = true;
                }
            }
            case FROM -> {
                if (insideDelete || insideInsert || insideColumnList || expectColumn) {
                    expectTable = true;
                }
            }
            case WHERE, ORDER, GROUP -> {
                expectColumn = true;
            }
            case VALUES -> {
                expectColumn = false;
                insideColumnList = false;
            }
            case LPAREN -> {
                if (insideInsert) {
                    insideColumnList = true;
                    expectColumn = true;
                }
            }
            case RPAREN -> {
                if (insideColumnList) {
                    insideColumnList = false;
                    expectColumn = false;
                }
            }
            case IDENTIFIER -> {
                if (expectTable) {
                    symbolTable.add(token.value, "TABLE");
                    expectTable = false;
                } else if (expectColumn || insideColumnList) {
                    symbolTable.add(token.value, "COLUMN");
                }
            }
            case SEMICOLON -> {
                // Reset state
                expectTable = false;
                expectColumn = false;
                insideInsert = false;
                insideColumnList = false;
                insideDelete = false;
            }
        }
    }
}
