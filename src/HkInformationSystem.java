import java.io.IOException;
import java.util.*;

public class HkInformationSystem implements HkInformationInterface {


    private final List<User> users = new ArrayList<>();
    private final List<Document> documents = new ArrayList<>();
    private final List<ExternalDataSource> externalSources = new ArrayList<>();


    private final Map<User, List<Document>> userSearchResults = new HashMap<>();

    private User currentUser;
    private String lastSearchStatus;

    private final NotificationSubject notificationSubject = new NotificationSubject();




    private void checkLoggedIn() {
        if (currentUser == null || !currentUser.isAuthenticated()) {
            throw new PreconditionViolationException("User must be logged in first");
        }
    }


    private void checkKeyword(String keyword) {
        if (keyword == null || keyword.trim().length() < 3) {
            throw new PreconditionViolationException("Keyword must be at least 3 characters");
        }
    }


    public void addUser(User user) {
        for (User u : users) {
            if (u.getUserID().equals(user.getUserID())) {
                throw new ConstraintViolationException("userID must be unique");
            }
        }
        users.add(user);

        if (user instanceof Observer) {
            notificationSubject.attach((Observer) user);
        }
    }

    public void addDocument(Document document) {
        for (Document d : documents) {
            if (d.getDocID().equals(document.getDocID())) {
                throw new ConstraintViolationException("docID must be unique");
            }
        }
        documents.add(document);
    }

    public void addExternalSource(ExternalDataSource src) {
        externalSources.add(src);
    }




    @Override
    public User logIn(String userID, String password) {
        if (userID == null || password == null ||
                userID.trim().isEmpty() || password.trim().isEmpty()) {
            throw new PreconditionViolationException("userID and password cannot be empty");
        }

        User found = null;
        for (User u : users) {
            if (u.getUserID().equals(userID)) {
                found = u;
                break;
            }
        }
        if (found == null) {
            throw new PreconditionViolationException("User not found");
        }

        if (!found.checkPassword(password)) {
            throw new PreconditionViolationException("Wrong password");
        }

        found.authenticated = true;
        currentUser = found;
        return found;
    }


    @Override
    public List<Document> searchInformation(String keyword) {
        checkLoggedIn();
        checkKeyword(keyword);

        List<Document> results = new ArrayList<>();
        String low = keyword.toLowerCase();

        for (Document d : documents) {
            if (d.getTitle().toLowerCase().contains(low)
                    || d.getContent().toLowerCase().contains(low)) {
                results.add(d);
            }
        }

        if (results.isEmpty()) {
            lastSearchStatus = "NO_RESULTS";
        } else {
            lastSearchStatus = "RESULTS_FOUND";
        }

        userSearchResults.put(currentUser, results);
        return results;
    }


    @Override
    public List<Document> filterResults(String criteria) {
        checkLoggedIn();

        List<Document> base = userSearchResults.get(currentUser);
        if (base == null) {
            base = new ArrayList<>();
        }

        List<Document> filtered = new ArrayList<>();
        String low = criteria.toLowerCase();

        for (Document d : base) {
            if (d.getTitle().toLowerCase().contains(low)) {
                filtered.add(d);
            }
        }
        return filtered;
    }


    @Override
    public Document viewDocument(String docID) {
        checkLoggedIn();

        int count = 0;
        Document found = null;

        for (Document d : documents) {
            if (d.getDocID().equals(docID)) {
                count++;
                found = d;
            }
        }

        if (count != 1) {  //
            throw new PreconditionViolationException("Document not found or duplicate id");
        }

        return found;
    }


    @Override
    public List<String> accessExternalData(String sourceName) {
        checkLoggedIn();

        ExternalDataSource src = null;
        for (ExternalDataSource s : externalSources) {
            if (s.getName().equals(sourceName)) {
                src = s;
                break;
            }
        }
        if (src == null) {
            throw new PreconditionViolationException("External source not registered");
        }

        if (!src.checkReachability()) {
            throw new PreconditionViolationException("External data source not available");
        }

        try {

            return src.fetchData("keyword");
        } catch (IOException e) {
            throw new ConstraintViolationException("Error while fetching external data");
        }
    }


    @Override
    public void updateNotification(String userID) {
        checkLoggedIn();

        User receiver = null;
        for (User u : users) {
            if (u.getUserID().equals(userID)) {
                receiver = u;
                break;
            }
        }
        if (receiver == null) {
            throw new PreconditionViolationException("User for notification not found");
        }

        Notification notification =
                new Notification(receiver, "Documents or data have been updated.");

        notificationSubject.notifyObservers(notification);
    }
}
