package models;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;


public class Expense implements Serializable {
    private String category;
    private double amount;
    private String date;
    private String paymentMethod;
    private boolean isRecurring;

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void addExpense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public void deleteExpense(String category, double amount, String date) {
        this.category = null;
        this.amount = 0;
        this.date = null;
    }

    public void updateExpense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
   }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public Dictionary<String, String> getExpenseInfo() {
        Dictionary<String, String> expenseInfo = new Hashtable<>();
        expenseInfo.put("category", this.category);
        expenseInfo.put("amount", String.valueOf(this.amount));
        expenseInfo.put("date", this.date);
        expenseInfo.put("paymentMethod", this.paymentMethod);
        expenseInfo.put("isRecurring", String.valueOf(this.isRecurring));
        return expenseInfo;
    }
}
