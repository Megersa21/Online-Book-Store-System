import java.util.HashMap;
import java.util.Map;

public class BookCoverFactory {
    private static final Map<String, BookCover> covers = new HashMap<>();

    public static BookCover getCover(String imageName) {
        if (!covers.containsKey(imageName)) {
            covers.put(imageName, new ConcreteBookCover(imageName));
        }
        return covers.get(imageName);
    }
}
