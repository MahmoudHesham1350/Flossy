import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Import controllers and factories
import controller.*;
import Factory.*;
import users.*;
import Expense.*;
import income.*;
import budget.*;
import reminder.*;
import service.*;
import storage.*;

public class GUI {
    private JFrame frame;
    private JPanel mainPanel, loginPanel, dashboardPanel;
    private JTextField emailField, usernameField, phoneField;
    private JPasswordField passwordField;
    
    // User info
    private User currentUser = null;
    
    // Controllers
    private UserController userController;
    private Controller<Expense> expenseController;
    private Controller<Income> incomeController;
    private Controller<Budget> budgetController;
    private Controller<IReminder> paymentReminderController;
    
    // Notification panel
    private JPanel notificationPanel;

    public GUI() {
        initializeControllers();
        
        frame = new JFrame("Flossy - Personal Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        initLoginPanel();
        frame.add(loginPanel);
        frame.setVisible(true);
    }
    
    private void initializeControllers() {
        try {
            // Initialize user controller
            UserControllerFactory userFactory = new UserControllerFactory();
            userController = (UserController) userFactory.createController();
        } catch (Exception e) {
            showError("Error initializing application", e);
        }
    }
    
    private void initializeUserSpecificControllers() {
        if (currentUser == null) return;
        
        try {
            // Initialize controllers specific to the logged-in user
            BudgetControllerFactory budgetFactory = new BudgetControllerFactory(currentUser);
            budgetController = (Controller<Budget>) budgetFactory.createController();
            
            // Initialize expense controller
            IControllerFactory<Expense> expenseFactory = new IControllerFactory<Expense>() {
                @Override
                public IController<Expense> createController() throws Exception {
                    return new Controller<>(new ExpenseService(), new ExpenseStorage());
                }
            };
            expenseController = (Controller<Expense>) expenseFactory.createController();
            
            // Initialize income controller
            IncomeControllerFactory incomeFactory = new IncomeControllerFactory();
            incomeController = (Controller<Income>) incomeFactory.createController();
            
            // Initialize payment reminder controller
            PaymentReminderControllerFactory reminderFactory = new PaymentReminderControllerFactory(currentUser);
            paymentReminderController = (Controller<IReminder>) reminderFactory.createController();
            
        } catch (Exception e) {
            showError("Error initializing user data", e);
        }
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create components
        emailField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        phoneField = new JTextField();
        
        JButton registerBtn = new JButton("Register");
        JButton loginBtn = new JButton("Login");
        
        // Style components
        JLabel titleLabel = new JLabel("Welcome to Flossy", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Add components to panel
        loginPanel.add(titleLabel);
        loginPanel.add(new JLabel()); // Empty cell for spacing
        
        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(emailField);
        
        loginPanel.add(new JLabel("Username: (for registration)"));
        loginPanel.add(usernameField);
        
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        
        loginPanel.add(new JLabel("Phone: (for registration)"));
        loginPanel.add(phoneField);
        
        loginPanel.add(registerBtn);
        loginPanel.add(loginBtn);

        // Add action listeners
        registerBtn.addActionListener(e -> registerUser());
        loginBtn.addActionListener(e -> loginUser());
    }
    
    private void registerUser() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String phoneNumber = phoneField.getText();
        
        try {
            // Validate inputs
            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "All fields are required", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create user data dictionary
            Dictionary<String, String> userData = new Hashtable<>();
            userData.put("email", email);
            userData.put("username", username);
            userData.put("password", password);
            userData.put("phoneNumber", phoneNumber);
            
            // Create user via controller
            userController.create(userData);
            
            JOptionPane.showMessageDialog(frame, "Registration successful! Please login.");
            clearFields();
            
        } catch (Exception e) {
            showError("Registration error", e);
        }
    }
    
    private void loginUser() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        try {
            userController.loginUser(email, password);
            
            if (userController.isAuthenticated()) {
                currentUser = userController.getUser();
                initializeUserSpecificControllers();
                switchToDashboard();
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed. Check your credentials.", 
                    "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            showError("Login error", e);
        }
    }
    
    private void clearFields() {
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
    }

    private void switchToDashboard() {
        frame.getContentPane().removeAll();
        initDashboardPanel();
        frame.add(dashboardPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void initDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        
        // Create components for dashboard
        JPanel sidePanel = new JPanel(new GridLayout(9, 1, 0, 10));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        sidePanel.setPreferredSize(new Dimension(200, frame.getHeight()));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // User welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getEmail());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Create navigation buttons
        JButton profileBtn = new JButton("View Profile");
        JButton addExpenseBtn = new JButton("Add Expense");
        JButton viewExpensesBtn = new JButton("View Expenses");
        JButton addIncomeBtn = new JButton("Add Income");
        JButton viewIncomeBtn = new JButton("View Income");
        JButton setBudgetBtn = new JButton("Set Budget");
        JButton viewBudgetBtn = new JButton("View Budget");
        JButton viewRemindersBtn = new JButton("View Reminders");
        JButton logoutBtn = new JButton("Logout");
        
        // Add action listeners
        profileBtn.addActionListener(e -> showProfilePanel(contentPanel));
        addExpenseBtn.addActionListener(e -> showAddExpensePanel(contentPanel));
        viewExpensesBtn.addActionListener(e -> showExpensesPanel(contentPanel));
        addIncomeBtn.addActionListener(e -> showAddIncomePanel(contentPanel));
        viewIncomeBtn.addActionListener(e -> showIncomePanel(contentPanel));
        setBudgetBtn.addActionListener(e -> showAddBudgetPanel(contentPanel));
        viewBudgetBtn.addActionListener(e -> showBudgetsPanel(contentPanel));
        viewRemindersBtn.addActionListener(e -> showRemindersPanel(contentPanel));
        logoutBtn.addActionListener(e -> logout());
        
        // Add buttons to side panel
        sidePanel.add(welcomeLabel);
        sidePanel.add(profileBtn);
        sidePanel.add(addExpenseBtn);
        sidePanel.add(viewExpensesBtn);
        sidePanel.add(addIncomeBtn);
        sidePanel.add(viewIncomeBtn);
        sidePanel.add(setBudgetBtn);
        sidePanel.add(viewBudgetBtn);
        sidePanel.add(viewRemindersBtn);
        sidePanel.add(logoutBtn);
        
        // Initialize notification panel
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));
        notificationPanel.setPreferredSize(new Dimension(frame.getWidth() - 220, 150));
        
        // Add default welcome content to content panel
        showWelcomePanel(contentPanel);
        
        // Add components to dashboard
        dashboardPanel.add(sidePanel, BorderLayout.WEST);
        dashboardPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dashboardPanel.add(notificationPanel, BorderLayout.SOUTH);
    }
    
