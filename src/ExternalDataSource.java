import java.io.IOException;
import java.util.List;

public interface ExternalDataSource {
    String getName();
    boolean checkReachability();
    List<String> fetchData(String keyword) throws IOException;
}
