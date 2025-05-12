import java.util.Scanner;
import java.util.UUID;
import java.util.List;

public class CLI {
    private static Scanner scanner = new Scanner(System.in);

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

        if (true) { // Add Registration Success Condition
            System.out.println("Registration successful! Please login.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void loginUser() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (true) { // Add Login Success Condition

            System.out.println("Login successful! Welcome, " + email + ".\n\n");
            userMenu();
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Profile");
            System.out.println("2. Add Expense");
            System.out.println("3. View Expenses");
            System.out.println("4. Add Income");
            System.out.println("5. View Income");
            System.out.println("6. Set Budget");
            System.out.println("7. View Budget");
            System.out.println("8. View Reminders");
            System.out.println("9. Logout");
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
                    System.out.println("Logged out successfully.");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void viewProfile() {
        System.out.println("\n--- Profile ---");
    }

    private static void addExpense() {
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean success = true; // Check for Expense Addition Success
        System.out.println(success ? "Expense added." : "Failed to add expense.");
    }

    private static void viewExpenses() {
        System.out.println("\n--- Expenses ---");

    }

    private static void addIncome() {
        System.out.print("Source: ");
        String source = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean success = true; // Check for Income Addition Success
        System.out.println(success ? "Income recorded." : "Failed to record income.");
    }

    private static void viewIncome() {
        System.out.println("\n--- Incomes ---");

    }

    private static void setBudget() {
        System.out.print("Category: ");
        String category = scanner.nextLine();
        System.out.print("Limit: ");
        double limit = Double.parseDouble(scanner.nextLine());

        boolean success = true;
        System.out.println(success ? "Budget set." : "Failed to set budget.");
    }

    private static void viewBudget() {
        System.out.println("\n--- Budgets ---");

    }

    private static void viewReminders() {
        System.out.println("\n--- Reminders ---");

    }
}
