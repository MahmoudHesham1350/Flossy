import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.Timer;

// Import controllers and factories
import controller.*;
import Factory.*;
import Factory.IControllerFactory;
import Factory.UserControllerFactory;
import models.*;
import reminder.*;
import service.*;
import storage.*;

/**
 * GUI is the graphical user interface for Flossy Personal Finance Manager.
 * It provides user authentication, navigation, data entry, and notification display using Java Swing.
 * The GUI supports all major features: registration, login, expenses, income, budgets, reminders, and notifications.
 */
public class GUI {
    /** The main application frame. */
    private JFrame frame;
    /** Panels for main, login, and dashboard views. */
    private JPanel mainPanel, loginPanel, dashboardPanel;
    /** Fields for user input during login/registration. */
    private JTextField emailField, usernameField, phoneField;
    private JPasswordField passwordField;
    /** The currently authenticated user. */
    private User currentUser = null;
    /** Controllers for user, expenses, income, budgets, and reminders. */
    private UserController userController;
    private Controller<Expense> expenseController;
    private Controller<Income> incomeController;
    private Controller<Budget> budgetController;
    private Controller<IReminder> paymentReminderController;
    private Controller<IReminder> budgetRemindController;
    /** Notification system components. */
    private Notification notification;
    private ReminderListener reminderListener;
    private Timer reminderCheckTimer;
    /** Panel for displaying notifications. */
    private JPanel notificationPanel;

    /**
     * Constructs the GUI, initializes controllers, and shows the login panel.
     */
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
    
    /**
     * Initializes the main user controllers (userController).
     */
    private void initializeControllers() {
        try {
            userController = (UserController) new UserControllerFactory().createController();
        } catch (Exception e) {
            showError("Error initializing application", e);
        }
    }
    
    /**
     * Initializes controllers for the currently logged-in user and sets up notifications and reminders.
     * Throws IllegalStateException if no user is logged in.
     */
    private void initializeUserSpecificControllers() {
        if (currentUser == null) {
            throw new IllegalStateException("User is not logged in");
        }
        try {
            expenseController = (Controller<Expense>) new ExpenseControllerFactory(currentUser).createController();
            incomeController = (Controller<Income>) new IncomeControllerFactory(currentUser).createController();
            budgetController = (Controller<Budget>) new BudgetControllerFactory(currentUser).createController();
            paymentReminderController = (Controller<IReminder>) new PaymentReminderControllerFactory(currentUser).createController();
            budgetRemindController = (Controller<IReminder>) new BudgetReminderControllerFactory(currentUser).createController();
            
            // Initialize notification system
            notification = new Notification();
            List<IReminder> allReminders = new ArrayList<>();
            allReminders.addAll(paymentReminderController.getAll());
            allReminders.addAll(budgetRemindController.getAll());
            reminderListener = new ReminderListener(notification, allReminders);
            
            // Start reminder checking timer
            startReminderCheckTimer();
            
            // Initial reminder check
            checkReminders();
        } catch (Exception e) {
            showError("Error initializing user data", e);
        }
    }

    /**
     * Initializes the login panel UI components and their event listeners.
     */
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
    
