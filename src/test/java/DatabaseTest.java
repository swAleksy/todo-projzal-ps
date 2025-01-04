import ps.database.Database;

import java.sql.Connection;

public class DatabaseTest {
    public static void main(String[] args) {
        try (Connection connection = Database.getConnection()) {
            System.out.println("Połączenie z bazą danych nawiązane!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Nie udało się połączyć z bazą danych.");
        }
    }
}