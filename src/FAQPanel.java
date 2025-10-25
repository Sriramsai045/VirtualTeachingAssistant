import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FAQPanel extends JPanel {
    public FAQPanel() {
        setLayout(new GridLayout(4,1,10,10));
        setBackground(new Color(80,80,80));
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        JLabel header = new JLabel("FAQ’s", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setForeground(Color.WHITE);
        add(header);

        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT question, answer FROM FAQ LIMIT 3")
        ) {
            ResultSet rs = stmt.executeQuery();
            int i = 0;
            while(rs.next() && i < 3) {
                String q = rs.getString("question");
                String a = rs.getString("answer");
                JTextArea qArea = new JTextArea(q + "\n" + a);
                qArea.setFont(new Font("Dialog", Font.PLAIN, 18));
                qArea.setEditable(false);
                qArea.setBackground(new Color(140,140,140));
                qArea.setForeground(Color.WHITE);
                add(qArea);
                i++;
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }
}
