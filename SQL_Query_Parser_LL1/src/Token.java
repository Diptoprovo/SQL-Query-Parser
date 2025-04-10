class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    @Override
    public String toString() {
        return String.valueOf(this.type) +": " + this.value;
    }
}