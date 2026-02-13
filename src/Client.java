public class Client extends User implements Observer {

    public Client(String userID, String password) {
        super(userID, password);
    }

    @Override
    public void update(Notification notification) {
        System.out.println("Client " + userID +
                " got notification: " + notification.getMessage());
    }
}
