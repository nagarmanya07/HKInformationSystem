import java.util.List;

public interface HkInformationInterface {

    User logIn(String userID, String password);

    List<Document> searchInformation(String keyword);

    List<Document> filterResults(String criteria);

    Document viewDocument(String docID);

    List<String> accessExternalData(String sourceName);

    void updateNotification(String userID);
}
