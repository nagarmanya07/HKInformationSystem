public class Notification {

    private final User receiver;
    private final String message;

    public Notification(User receiver, String message) {
        if (receiver == null || message == null || message.trim().isEmpty()) {
            throw new ConstraintViolationException(
                    "Notification needs a receiver and a non-empty message");
        }
        this.receiver = receiver;
        this.message = message;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
