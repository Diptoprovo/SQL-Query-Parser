import java.util.Scanner;

public class SQLParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("SQL>");
            String sql = scanner.nextLine();
            if(sql.equals("exit"))
                break;
            Lexer lexer = new Lexer(sql);
            Parser parser = new Parser(lexer);

            try {
                parser.parseSQLStatement();
            } catch (RuntimeException e) {
                System.out.println("\u001B[31m" + e.getMessage() + "\u001B[0m");
            }

        }
        scanner.close();
    }
}