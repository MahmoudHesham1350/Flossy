package models;
import java.time.LocalDate;
import java.util.UUID;

public class Budget implements java.io.Serializable {
    private final UUID id;
    private String category;
    private double amount;
    private double spentAmount;
    private int recurrence;
    private LocalDate createdDate;

    public Budget(String category, double amount, int recurrence) {
        this.id = UUID.randomUUID();
        this.category = category;
        this.amount = amount;
        this.spentAmount = 0.0;
        this.recurrence = recurrence;
        this.createdDate = LocalDate.now();
    }

    public Budget(String category, double amount) {
        this(category, amount, 0);
    }
    public UUID getId() {
        return id;
    }

    public LocalDate getNextDate() {
        return createdDate.plusDays(recurrence);
    }
    public void nextPeriod() {
        if (recurrence > 0) {
            createdDate = createdDate.plusDays(recurrence);
        }
    }

    public boolean isOneTime() {
        return recurrence == 0;
    }

    public void addSpending(double amount) {
        this.spentAmount += amount;
    }

    public void resetSpentAmount() {
        this.spentAmount = 0.0;
    }

    public boolean isExceeded() {
        return spentAmount > amount;
    }
    public String getAnalysis() {
        return "Category: " + category + "\n" +
               "Budgeted Amount: " + amount + "\n" +
               "Spent Amount: " + spentAmount + "\n" +
               (isExceeded() ? "Budget Exceeded!" : "Budget Not Exceeded");
    }
}
