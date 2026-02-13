import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HkInformationSystemTest {

    private HkInformationSystem createTestSystem() {
        HkInformationSystem system = new HkInformationSystem();

        Employee emp = new Employee("emp_01", "pass007");
        system.addUser(emp);

        Document d1 = new Document("doc1", "H&K basics", "This is a H&K document");
        Document d2 = new Document("doc2", "H&K and PR notes", "Notes about H&K and PR");
        system.addDocument(d1);
        system.addDocument(d2);

        return system;
    }


    @Test
    public void testLogin_Success() {
        System.out.println("Test_1 (Login success): employee logs in with correct id and password.");
        HkInformationSystem system = createTestSystem();

        try {
            User loggedIn = system.logIn("emp_01", "pass007");

            assertNotNull(loggedIn);
            assertEquals("emp_01", loggedIn.getUserID());
            assertTrue(loggedIn.isAuthenticated());

            System.out.println("[PASS] Employee emp_01 logged in successfully.");
        } catch (Exception e) {
            fail("Did not expect exception during successful login");
        }
    }


    @Test
    public void testLogin_EmptyPassword() {
        System.out.println("Test_2 (Login empty password): system should reject empty password.");
        HkInformationSystem system = createTestSystem();

        try {
            system.logIn("emp_01", "");
            System.out.println("[FAIL] System allowed login with empty password.");
            fail("Expected PreconditionViolationException");
        } catch (PreconditionViolationException e) {
            System.out.println("[PASS] Caught expected error: " + e.getMessage());
        }
    }


    @Test
    public void testSearch_WithResults() {
        System.out.println("Test_3 (Search with results): keyword appears in document.");
        HkInformationSystem system = createTestSystem();
        system.logIn("emp_01", "pass007");

        try {
            var results = system.searchInformation("H&K");

            assertNotNull(results);
            assertFalse(results.isEmpty());
            assertEquals("doc1", results.get(0).getDocID());

            System.out.println("[PASS] Search returned expected document doc1.");
        } catch (Exception e) {
            fail("Did not expect exception during valid search");
        }
    }


    @Test
    public void testSearch_ShortKeyword() {
        System.out.println("Test_4 (Search short keyword): system should reject keyword shorter than 3 characters.");
        HkInformationSystem system = createTestSystem();
        system.logIn("emp_01", "pass007");

        try {
            system.searchInformation("ab");
            System.out.println("[FAIL] System allowed search with 2-letter keyword.");
            fail("Expected PreconditionViolationException for short keyword");
        } catch (PreconditionViolationException e) {
            System.out.println("[PASS] Caught expected error: " + e.getMessage());
        }
    }

    @Test
    public void testViewDocument_ValidId() {
        System.out.println("Test_5 (View document valid id): employee opens an existing document.");
        HkInformationSystem system = createTestSystem();
        system.logIn("emp_01", "pass007");

        try {
            Document doc = system.viewDocument("doc1");

            assertNotNull(doc);
            assertEquals("doc1", doc.getDocID());

            System.out.println("[PASS] viewDocument returned the correct document doc1.");
        } catch (Exception e) {
            fail("Did not expect exception for valid document ID");
        }
    }

    @Test
    public void testViewDocument_InvalidId() {
        System.out.println("Test_6 (View document invalid id): system should not return missing document.");
        HkInformationSystem system = createTestSystem();
        system.logIn("emp_01", "pass007");

        try {
            system.viewDocument("wrongId");
            System.out.println("[FAIL] System returned document for wrongId.");
            fail("Expected PreconditionViolationException for wrong docID");
        } catch (PreconditionViolationException e) {
            System.out.println("[PASS] Caught expected error: " + e.getMessage());
        }
    }
}
