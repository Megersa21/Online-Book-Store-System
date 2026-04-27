import java.util.ArrayList;
import java.util.List;

public class CartMemento {
    private final List<Book> state;

    public CartMemento(List<Book> state) {
        this.state = new ArrayList<>(state);
    }

    public List<Book> getState() {
        return state;
    }
}
