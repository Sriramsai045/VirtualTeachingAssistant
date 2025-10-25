import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ChatPanel extends JPanel {
    private JTextArea chatArea;
    private JTextField inputField;
    private String username;

    public ChatPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(60,60,60));
        setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setBackground(new Color(100,100,100));
        chatArea.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout(10, 10));
        inputField = new JTextField("type here...");
        inputField.setForeground(Color.LIGHT_GRAY);
        inputField.setBackground(new Color(80,80,80));
        inputField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(inputField.getText().equals("type here...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if(inputField.getText().trim().isEmpty()) {
                    inputField.setText("type here...");
                    inputField.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        southPanel.add(inputField, BorderLayout.CENTER);
        JButton askBtn = new JButton("Ask↑");
        askBtn.setBackground(Color.LIGHT_GRAY);
        askBtn.setForeground(Color.BLACK);
        askBtn.setFont(new Font("Dialog", Font.BOLD, 15));
        askBtn.addActionListener(e -> handleSend());
        southPanel.add(askBtn, BorderLayout.EAST);

        add(southPanel, BorderLayout.SOUTH);
    }

    private void handleSend() {
        String msg = inputField.getText().trim();
        if(msg.isEmpty() || msg.equals("type here...")) return;
        chatArea.append(username + ": " + msg + "\n");
        inputField.setText("");

        // Lookup FAQ answer
        String answer = getFAQAnswer(msg);
        if(answer != null) {
            chatArea.append("Assistant: " + answer + "\n");
            saveMessageToDB(msg, answer);
        } else {
            chatArea.append("Assistant: Sorry, I don't know the answer.\n");
            saveMessageToDB(msg, "No answer available.");
        }
    }

    private String getFAQAnswer(String userMsg) {
        String ans = null;
        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT answer FROM FAQ WHERE LOWER(question) LIKE LOWER(?)")
        ) {
            stmt.setString(1, "%" + userMsg + "%");
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                ans = rs.getString("answer");
            }
        } catch(Exception ex) { ex.printStackTrace(); }
        return ans;
    }

    private void saveMessageToDB(String userMsg, String botMsg) {
        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO Chat(user_id, message) VALUES ((SELECT user_id FROM Users WHERE username = ?), ?)")
        ) {
            stmt.setString(1, username);
            stmt.setString(2, userMsg + " | Bot: " + botMsg);
            stmt.executeUpdate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }
}
