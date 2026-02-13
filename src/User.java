public abstract class User {

    protected String userID;
    protected String passwordHash;
    protected boolean authenticated;

    public User(String userID, String rawPassword) {
        if (userID == null || userID.trim().isEmpty()) {
            throw new ConstraintViolationException("userID cannot be empty");
        }
        this.userID = userID;
        setPassword(rawPassword);
    }

    public String getUserID() {
        return userID;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setPassword(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new ConstraintViolationException("Password cannot be empty");
        }

        this.passwordHash = Integer.toHexString(rawPassword.hashCode());
    }

    public boolean checkPassword(String rawPassword) {
        String hash = Integer.toHexString(rawPassword.hashCode());
        return passwordHash.equals(hash);
    }
}
