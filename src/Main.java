import java.util.List;

public class Main {

    public static void main(String[] args) {

        HkInformationSystem system = new HkInformationSystem();


        Employee emp = new Employee("emp_01", "pass_321");
        Client client = new Client("cli_01", "pass_711");

        system.addUser(emp);
        system.addUser(client);


        Document d1 = new Document("doc1", "H&K basics", "This is a H&K document");
        Document d2 = new Document("doc2", "H&K and PR notes", "Notes about H&K and PR");

        system.addDocument(d1);
        system.addDocument(d2);


        system.logIn("emp_01", "pass_321");


        List<Document> results = system.searchInformation("H&K");
        System.out.println("Search results size: " + results.size());


        Document viewed = system.viewDocument("doc1");
        System.out.println("Viewed document title: " + viewed.getTitle());


        system.updateNotification("emp_01");
    }
}
