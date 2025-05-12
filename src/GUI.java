import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private JFrame frame;
    private JPanel mainPanel, loginPanel, dashboardPanel;
    private JTextField emailField, usernameField;
    private JPasswordField passwordField;
    private String currentUser = null;

    public GUI() {
        frame = new JFrame("Flossy - Personal Finance GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        initLoginPanel();
        frame.add(loginPanel);
        frame.setVisible(true);
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridLayout(6, 1));
        emailField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");

        registerBtn.addActionListener(e -> {
            // Simulated registration
            String email = emailField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
            } else {
                JOptionPane.showMessageDialog(frame, "Registration failed. Fill all fields.");
            }
        });

        loginBtn.addActionListener(e -> {
            // Simulated login
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (email.equals("user@example.com") && password.equals("1234")) {
                currentUser = email;
                switchToDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials.");
            }
        });

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(registerBtn);
        loginPanel.add(loginBtn);
    }

    private void switchToDashboard() {
        frame.getContentPane().removeAll();
        initDashboardPanel();
        frame.add(dashboardPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void initDashboardPanel() {
        dashboardPanel = new JPanel(new GridLayout(9, 1));
        dashboardPanel.add(new JLabel("Welcome, " + currentUser));

        JButton profileBtn = new JButton("View Profile");
        JButton addExpenseBtn = new JButton("Add Expense");
        JButton viewExpensesBtn = new JButton("View Expenses");
        JButton addIncomeBtn = new JButton("Add Income");
        JButton viewIncomeBtn = new JButton("View Income");
        JButton setBudgetBtn = new JButton("Set Budget");
        JButton viewBudgetBtn = new JButton("View Budget");
        JButton viewRemindersBtn = new JButton("View Reminders");
        JButton logoutBtn = new JButton("Logout");

        profileBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Username: DemoUser\nEmail: " + currentUser));
        addExpenseBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Expense added successfully!"));
        viewExpensesBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "You have 3 expenses logged."));
        addIncomeBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Income recorded."));
        viewIncomeBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Income entries: 2"));
        setBudgetBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Budget set for category."));
        viewBudgetBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Viewing set budgets..."));
        viewRemindersBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Upcoming reminders shown here."));
        logoutBtn.addActionListener(e -> {
            currentUser = null;
            frame.getContentPane().removeAll();
            frame.add(loginPanel);
            frame.revalidate();
            frame.repaint();
        });

        dashboardPanel.add(profileBtn);
        dashboardPanel.add(addExpenseBtn);
        dashboardPanel.add(viewExpensesBtn);
        dashboardPanel.add(addIncomeBtn);
        dashboardPanel.add(viewIncomeBtn);
        dashboardPanel.add(setBudgetBtn);
        dashboardPanel.add(viewBudgetBtn);
        dashboardPanel.add(viewRemindersBtn);
        dashboardPanel.add(logoutBtn);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