    private void showWelcomePanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome to your personal finance dashboard");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showProfilePanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("User Profile");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel profileDetails = new JPanel(new GridLayout(3, 2, 10, 10));
        profileDetails.setAlignmentX(Component.LEFT_ALIGNMENT);
        profileDetails.setMaximumSize(new Dimension(400, 100));
        
        Dictionary<String, String> userInfo = currentUser.getUserInfo();
        
        profileDetails.add(new JLabel("Email:"));
        profileDetails.add(new JLabel(currentUser.getEmail()));
        
        profileDetails.add(new JLabel("Username:"));
        profileDetails.add(new JLabel(userInfo.get("username")));
        
        profileDetails.add(new JLabel("Phone:"));
        profileDetails.add(new JLabel(userInfo.get("phoneNumber")));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(profileDetails);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddExpensePanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Add Expense");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 150));
        
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField paymentMethodField = new JTextField();
        JCheckBox recurringCheckbox = new JCheckBox("Recurring Expense");
        
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        
        formPanel.add(new JLabel("Payment Method:"));
        formPanel.add(paymentMethodField);
        
        JButton saveBtn = new JButton("Save Expense");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        saveBtn.addActionListener(e -> {
            try {
                Dictionary<String, String> expenseData = new Hashtable<>();
                expenseData.put("category", categoryField.getText());
                expenseData.put("amount", amountField.getText());
                expenseData.put("date", dateField.getText());
                expenseData.put("paymentMethod", paymentMethodField.getText());
                expenseData.put("isRecurring", String.valueOf(recurringCheckbox.isSelected()));
                
                expenseController.create(expenseData);
                JOptionPane.showMessageDialog(frame, "Expense added successfully!");
                
                // Clear fields
                categoryField.setText("");
                amountField.setText("");
                dateField.setText("");
                paymentMethodField.setText("");
                recurringCheckbox.setSelected(false);
                
            } catch (Exception ex) {
                showError("Error adding expense", ex);
            }
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(formPanel);
        contentPanel.add(recurringCheckbox);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(saveBtn);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showExpensesPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Your Expenses");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table model for expenses
        String[] columnNames = {"Category", "Amount", "Date", "Payment Method", "Recurring"};
        
        List<Expense> expenses = expenseController.getAll();
        Object[][] data = new Object[expenses.size()][5];
        
        for (int i = 0; i < expenses.size(); i++) {
            Expense expense = expenses.get(i);
            data[i][0] = expense.getCategory();
            data[i][1] = expense.getAmount();
            data[i][2] = expense.getDate();
            data[i][3] = expense.getPaymentMethod() != null ? expense.getPaymentMethod() : "N/A";
            data[i][4] = expense.isRecurring() ? "Yes" : "No";
        }
        
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(contentPanel.getWidth(), 300));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddIncomePanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Add Income");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 100));
        
        JTextField sourceField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        
        formPanel.add(new JLabel("Source:"));
        formPanel.add(sourceField);
        
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        
        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);
        
