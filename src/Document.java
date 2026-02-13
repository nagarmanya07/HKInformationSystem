public class Document {

    private final String docID;
    private final String title;
    private final String content;

    public Document(String docID, String title, String content) {
        if (docID == null || docID.trim().isEmpty()) {
            throw new ConstraintViolationException("docID cannot be empty");
        }
        this.docID = docID;
        this.title = title;
        this.content = content;
    }

    public String getDocID() {
        return docID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
