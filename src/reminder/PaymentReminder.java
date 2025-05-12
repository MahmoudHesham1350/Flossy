package reminder;
import java.time.LocalDate;

public class PaymentReminder implements IReminder {
    private String message;
    private LocalDate reminderDate;
    private int recurrence;

    public PaymentReminder(String message, LocalDate reminderDate) {
        this.message = message;
        this.reminderDate = reminderDate;
    }

    @Override
    public void nextOccurrence() {
        if(reminderDate.isBefore(LocalDate.now())) {
            return;
        }
        LocalDate nextDate = reminderDate.plusDays(recurrence);
        while (nextDate.isBefore(LocalDate.now())) {
            reminderDate = reminderDate.plusDays(recurrence);
        }
    }

    @Override
    public boolean isTriggered() {
        return reminderDate.isEqual(LocalDate.now()) || reminderDate.isBefore(LocalDate.now());
    }
    
    @Override
    public boolean isOneTime() {
        return recurrence == 0;
    }

    @Override
    public String getMessage() {
        return message + " on " + reminderDate;
    }


}
