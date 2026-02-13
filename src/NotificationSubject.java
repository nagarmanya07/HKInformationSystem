import java.util.ArrayList;
import java.util.List;

public class NotificationSubject implements Subject {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Notification notification) {

        for (Observer o : observers) {
            o.update(notification);
        }
    }
}