    /**
     * Handles user registration from the login panel.
     * Shows a dialog on success or error.
     */
    private void registerUser() {
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String phoneNumber = phoneField.getText();
        
        try {
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
    
    /**
     * Handles user login from the login panel.
     * Shows a dialog on error and switches to dashboard on success.
     */
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
    
    /**
     * Clears all login/registration input fields.
     */
    private void clearFields() {
        emailField.setText("");
        usernameField.setText("");
        passwordField.setText("");
        phoneField.setText("");
    }

    /**
     * Switches the main frame to the dashboard panel after login.
     */
    private void switchToDashboard() {
        frame.getContentPane().removeAll();
        initDashboardPanel();
        frame.add(dashboardPanel);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Initializes the dashboard panel, navigation, and notification area.
     */
    private void initDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());

        // Content panel for main page content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Navigation panel at the bottom
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton profileBtn = new JButton("Profile");
        JButton expensesBtn = new JButton("Expenses");
        JButton incomeBtn = new JButton("Income");
        JButton budgetsBtn = new JButton("Budgets");
        JButton remindersBtn = new JButton("Reminders");
        JButton logoutBtn = new JButton("Logout");

        navPanel.add(profileBtn);
        navPanel.add(expensesBtn);
        navPanel.add(incomeBtn);
        navPanel.add(budgetsBtn);
        navPanel.add(remindersBtn);
        navPanel.add(logoutBtn);

        // Action listeners for navigation
        profileBtn.addActionListener(e -> showProfilePanel(contentPanel));
        expensesBtn.addActionListener(e -> showExpensesPanel(contentPanel));
        incomeBtn.addActionListener(e -> showIncomePanel(contentPanel));
        budgetsBtn.addActionListener(e -> showBudgetsPanel(contentPanel));
        remindersBtn.addActionListener(e -> showRemindersPanel(contentPanel));
        logoutBtn.addActionListener(e -> logout());

        // Show welcome panel by default
        showWelcomePanel(contentPanel);

        // Notification panel
        notificationPanel = new JPanel();
        notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
        notificationPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));
        notificationPanel.setPreferredSize(new Dimension(frame.getWidth(), 100));

        // Add panels to dashboard
        dashboardPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        dashboardPanel.add(navPanel, BorderLayout.SOUTH);
        dashboardPanel.add(notificationPanel, BorderLayout.NORTH);
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
    
