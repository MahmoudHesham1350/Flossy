import java.util.Scanner;
import java.util.List;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;
import controller.*;
import Factory.*;
import models.*;
import reminder.*;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;
    private static UserController userController;
    private static Controller<Expense> expenseController;
    private static Controller<Income> incomeController;
    private static Controller<Budget> budgetController;
    private static Controller<IReminder> paymentReminderController;
    private static Controller<IReminder> budgetRemindController;
    private static Notification notification;
    private static ReminderListener reminderListener;

    static {
        try {
            userController = (UserController) new UserControllerFactory().createController();
        } catch (Exception e) {
            System.out.println("Error initializing application: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void initializeUserControllers() throws Exception {
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
        reminderListener.checkReminders(); // Check for any pending reminders
    }

    private static void saveAllData() {
        userController.save();
        if (currentUser != null) {
            try {
                // Save expenses
                expenseController.save();
                // Save income records
                incomeController.save();
                // Save budgets
                budgetController.save();
                // Save reminders
                paymentReminderController.save();
                budgetRemindController.save();
                System.out.println("All data saved successfully.");
            } catch (Exception e) {
                System.out.println("Error saving data: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\nWelcome to Flossy - Personal Finance CLI");
            System.out.println("1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    registerUser();
                    break;
                case "2":
                    loginUser();
                    break;
                case "3":
                    saveAllData();
                    System.out.println("Exiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    private static void registerUser() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();

        try {
            Dictionary<String, String> userData = new Hashtable<>();
            userData.put("email", email);
            userData.put("username", username);
            userData.put("password", password);
            userData.put("phoneNumber", phoneNumber);

            userController.create(userData);
            System.out.println("Registration successful! Please login.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private static void loginUser() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        try {
            userController.loginUser(email, password);
            if (userController.isAuthenticated()) {
                currentUser = userController.getUser();
                initializeUserControllers();
                System.out.println("Login successful! Welcome, " + currentUser.getEmail() + ".\n");
                userMenu();
            }
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void userMenu() {
        while (true) {
            // Check for any new reminders
            reminderListener.checkReminders();
            
            int notificationCount = notification.getNumberOfReminders();
            System.out.println("\n--- User Menu ---");
            if (notificationCount > 0) {
                System.out.println("You have " + notificationCount + " notification(s)!");
            }
            
            System.out.println("1. View Profile");
            System.out.println("2. Add Expense");
            System.out.println("3. View Expenses");
            System.out.println("4. Add Income");
            System.out.println("5. View Income");
            System.out.println("6. Set Budget");
            System.out.println("7. View Budget");
            System.out.println("8. View Reminders");
            System.out.println("9. View Notifications");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    viewProfile();
                    break;
                case "2":
                    addExpense();
                    break;
                case "3":
                    viewExpenses();
                    break;
                case "4":
                    addIncome();
                    break;
                case "5":
                    viewIncome();
                    break;
                case "6":
                    setBudget();
                    break;
                case "7":
                    viewBudget();
                    break;
                case "8":
                    viewReminders();
                    break;
                case "9":
                    viewNotifications();
                    break;
                case "10":
                    saveAllData();
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void viewProfile() {
        System.out.println("\n--- Profile ---");
        Dictionary<String, String> userInfo = currentUser.getUserInfo();
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Username: " + userInfo.get("username"));
        System.out.println("Phone: " + userInfo.get("phoneNumber"));
    }

    private static void addExpense() {
        try {
            System.out.print("Category: ");
            String category = scanner.nextLine();
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Date (yyyy-MM-dd): ");
            String date = scanner.nextLine();
            System.out.print("Payment Method: ");
            String paymentMethod = scanner.nextLine();
            System.out.print("Is this a recurring expense? (yes/no): ");
            boolean isRecurring = scanner.nextLine().toLowerCase().startsWith("y");

            Dictionary<String, String> expenseData = new Hashtable<>();
            expenseData.put("category", category);
            expenseData.put("amount", String.valueOf(amount));
            expenseData.put("date", date);
            expenseData.put("paymentMethod", paymentMethod);
            expenseData.put("isRecurring", String.valueOf(isRecurring));

            expenseController.create(expenseData);
            System.out.println("Expense added successfully.");
        } catch (Exception e) {
            System.out.println("Failed to add expense: " + e.getMessage());
        }
    }

    private static void viewExpenses() {
        System.out.println("\n--- Expenses ---");
        List<Expense> expenses = expenseController.getAll();
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
            return;
        }

        System.out.println("Category\tAmount\tDate\tPayment Method\tRecurring");
        System.out.println("-".repeat(60));
        
        for (Expense expense : expenses) {
            Dictionary<String, String> info = expense.getExpenseInfo();
            System.out.printf("%s\t%.2f\t%s\t%s\t%s%n",
                info.get("category"),
                Double.parseDouble(info.get("amount")),
                info.get("date"),
                info.get("paymentMethod"),
                Boolean.parseBoolean(info.get("isRecurring")) ? "Yes" : "No");
        }
    }

    private static void addIncome() {
        try {
            System.out.print("Source: ");
            String source = scanner.nextLine();
            System.out.print("Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Date (yyyy-MM-dd): ");
            String date = scanner.nextLine();

            Dictionary<String, String> incomeData = new Hashtable<>();
            incomeData.put("source", source);
            incomeData.put("amount", String.valueOf(amount));
            incomeData.put("date", date);

            incomeController.create(incomeData);
            System.out.println("Income recorded successfully.");
        } catch (Exception e) {
            System.out.println("Failed to record income: " + e.getMessage());
        }
    }

    private static void viewIncome() {
        System.out.println("\n--- Income ---");
        List<Income> incomes = incomeController.getAll();
        if (incomes.isEmpty()) {
            System.out.println("No income recorded yet.");
            return;
        }

        System.out.println("Source\tAmount\tDate");
        System.out.println("-".repeat(40));
        
        for (Income income : incomes) {
            Dictionary<String, String> info = income.getIncomeInfo();
            System.out.printf("%s\t%.2f\t%s%n",
                info.get("source"),
                Double.parseDouble(info.get("amount")),
                info.get("date"));
        }
    }

    private static void setBudget() {
        try {
            System.out.print("Category: ");
            String category = scanner.nextLine();
            System.out.print("Amount limit: ");
            double amount = Double.parseDouble(scanner.nextLine());
            System.out.print("Recurrence period (days, 0 for one-time): ");
            int recurrence = Integer.parseInt(scanner.nextLine());

            Dictionary<String, String> budgetData = new Hashtable<>();
            budgetData.put("category", category);
            budgetData.put("amount", String.valueOf(amount));
            budgetData.put("recurrence", String.valueOf(recurrence));

            budgetController.create(budgetData);
            System.out.println("Budget set successfully.");

            // Ask if user wants to set a reminder for this budget
            System.out.print("Would you like to set a reminder for this budget? (yes/no): ");
            if (scanner.nextLine().toLowerCase().startsWith("y")) {
                System.out.print("Enter reminder message: ");
                String message = scanner.nextLine();

                Dictionary<String, String> reminderData = new Hashtable<>();
                reminderData.put("message", message);
                reminderData.put("budgetId", budgetController.getAll().get(budgetController.getAll().size() - 1).getId().toString());

                budgetRemindController.create(reminderData);
                System.out.println("Budget reminder set successfully.");
            }
        } catch (Exception e) {
            System.out.println("Failed to set budget: " + e.getMessage());
        }
    }

    private static void viewBudget() {
        System.out.println("\n--- Budgets ---");
        List<Budget> budgets = budgetController.getAll();
        if (budgets.isEmpty()) {
            System.out.println("No budgets set yet.");
            return;
        }

        for (Budget budget : budgets) {
            System.out.println("-".repeat(40));
            System.out.println(budget.getAnalysis());
        }
    }

    private static void viewReminders() {
        System.out.println("\n--- Reminders ---");
        List<IReminder> paymentReminders = paymentReminderController.getAll();
        List<IReminder> budgetReminders = budgetRemindController.getAll();

        if (paymentReminders.isEmpty() && budgetReminders.isEmpty()) {
            System.out.println("No reminders set.");
            return;
        }

        System.out.println("Payment Reminders:");
        System.out.println("-".repeat(40));
        if (paymentReminders.isEmpty()) {
            System.out.println("No payment reminders set.");
        } else {
            for (IReminder reminder : paymentReminders) {
                System.out.println(reminder.getMessage());
            }
        }

        System.out.println("\nBudget Reminders:");
        System.out.println("-".repeat(40));
        if (budgetReminders.isEmpty()) {
            System.out.println("No budget reminders set.");
        } else {
            for (IReminder reminder : budgetReminders) {
                System.out.println(reminder.getMessage());
            }
        }

        // Add option to create a new payment reminder
        System.out.print("\nWould you like to add a new payment reminder? (yes/no): ");
        if (scanner.nextLine().toLowerCase().startsWith("y")) {
            try {
                System.out.print("Enter reminder message: ");
                String message = scanner.nextLine();
                System.out.print("Enter reminder date (yyyy-MM-dd): ");
                String date = scanner.nextLine();

                Dictionary<String, String> reminderData = new Hashtable<>();
                reminderData.put("message", message);
                reminderData.put("date", date);

                paymentReminderController.create(reminderData);
                System.out.println("Payment reminder added successfully.");
            } catch (Exception e) {
                System.out.println("Failed to add reminder: " + e.getMessage());
            }
        }
    }

    private static void viewNotifications() {
        System.out.println("\n--- Notifications ---");
        List<String> notifications = notification.getNotifications();
        if (notifications.isEmpty()) {
            System.out.println("No notifications available.");
        } else {
            for (String note : notifications) {
                System.out.println(note);
            }
        }
    }
}