        JButton saveBtn = new JButton("Save Income");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        saveBtn.addActionListener(e -> {
            try {
                Dictionary<String, String> incomeData = new Hashtable<>();
                incomeData.put("source", sourceField.getText());
                incomeData.put("amount", amountField.getText());
                incomeData.put("date", dateField.getText());
                
                incomeController.create(incomeData);
                JOptionPane.showMessageDialog(frame, "Income added successfully!");
                
                // Clear fields
                sourceField.setText("");
                amountField.setText("");
                dateField.setText(LocalDate.now().toString());
                
            } catch (Exception ex) {
                showError("Error adding income", ex);
            }
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(saveBtn);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showIncomePanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Your Income");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create table model for income entries
        String[] columnNames = {"Source", "Amount", "Date"};
        
        List<Income> incomeEntries = incomeController.getAll();
        Object[][] data = new Object[incomeEntries.size()][3];
        
        for (int i = 0; i < incomeEntries.size(); i++) {
            Income income = incomeEntries.get(i);
            data[i][0] = income.getSource();
            data[i][1] = income.getAmount();
            data[i][2] = income.getDate();
        }
        
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(contentPanel.getWidth(), 300));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddBudgetPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Set Budget");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 100));
        
        JTextField categoryField = new JTextField();
        JTextField amountField = new JTextField();
        JTextField recurrenceField = new JTextField("0");
        
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        
        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);
        
        formPanel.add(new JLabel("Recurrence (days, 0 for one-time):"));
        formPanel.add(recurrenceField);
        
