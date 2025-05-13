# Flossy Personal Finance Manager

Flossy is a modular Java application for personal finance management. It supports both CLI and GUI interfaces, and is structured using the MVC (Model-View-Controller) pattern, with additional use of the Factory and Observer (Listener) patterns. The application persists user data using simple file-based storage.

---

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Main Components](#main-components)
  - [Models](#models)
  - [Reminders & Notifications](#reminders--notifications)
  - [Storage](#storage)
  - [Services](#services)
  - [Controllers](#controllers)
  - [Factories](#factories)
  - [User Interfaces](#user-interfaces)
- [Key Design Patterns](#key-design-patterns)
- [Data Flow Example](#data-flow-example)
- [Extensibility](#extensibility)

---

## Architecture Overview

Flossy is organized into clear packages for models, controllers, services, storage, reminders, and factories. It uses:
- **MVC** for separation of concerns
- **Factory Pattern** for controller instantiation
- **Observer Pattern** for reminders/notifications
- **File-based persistence** for all user data

---

## Main Components

### Models
- **User** (`models/User.java`): Represents a user with email, username, password, phone number, and a unique UUID. Provides methods for password checking and user info retrieval.
- **Expense** (`models/Expense.java`): Represents an expense with category, amount, date, payment method, and recurrence. Includes methods for CRUD and info extraction.
- **Income** (`models/Income.java`): Represents an income entry with source, amount, and date. Includes methods for CRUD and info extraction.
- **Budget** (`models/Budget.java`): Represents a budget for a category, with amount, spent amount, recurrence, and analysis methods. Tracks spending and period.
- **PaymentReminder** (`models/PaymentReminder.java`): Implements `IReminder`, represents a reminder for payments, with message, date, and recurrence logic.
- **BudgetReminder** (`models/BudgetReminder.java`): Implements `IReminder`, represents a reminder for budgets, linked to a `Budget` object.

### Reminders & Notifications
- **IReminder** (`reminder/IReminder.java`): Interface for reminders, with methods for message, occurrence, and trigger logic.
- **Notification** (`reminder/Notification.java`): Holds a list of triggered reminders and provides notification messages.
- **ReminderListener** (`reminder/ReminderListener.java`): Observes reminders, checks if they are triggered, and adds them to notifications.

### Storage
- **UltraSimpleStorage<T>** (`storage/UltraSimpleStorage.java`): Abstract class for file-based storage of serializable objects. Handles save/load/add/remove/getAll.
- **UserStorage** (`storage/UserStorage.java`): Stores all users in `users.dat`.
- **ExpenseStorage** (`storage/ExpenseStorage.java`): Stores expenses per user in `<userID>Expense.dat`.
- **IncomeStorage** (`storage/IncomeStorage.java`): Stores incomes per user in `<userID>Income.dat`.
- **BudgetStorage** (`storage/BudgetStorage.java`): Stores budgets per user in `<userID>budget.dat`.
- **ReminderStorage** (`storage/ReminderStorage.java`): Stores reminders per user in `<userID>reminders.dat`.

### Services
- **IService<T>** (`service/IService.java`): Interface for validation logic.
- **UserService** (`service/UserService.java`): Validates user data and handles login logic.
- **ExpenseService** (`service/ExpenseService.java`): Validates expense data.
- **IncomeService** (`service/IncomeService.java`): Validates income data.
- **BudgetService** (`service/BudgetService.java`): Validates budget data.
- **PaymentReminderService** (`service/PaymentReminderService.java`): Validates payment reminder data.
- **BudgetReminderService** (`service/BudgetReminderService.java`): Validates budget reminder data, links reminders to budgets.

### Controllers
- **IController<T>** (`controller/IController.java`): Interface for basic CRUD operations.
- **Controller<T>** (`controller/Controller.java`): Generic controller for most models, handles creation, retrieval, removal, and saving.
- **UserController** (`controller/UserController.java`): Specialized controller for user authentication and management. Handles login, registration, and user-specific queries.

### Factories
- **IControllerFactory<T>** (`Factory/IControllerFactory.java`): Interface for controller factories.
- **UserControllerFactory** (`Factory/UserControllerFactory.java`): Creates a `UserController` with the correct service and storage.
- **ExpenseControllerFactory** (`Factory/ExpenseControllerFactory.java`): Creates a `Controller<Expense>` for a specific user.
- **IncomeControllerFactory** (`Factory/IncomeControllerFactory.java`): Creates a `Controller<Income>` for a specific user.
- **BudgetControllerFactory** (`Factory/BudgetControllerFactory.java`): Creates a `Controller<Budget>` for a specific user.
- **PaymentReminderControllerFactory** (`Factory/PaymentReminderControllerFactory.java`): Creates a `Controller<IReminder>` for payment reminders for a specific user.
- **BudgetReminderControllerFactory** (`Factory/BudgetReminderControllerFactory.java`): Creates a `Controller<IReminder>` for budget reminders for a specific user.

### User Interfaces
- **CLI.java**: Command-line interface for user interaction. Handles registration, login, menu navigation, and invokes controllers for all operations. Integrates notifications and reminders.
- **GUI.java**: Graphical interface using Swing, with similar logic to CLI.
- **Main.java**: Entry point for the application.

---

## GUI Usage

The GUI provides a modern, user-friendly interface for all Flossy features:

- **Navigation**: Use the navigation bar at the bottom to switch between Profile, Expenses, Income, Budgets, Reminders, and Logout.
- **Adding Data**: Each main page (Expenses, Income, Budgets, Reminders) has an "Add" button at the top. Clicking it opens a form to add a new entry.
  - **Add Expense**: Enter category, amount, date, payment method, and whether it is recurring.
  - **Add Income**: Enter source, amount, and date.
  - **Add Budget**: Enter category, amount, and recurrence (in days).
  - **Add Reminder**: Enter message, date, and select a budget. All fields are validated before submission.
- **Validation**: The GUI checks for empty fields and correct date formats before submitting data. Errors are shown in dialog boxes.
- **Notifications**: The top of the window displays notifications for triggered reminders.
- **Data Persistence**: All data is saved automatically on logout.

---

## Key Design Patterns
- **MVC**: Models represent data, Controllers handle logic, CLI/GUI are the views.
- **Factory Pattern**: Used for controller instantiation, ensuring correct dependencies.
- **Observer/Listener Pattern**: `ReminderListener` checks reminders and updates notifications.
- **Persistence**: All data is serialized and saved to disk using `UltraSimpleStorage`.

---

## Data Flow Example
1. **User Registration/Login**:  
   - CLI/GUI collects user info, calls `UserController` (created by `UserControllerFactory`).
   - User data is validated by `UserService`, stored in `UserStorage`.
2. **Adding Expense/Income/Budget**:  
   - CLI/GUI collects data, calls the appropriate controller (created by its factory).
   - Data is validated by the corresponding service, stored in the appropriate storage.
3. **Reminders & Notifications**:  
   - Reminders are created and stored.
   - `ReminderListener` checks all reminders on each menu view, and adds triggered ones to `Notification`.
   - Notifications are shown in the menu and can be viewed in detail.
4. **Persistence**:  
   - On logout or exit, all controllers save their data to disk.

---

## Extensibility
- New models can be added by creating a model, service, storage, controller, and factory.
- The CLI/GUI can be extended to support new features or data types.
- The storage system is file-based but can be swapped for a database with minimal changes to controllers.

---

## Class-by-Class Details

### CLI.java
- Main command-line interface. Handles user registration, login, menu navigation, and all user actions.
- Integrates notifications and reminders, showing notification counts and details.
- Calls the appropriate controller for each user action.

### GUI.java
- Graphical user interface using Java Swing.
- Provides similar functionality to CLI but with a visual interface.
- Handles user authentication, data entry, and displays notifications.

### Main.java
- Entry point for the application. Can be used to launch either CLI or GUI.

### controller/IController.java
- Interface for all controllers. Defines `create`, `getAll`, and `remove` methods.

### controller/Controller.java
- Generic controller for most models. Handles creation, retrieval, removal, and saving of data.
- Uses a service for validation and a storage for persistence.

### controller/UserController.java
- Specialized controller for user management.
- Handles login, registration, authentication checks, and user retrieval.
- Uses `UserService` for validation and `UserStorage` for persistence.

### Factory/IControllerFactory.java
- Interface for all controller factories. Defines `createController()` method.

### Factory/UserControllerFactory.java
- Creates a `UserController` with the correct service and storage.

### Factory/ExpenseControllerFactory.java
- Creates a `Controller<Expense>` for a specific user, with user-specific storage.

### Factory/IncomeControllerFactory.java
- Creates a `Controller<Income>` for a specific user, with user-specific storage.

### Factory/BudgetControllerFactory.java
- Creates a `Controller<Budget>` for a specific user, with user-specific storage.

### Factory/PaymentReminderControllerFactory.java
- Creates a `Controller<IReminder>` for payment reminders for a specific user.

### Factory/BudgetReminderControllerFactory.java
- Creates a `Controller<IReminder>` for budget reminders for a specific user.

### models/User.java
- Represents a user with email, username, password, phone number, and UUID.
- Provides methods for password checking and user info retrieval.

### models/Expense.java
- Represents an expense with category, amount, date, payment method, and recurrence.
- Provides methods for CRUD and info extraction.

### models/Income.java
- Represents an income entry with source, amount, and date.
- Provides methods for CRUD and info extraction.

### models/Budget.java
- Represents a budget for a category, with amount, spent amount, recurrence, and analysis methods.
- Tracks spending and period.

### models/PaymentReminder.java
- Implements `IReminder`, represents a reminder for payments, with message, date, and recurrence logic.

### models/BudgetReminder.java
- Implements `IReminder`, represents a reminder for budgets, linked to a `Budget`