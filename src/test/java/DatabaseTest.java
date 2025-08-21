import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.*;

public class DatabaseTest {

    @Test
    public void testUserExists() throws Exception {
        // Підключення
        Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/testdb",
                "root",
                "m@zCg26ti4a"
        );

        // SQL-запит на пошук користувача
        String query = "SELECT * FROM users WHERE username = 'test_user'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        if (rs.next()) {
            String username = rs.getString("username");
            String email = rs.getString("email");
            System.out.println("Знайдено користувача: " + username + " (email: " + email + ")");
            Assert.assertTrue(true); // тест пройшов
        } else {
            System.out.println("Користувача test_user НЕ знайдено!");
            Assert.fail("Користувач test_user відсутній у таблиці!");
        }

        // Закриваємо ресурси
        rs.close();
        stmt.close();
        conn.close();
    }
}
