import java.sql.*;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/VirtualTeachingAssistant";
    private static final String USER = "Sriam"; // change if needed
    private static final String PASSWORD = "Aravinda@23"; // change if needed

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
