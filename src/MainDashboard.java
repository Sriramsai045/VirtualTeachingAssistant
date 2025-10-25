import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public MainDashboard(String username) {
        setTitle("Virtual Teaching Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.BLACK);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(40, 38, 38));
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(null);

        JLabel brand = new JLabel("<html><center>VIRTUAL<br>TEACHING<br>ASSISTANT</center></html>", SwingConstants.CENTER);
        brand.setForeground(Color.WHITE);
        brand.setFont(new Font("Monospaced", Font.BOLD, 16));
        brand.setBounds(25, 24, 200, 60);
        sidebar.add(brand);

        JButton chatBtn = new JButton("Chat");
        styleNavButton(chatBtn, true);
        chatBtn.setBounds(25, 120, 200, 40);
        sidebar.add(chatBtn);

        JButton faqBtn = new JButton("FAQ’s");
        styleNavButton(faqBtn, false);
        faqBtn.setBounds(25, 180, 200, 40);
        sidebar.add(faqBtn);

        JButton reminderBtn = new JButton("Reminders");
        styleNavButton(reminderBtn, false);
        reminderBtn.setBounds(25, 240, 200, 40);
        sidebar.add(reminderBtn);

        add(sidebar, BorderLayout.WEST);

        // Content area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.BLACK);
        contentPanel.add(new ChatPanel(username), "chat");
        contentPanel.add(new FAQPanel(), "faq");
        contentPanel.add(new ReminderPanel(username), "reminder");
        add(contentPanel, BorderLayout.CENTER);

        chatBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "chat");
            styleNavButton(chatBtn, true);
            styleNavButton(faqBtn, false);
            styleNavButton(reminderBtn, false);
        });
        faqBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "faq");
            styleNavButton(chatBtn, false);
            styleNavButton(faqBtn, true);
            styleNavButton(reminderBtn, false);
        });
        reminderBtn.addActionListener(e -> {
            cardLayout.show(contentPanel, "reminder");
            styleNavButton(chatBtn, false);
            styleNavButton(faqBtn, false);
            styleNavButton(reminderBtn, true);
        });
    }

    private void styleNavButton(JButton btn, boolean selected) {
        btn.setFocusPainted(false);
        btn.setBackground(selected ? Color.LIGHT_GRAY : new Color(80, 78, 78));
        btn.setForeground(selected ? Color.BLACK : Color.WHITE);
        btn.setFont(new Font("Dialog", Font.BOLD, 17));
        btn.setBorder(BorderFactory.createEmptyBorder());
    }
}
