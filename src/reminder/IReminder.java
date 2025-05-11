package reminder;
import java.io.Serializable;


public interface IReminder extends Serializable {
    String getMessage();
    void nextOccurrence();
    boolean isTriggered();
    boolean isOneTime();
}