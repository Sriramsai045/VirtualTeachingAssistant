import java.sql.Connection;
import java.sql.DriverManager;

public class TestDBConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/VirtualTeachingAssistant";
        String user = "Sriam";
        String password = "Aravinda@23";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
