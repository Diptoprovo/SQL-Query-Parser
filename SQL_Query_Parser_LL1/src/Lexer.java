

import java.util.ArrayList;
import java.util.List;

class Lexer {
    private final List<Token> tokens = new ArrayList<>();
    private int index = 0;

    public Lexer(String input) {
        tokenize(input);
        tokens.add(new Token(TokenType.END, "EOF")); // End marker
    }

    private void tokenize(String input) {
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
    
            // Skip whitespace
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
    
            // Handle symbols
            switch (c) {
                case '*': tokens.add(new Token(TokenType.STAR, "*")); i++; continue;
                case '(': tokens.add(new Token(TokenType.LPAREN, "(")); i++; continue;
                case ')': tokens.add(new Token(TokenType.RPAREN, ")")); i++; continue;
                case ',': tokens.add(new Token(TokenType.COMMA, ",")); i++; continue;
                case ';': tokens.add(new Token(TokenType.SEMICOLON, ";")); i++; continue;
                case '=': tokens.add(new Token(TokenType.EQUALS, "=")); i++; continue;
                case '>': tokens.add(new Token(TokenType.GREATER, ">")); i++; continue;
                case '<': tokens.add(new Token(TokenType.LESS, "<")); i++; continue;
            }
    
            // Handle string literals (single-quoted)
            if (c == '\'') {
                int start = i + 1;  // Skip opening quote
                i++;
                while (i < input.length() && input.charAt(i) != '\'') {
                    i++;
                }
    
                if (i >= input.length()) {
                    throw new RuntimeException("Syntax Error: Unterminated string literal");
                }
    
                String stringValue = input.substring(start, i); // Extract string content
                tokens.add(new Token(TokenType.STRING, stringValue));
                i++; // Skip closing quote
                continue;
            }
    
            // Handle identifiers and keywords
            // Handle identifiers and keywords
        if (Character.isLetter(c)) {
            int start = i;
            while (i < input.length() && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                i++;
            }
            String word = input.substring(start, i).toUpperCase();

            // Keywords
            switch (word) {
                case "SELECT": tokens.add(new Token(TokenType.SELECT, word)); continue;
                case "FROM": tokens.add(new Token(TokenType.FROM, word)); continue;
                case "WHERE": tokens.add(new Token(TokenType.WHERE, word)); continue;
                case "ORDER": tokens.add(new Token(TokenType.ORDER, word)); continue;
                case "BY": tokens.add(new Token(TokenType.BY, word)); continue;
                case "GROUP": tokens.add(new Token(TokenType.GROUP, word)); continue;
                case "ASC": tokens.add(new Token(TokenType.ASC, word)); continue;
                case "DESC": tokens.add(new Token(TokenType.DESC, word)); continue;
                case "INSERT": tokens.add(new Token(TokenType.INSERT, word)); continue;
                case "INTO": tokens.add(new Token(TokenType.INTO, word)); continue;
                case "VALUES": tokens.add(new Token(TokenType.VALUES, word)); continue;
                case "DELETE": tokens.add(new Token(TokenType.DELETE, word)); continue;
                case "AND": tokens.add(new Token(TokenType.AND, word)); continue;
                case "OR": tokens.add(new Token(TokenType.OR, word)); continue;
                case "NOT": tokens.add(new Token(TokenType.NOT, word)); continue;
                case "IS": tokens.add(new Token(TokenType.IS, word)); continue;
                case "BETWEEN": tokens.add(new Token(TokenType.BETWEEN, word)); continue;
                case "LIKE": tokens.add(new Token(TokenType.LIKE, word)); continue;
                case "NULL": tokens.add(new Token(TokenType.NULL, word)); continue;
                case "IN": tokens.add(new Token(TokenType.IN, word)); continue;
                default: tokens.add(new Token(TokenType.IDENTIFIER, word)); continue;
            }
        }

            
            // Handle numbers
            if (Character.isDigit(c)) {
                int start = i;
                while (i < input.length() && Character.isDigit(input.charAt(i))) {
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, input.substring(start, i)));
                continue;
            }
    
            // If we reach here, it's an unknown token
            tokens.add(new Token(TokenType.UNKNOWN, String.valueOf(c)));
            i++;
        }
        tokens.add(new Token(TokenType.END, "EOF")); // End marker
        System.out.println(tokens);
    }
    

    public Token getNextToken() {
        return index < tokens.size() ? tokens.get(index++) : new Token(TokenType.END, "EOF");
    }
}
