public class Employee extends User implements Observer {

    public Employee(String userID, String password) {
        super(userID, password);
    }

    @Override
    public void update(Notification notification) {
        System.out.println("Employee " + userID +
                " got notification: " + notification.getMessage());
    }
}