    private void showExpensesPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        // Add Expense button
        JButton addExpenseBtn = new JButton("Add Expense");
        addExpenseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addExpenseBtn.addActionListener(e -> showAddExpensePanel(contentPanel));
        contentPanel.add(addExpenseBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
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
        JTextField dateField = new JTextField(LocalDate.now().toString());
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
        
        formPanel.add(new JLabel("")); // Empty label for alignment
        formPanel.add(recurringCheckbox);
        
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
                dateField.setText(LocalDate.now().toString());
                paymentMethodField.setText("");
                recurringCheckbox.setSelected(false);
            } catch (Exception ex) {
                showError("Error adding expense", ex);
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
        
        // Add Income button
        JButton addIncomeBtn = new JButton("Add Income");
        addIncomeBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addIncomeBtn.addActionListener(e -> showAddIncomePanel(contentPanel));
        contentPanel.add(addIncomeBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
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
    
    private void showBudgetsPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        // Add Budget button
        JButton addBudgetBtn = new JButton("Add Budget");
        addBudgetBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBudgetBtn.addActionListener(e -> showAddBudgetPanel(contentPanel));
        contentPanel.add(addBudgetBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel titleLabel = new JLabel("Your Budgets");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel budgetListPanel = new JPanel();
        budgetListPanel.setLayout(new BoxLayout(budgetListPanel, BoxLayout.Y_AXIS));
        budgetListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        budgetListPanel.setBackground(Color.WHITE);
        
        List<Budget> budgets = budgetController.getAll();
        
        if (budgets.isEmpty()) {
            JLabel noBudgetsLabel = new JLabel("No budgets set yet.");
            noBudgetsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            budgetListPanel.add(noBudgetsLabel);
        } else {
            for (Budget budget : budgets) {
                JPanel card = new JPanel();
                card.setLayout(new BorderLayout());
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(180, 180, 180)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                card.setBackground(new Color(245, 245, 245));
                card.setMaximumSize(new Dimension(contentPanel.getWidth() - 40, 40));

                JLabel summaryLabel = new JLabel(budget.getAnalysis());
                card.add(summaryLabel, BorderLayout.CENTER);

                budgetListPanel.add(card);
                budgetListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(budgetListPanel);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(scrollPane);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void showAddBudgetPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Add Budget");
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
                // Refresh budgets list
                showBudgetsPanel(contentPanel);
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
    
    private void showRemindersPanel(JPanel contentPanel) {
        contentPanel.removeAll();
        
        JLabel titleLabel = new JLabel("Reminders");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel reminderPanel = new JPanel();
        reminderPanel.setLayout(new BoxLayout(reminderPanel, BoxLayout.Y_AXIS));
        
        JPanel addReminderPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Now 5 rows
        addReminderPanel.setBorder(BorderFactory.createTitledBorder("Add Budget Reminder"));
        addReminderPanel.setMaximumSize(new Dimension(contentPanel.getWidth() - 20, 180));
        
        JTextField messageField = new JTextField();
        JTextField dateField = new JTextField(LocalDate.now().toString());
        // Create budget selection combo box
        JComboBox<String> budgetCombo = new JComboBox<>();
        List<Budget> budgets = budgetController.getAll();
        for (Budget budget : budgets) {
            budgetCombo.addItem(budget.getAnalysis());
        }
        
        addReminderPanel.add(new JLabel("Message:"));
        addReminderPanel.add(messageField);
        addReminderPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        addReminderPanel.add(dateField);
        addReminderPanel.add(new JLabel("Select Budget:"));
        addReminderPanel.add(budgetCombo);
        
        JButton addBtn = new JButton("Add Reminder");
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        addBtn.addActionListener(e -> {
            try {
                String message = messageField.getText().trim();
                String date = dateField.getText().trim();
                if (message.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Message cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (date.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Date cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Validate date format
                try {
                    LocalDate.parse(date);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Date must be in yyyy-MM-dd format!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int selectedIndex = budgetCombo.getSelectedIndex();
                if (selectedIndex < 0 || selectedIndex >= budgets.size()) {
                    JOptionPane.showMessageDialog(frame, "Please select a budget!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Budget selectedBudget = budgets.get(selectedIndex);
                Dictionary<String, String> reminderData = new Hashtable<>();
                reminderData.put("message", message);
                reminderData.put("date", date);
                reminderData.put("budgetId", selectedBudget.getId().toString());
                paymentReminderController.create(reminderData);
                JOptionPane.showMessageDialog(frame, "Reminder added successfully!");
                // Clear fields
                messageField.setText("");
                dateField.setText(LocalDate.now().toString());
                budgetCombo.setSelectedIndex(0);
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
    
    private void startReminderCheckTimer() {
        reminderCheckTimer = new Timer(60000, e -> checkReminders()); // Check every minute
        reminderCheckTimer.start();
    }

    private void checkReminders() {
        reminderListener.checkReminders();
        updateNotificationPanel();
    }

    private void updateNotificationPanel() {
        if (notificationPanel == null) {
            return;  // Exit if notification panel hasn't been initialized yet
        }
        
        notificationPanel.removeAll();
        int notificationCount = notification.getNumberOfReminders();
        
        if (notificationCount > 0) {
            JLabel notificationLabel = new JLabel("You have " + notificationCount + " notification(s)!");
            notificationLabel.setFont(new Font("Arial", Font.BOLD, 14));
            notificationPanel.add(notificationLabel);
            
            // Add each notification
            for (String message : notification.getNotifications()) {
                JLabel messageLabel = new JLabel(message);
                notificationPanel.add(messageLabel);
            }
        } else {
            JLabel noNotificationsLabel = new JLabel("No new notifications");
            notificationPanel.add(noNotificationsLabel);
        }
        
        notificationPanel.revalidate();
        notificationPanel.repaint();
    }

    private void saveAllData() {
        userController.save();
        if (currentUser != null) {
            try {
                expenseController.save();
                incomeController.save();
                budgetController.save();
                paymentReminderController.save();
                budgetRemindController.save();
            } catch (Exception e) {
                showError("Error saving data", e);
            }
        }
    }

    private void logout() {
        if (reminderCheckTimer != null) {
            reminderCheckTimer.stop();
        }
        saveAllData();
        currentUser = null;
        frame.getContentPane().removeAll();
        initLoginPanel();
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

    /**
     * Main entry point for launching the GUI.
     * @param args Command-line arguments (not used)
     */
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
