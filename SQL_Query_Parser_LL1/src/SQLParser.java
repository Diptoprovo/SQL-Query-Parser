import java.util.Scanner;

public class SQLParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            StringBuilder sqlBuilder = new StringBuilder();
            System.out.print("\nSQL> ");

            while (true) {
                String line = scanner.nextLine().trim();
                sqlBuilder.append(line).append(" ");
                if (line.endsWith(";")) {
                    break;
                }
                System.out.print("   > ");  // prompt for continuation
            }

            String sql = sqlBuilder.toString().replace("\n", "").trim();

            if (sql.equalsIgnoreCase("exit;"))
                break;

            try {
                Lexer lexer = new Lexer(sql);
                Parser parser = new Parser(lexer);
                parser.parseSQLStatement();
            } catch (RuntimeException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }
        }
        scanner.close();
    }
}
