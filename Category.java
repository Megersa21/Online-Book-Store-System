import java.util.ArrayList;
import java.util.List;

public class Category implements BookComponent {
    private String name;
    private List<BookComponent> components = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void addComponent(BookComponent component) {
        components.add(component);
    }

    public void removeComponent(BookComponent component) {
        components.remove(component);
    }

    @Override
    public void displayInfo() {
        System.out.println("Category: " + name);
        for (BookComponent component : components) {
            component.displayInfo();
        }
    }
}
