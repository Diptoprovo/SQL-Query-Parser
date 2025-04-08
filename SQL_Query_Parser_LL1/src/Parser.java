

import java.util.*;

class Parser {
    private Lexer lexer;
    private Token currentToken;
    private Stack<String> parseStack = new Stack<>();
    private static final Map<String, Map<TokenType, List<String>>> parsingTable = new HashMap<>();

//    static {
//        parsingTable.put("sql_statement", Map.of(
//                TokenType.SELECT, List.of("select_statement"),
//                TokenType.INSERT, List.of("insert_statement"),
//                TokenType.DELETE, List.of("delete_statement")
//        ));
//
//        parsingTable.put("select_statement", Map.of(
//                TokenType.SELECT, List.of("SELECT", "column_list", "FROM", "IDENTIFIER", "where_clause_opt", "group_by_clause_opt", "order_by_clause_opt", "SEMICOLON")
//        ));
//
//        parsingTable.put("insert_statement", Map.of(
//                TokenType.INSERT, List.of("INSERT", "INTO", "IDENTIFIER", "column_list_opt", "insert_tail")
//        ));
//        parsingTable.put("insert_tail", Map.of(
//                TokenType.VALUES, List.of("VALUES", "LPAREN", "value_list","RPAREN", "SEMICOLON")
//        ));
//        parsingTable.put("column_list_opt", Map.of(
//                TokenType.LPAREN, List.of("LPAREN", "column_list_opt_ext", "RPAREN"),
//                TokenType.VALUES, List.of()
//        ));
//        parsingTable.put("column_list_opt_ext", Map.of(
//                TokenType.IDENTIFIER, List.of("IDENTIFIER", "column_list_opt_tail")
//        ));
//        parsingTable.put("column_list_opt_tail", Map.of(
//                TokenType.COMMA, List.of("COMMA", "IDENTIFIER", "column_list_opt_tail"),
//                TokenType.RPAREN, List.of()
//        ));
//
//
//
//        parsingTable.put("delete_statement", Map.of(
//                TokenType.DELETE, List.of("DELETE", "FROM", "IDENTIFIER", "where_clause_opt", "SEMICOLON")
//        ));
//
//        parsingTable.put("column_list", Map.of(
//                TokenType.IDENTIFIER, List.of("IDENTIFIER", "column_list_tail"),
//                TokenType.STAR, List.of("STAR")
//        ));
//
//        parsingTable.put("column_list_tail", Map.of(
//                TokenType.COMMA, List.of("COMMA", "IDENTIFIER", "column_list_tail"),
//                TokenType.FROM, List.of()
//        ));
//
//        parsingTable.put("where_clause_opt", Map.of(
//                TokenType.WHERE, List.of("WHERE", "condition"),
//                TokenType.GROUP, List.of(),
//                TokenType.ORDER, List.of(),
//                TokenType.SEMICOLON, List.of()
//        ));
//
//        parsingTable.put("order_by_clause_opt", Map.of(
//                TokenType.ORDER, List.of("ORDER", "BY", "IDENTIFIER", "order_direction_opt"),
//                TokenType.SEMICOLON, List.of()
//        ));
//
//        parsingTable.put("order_direction_opt", Map.of(
//                TokenType.ASC, List.of("ASC"),
//                TokenType.DESC, List.of("DESC"),
//                TokenType.SEMICOLON, List.of()
//        ));
//
//        parsingTable.put("group_by_clause_opt", Map.of(
//                TokenType.GROUP, List.of("GROUP", "BY", "IDENTIFIER"),
//                TokenType.ORDER, List.of(),
//                TokenType.SEMICOLON, List.of()
//        ));
//
//        parsingTable.put("condition", Map.of(
//                TokenType.IDENTIFIER, List.of("IDENTIFIER", "operator_or_is", "condition_tail")
//        ));
//
//        parsingTable.put("operator_or_is", Map.of(
//                TokenType.EQUALS, List.of("EQUALS", "value"),
//                TokenType.GREATER, List.of("GREATER", "value"),
//                TokenType.LESS, List.of("LESS", "value"),
//                TokenType.IS, List.of("IS", "is_null_opt")  // IS NULL / IS NOT NULL support
//        ));
//
//        parsingTable.put("is_null_opt", Map.of(
//                TokenType.NULL, List.of("NULL"),  // IS NULL
//                TokenType.NOT, List.of("NOT", "NULL")  // IS NOT NULL
//        ));
//
//        parsingTable.put("condition_tail", Map.of(
//                TokenType.AND, List.of("AND", "condition"),
//                TokenType.OR, List.of("OR", "condition"),
//                TokenType.SEMICOLON, List.of(), // Empty production
//                TokenType.ORDER, List.of(), // Optional
//                TokenType.GROUP, List.of()  // Optional
//        ));
//
//        parsingTable.put("operator", Map.of(
//                TokenType.EQUALS, List.of("EQUALS"),
//                TokenType.GREATER, List.of("GREATER"),
//                TokenType.LESS, List.of("LESS")
//        ));
//
//        parsingTable.put("value", Map.of(
//                TokenType.NUMBER, List.of("NUMBER"),
//                TokenType.STRING, List.of("STRING")
//        ));
//        parsingTable.put("value_list", Map.of(
//                TokenType.NUMBER, List.of("value", "value_list_tail"),
//                TokenType.STRING, List.of("value", "value_list_tail")
//        ));
//
//        parsingTable.put("value_list_tail", Map.of(
//                TokenType.COMMA, List.of("COMMA", "value", "value_list_tail"),
//                TokenType.RPAREN, List.of() // Empty production
//        ));
//    }

    public Parser(Lexer lexer) {
        GrammarLoader.loadParsingTable("src/parsing_table.txt", parsingTable);
        this.lexer = lexer;
        this.currentToken = lexer.getNextToken();
        parseStack.push("sql_statement");
    }

    public void parseSQLStatement() {

        System.out.println(parsingTable);
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


//        parsingTable.put("insert_statement", Map.of(
//                TokenType.INSERT, List.of("INSERT", "INTO", "IDENTIFIER", "VALUES", "LPAREN", "value_list", "RPAREN", "SEMICOLON")
//        ));

//                TokenType.DELETE, List.of("DELETE", "FROM", "IDENTIFIER", "WHERE", "IDENTIFIER", "EQUALS", "NUMBER", "SEMICOLON")