        JButton saveBtn = new JButton("Save Budget");
        saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        saveBtn.addActionListener(e -> {
            try {
                Dictionary<String, String> budgetData = new Hashtable<>();
                budgetData.put("category", categoryField.getText());
                budgetData.put("amount", amountField.getText());
                budgetData.put("recurrence", recurrenceField.getText());
                
                budgetController.create(budgetData);
                JOptionPane.showMessageDialog(frame, "Budget set successfully!");
                
                // Clear fields
                categoryField.setText("");
                amountField.setText("");
                recurrenceField.setText("0");
                
            } catch (Exception ex) {
                showError("Error setting budget", ex);
            }
        });
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(formPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(saveBtn);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showBudgetsPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Your Budgets");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel budgetListPanel = new JPanel();
        budgetListPanel.setLayout(new BoxLayout(budgetListPanel, BoxLayout.Y_AXIS));
        budgetListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        List<Budget> budgets = budgetController.getAll();
        
        if (budgets.isEmpty()) {
            JLabel noBudgetsLabel = new JLabel("No budgets set yet.");
            budgetListPanel.add(noBudgetsLabel);
        } else {
            for (Budget budget : budgets) {
                JPanel budgetPanel = new JPanel();
                budgetPanel.setLayout(new BoxLayout(budgetPanel, BoxLayout.Y_AXIS));
                budgetPanel.setBorder(BorderFactory.createTitledBorder(budget.getAnalysis()));
                budgetPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                budgetPanel.setMaximumSize(new Dimension(contentPanel.getWidth() - 20, 150));
                
                budgetListPanel.add(budgetPanel);
                budgetListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(budgetListPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showRemindersPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Reminders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.Y_AXIS));
        
        JPanel addReminderPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        addReminderPanel.setBorder(BorderFactory.createTitledBorder("Add Payment Reminder"));
        addReminderPanel.setMaximumSize(new Dimension(contentPanel.getWidth() - 20, 120));
        
        JTextField messageField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        JTextField recurrenceField = new JTextField("0");
        
        addReminderPanel.add(new JLabel("Message:"));
        addReminderPanel.add(messageField);
        
        addReminderPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        addReminderPanel.add(dateField);
        
        addReminderPanel.add(new JLabel("Recurrence (days, 0 for one-time):"));
        addReminderPanel.add(recurrenceField);
        
        JButton addBtn = new JButton("Add Reminder");
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addBtn.addActionListener(e -> {
            try {
                Dictionary<String, String> reminderData = new Hashtable<>();
                reminderData.put("message", messageField.getText());
                reminderData.put("date", dateField.getText());
                reminderData.put("recurrence", recurrenceField.getText());
                
                paymentReminderController.create(reminderData);
                JOptionPane.showMessageDialog(frame, "Reminder added successfully!");
                
                // Clear fields
                messageField.setText("");
                dateField.setText(LocalDate.now().toString());
                recurrenceField.setText("0");
                
                // Refresh reminders list
                showRemindersPanel(contentPanel);
                
            } catch (Exception ex) {
                showError("Error adding reminder", ex);
            }
        });
        
        // Get current reminders
        JPanel currentRemindersPanel = new JPanel();
        currentRemindersPanel.setLayout(new BoxLayout(currentRemindersPanel, BoxLayout.Y_AXIS));
        currentRemindersPanel.setBorder(BorderFactory.createTitledBorder("Current Reminders"));
        currentRemindersPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        List<IReminder> reminders = paymentReminderController.getAll();
        
        if (reminders.isEmpty()) {
            JLabel noRemindersLabel = new JLabel("No reminders set.");
            currentRemindersPanel.add(noRemindersLabel);
        } else {
            for (IReminder reminder : reminders) {
                JPanel reminderItem = new JPanel(new BorderLayout());
                reminderItem.setBorder(BorderFactory.createEtchedBorder());
                
                JLabel reminderLabel = new JLabel(reminder.getMessage());
                JButton deleteBtn = new JButton("Delete");
                
                deleteBtn.addActionListener(evt -> {
                    paymentReminderController.remove(reminder);
                    showRemindersPanel(contentPanel);
                });
                
                reminderItem.add(reminderLabel, BorderLayout.CENTER);
                reminderItem.add(deleteBtn, BorderLayout.EAST);
                
                currentRemindersPanel.add(reminderItem);
                currentRemindersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(currentRemindersPanel);
        scrollPane.setPreferredSize(new Dimension(contentPanel.getWidth() - 40, 200));
        
        reminderPanel.add(addReminderPanel);
        reminderPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        reminderPanel.add(addBtn);
        reminderPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        reminderPanel.add(scrollPane);
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(reminderPanel);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void logout() {
        currentUser = null;
        expenseController = null;
        incomeController = null;
        budgetController = null;
        paymentReminderController = null;
        
        frame.getContentPane().removeAll();
        clearFields();
        frame.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    }
    
    private void showError(String title, Exception e) {
        JOptionPane.showMessageDialog(
            frame,
            "Error: " + e.getMessage(),
            title,
            JOptionPane.ERROR_MESSAGE
        );
        e.printStackTrace();
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new GUI());
    }
}
