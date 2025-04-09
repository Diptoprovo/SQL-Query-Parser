import java.util.Scanner;

public class SQLParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SymbolTable symbolTable = new SymbolTable();
        while (true) {
            StringBuilder sqlBuilder = new StringBuilder();
            System.out.print("\nSQL> ");

            while (true) {
                String line = scanner.nextLine().trim();
                sqlBuilder.append(line).append(" ");
                if (line.endsWith(";")) {
                    break;
                }
                System.out.print("   > ");
            }

            String sql = sqlBuilder.toString().replace("\n", "").trim();

            if (sql.equalsIgnoreCase("exit;"))
                break;

            try {
                symbolTable.clear();
                Lexer lexer = new Lexer(sql);
                Parser parser = new Parser(lexer, symbolTable);
                parser.parseSQLStatement();
            } catch (RuntimeException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }
        }
        scanner.close();
    }
}
