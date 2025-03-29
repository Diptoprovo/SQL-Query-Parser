import java.util.Scanner;

public class SQLParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter SQL statement:");
        String sql = scanner.nextLine();
        scanner.close();

        Lexer lexer = new Lexer(sql);
        Parser parser = new Parser(lexer);

        try {
            parser.parseSQLStatement();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}

// SELECT A,V,D,E FROM B WHERE A>10 AND B>18 OR C=10 GROUP BY S ORDER BY G;
// DELETE FROM BHAI WHERE BRAIN = 10;
// INSERT INTO table_name VALUES (value1, value2, value3);
