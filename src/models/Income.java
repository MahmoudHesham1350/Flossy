package models;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Hashtable;


public class Income implements Serializable {
    private String source;
    private double amount;
    private String date;

    public Income(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void addIncome(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
    }
   public void deleteIncome(String source, double amount, String date) {
        this.source = null;
        this.amount = 0;
        this.date = null;
    }
    public void updateIncome(String source, double amount, String date) {
        this.source = source;
        this.amount = amount;
        this.date = date;
   }

    public Dictionary<String, String> getIncomeInfo() {
        Dictionary<String, String> incomeInfo = new Hashtable<>();
        incomeInfo.put("source", this.source);
        incomeInfo.put("amount", String.valueOf(this.amount));
        incomeInfo.put("date", this.date);
        return incomeInfo;
    }
}
