import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Virtual Teaching Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);

        JLabel title = new JLabel("VIRTUAL TEACHING ASSISTANT", JLabel.CENTER);
        title.setBounds(40, 20, 320, 30);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        add(title);

        JLabel userLabel = new JLabel("User id");
        userLabel.setBounds(60, 70, 100, 25);
        userLabel.setForeground(Color.WHITE);
        add(userLabel);

        userField = new JTextField();
        userField.setBounds(150, 70, 180, 25);
        userField.setBackground(Color.GRAY);
        userField.setForeground(Color.WHITE);
        add(userField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(60, 110, 100, 25);
        passLabel.setForeground(Color.WHITE);
        add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(150, 110, 180, 25);
        passField.setBackground(Color.GRAY);
        passField.setForeground(Color.WHITE);
        add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(130, 160, 120, 35);
        loginBtn.setBackground(Color.BLACK);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Dialog", Font.BOLD, 18));
        add(loginBtn);

        loginBtn.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";

        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                dispose();
                new MainDashboard(username).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

