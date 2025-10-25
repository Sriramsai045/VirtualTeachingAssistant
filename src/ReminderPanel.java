import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReminderPanel extends JPanel {
    private DefaultListModel<String> model;
    private String username;

    public ReminderPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout(10,10));
        setBackground(new Color(60,60,60));
        setBorder(BorderFactory.createEmptyBorder(40,40,40,40));
        model = new DefaultListModel<>();

        JLabel lbl = new JLabel("Reminders", SwingConstants.CENTER);
        lbl.setFont(new Font("Dialog", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);
        add(lbl, BorderLayout.NORTH);

        JList<String> reminderList = new JList<>(model);
        reminderList.setFont(new Font("Dialog", Font.BOLD, 18));
        reminderList.setBackground(new Color(120,120,120));
        reminderList.setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(reminderList);
        add(scroll, BorderLayout.CENTER);

        JPanel addPanel = new JPanel(new BorderLayout());
        JTextField field = new JTextField("type here...");
        field.setForeground(Color.LIGHT_GRAY);
        field.setBackground(new Color(80,80,80));
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(field.getText().equals("type here...")) {
                    field.setText("");
                    field.setForeground(Color.WHITE);
                }
            }
            public void focusLost(FocusEvent e) {
                if(field.getText().trim().isEmpty()) {
                    field.setText("type here...");
                    field.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        JButton addBtn = new JButton("Add↑");
        addBtn.setBackground(Color.LIGHT_GRAY);
        addBtn.setForeground(Color.BLACK);

        addBtn.addActionListener(e -> {
            String reminder = field.getText().trim();
            if(!reminder.isEmpty() && !reminder.equals("type here...")) {
                addNewReminder(reminder);
                model.addElement(reminder);
                showPopup(reminder);
                field.setText("type here...");
                field.setForeground(Color.LIGHT_GRAY);
            }
        });

        addPanel.add(field, BorderLayout.CENTER);
        addPanel.add(addBtn, BorderLayout.EAST);
        add(addPanel, BorderLayout.SOUTH);

        loadReminders();
    }

    private void loadReminders() {
        model.clear();
        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT reminder_text FROM Reminder WHERE user_id=(SELECT user_id FROM Users WHERE username=?)")
        ) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                model.addElement(rs.getString("reminder_text"));
            }
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void addNewReminder(String reminder) {
        try (
                Connection conn = DBManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO Reminder(user_id, reminder_text) VALUES ((SELECT user_id FROM Users WHERE username = ?), ?)")
        ) {
            stmt.setString(1, username);
            stmt.setString(2, reminder);
            stmt.executeUpdate();
        } catch(Exception ex) { ex.printStackTrace(); }
    }

    private void showPopup(String reminder) {
        JOptionPane.showMessageDialog(this,
                "Reminder Added: " + reminder, "New Reminder", JOptionPane.INFORMATION_MESSAGE);
    }
}
