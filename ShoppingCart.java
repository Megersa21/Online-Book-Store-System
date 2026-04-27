import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private Mediator mediator;
    private List<Book> books = new ArrayList<>();

    public ShoppingCart(Mediator mediator) {
        this.mediator = mediator;
    }
    
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void addBook(Book book) {
        books.add(book);
        System.out.println(book.getTitle() + " added to cart.");
    }
    
    public void clearCart() {
        books.clear();
        System.out.println("Shopping cart cleared.");
    }

    public void checkout() {
        System.out.println("ShoppingCart: Initiating checkout...");
        mediator.notify(this, "Checkout");
    }
    
    public double getTotalAmount() {
        double total = 0;
        for (Book book : books) {
            total += book.getPrice();
        }
        return total;
    }
    
    public CartMemento save() {
        return new CartMemento(books);
    }

    public void restore(CartMemento memento) {
        this.books = memento.getState();
        System.out.println("Cart restored from memento.");
    }
    
    public void showCart() {
        System.out.println("--- Current Cart ---");
        if (books.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (Book book : books) {
                System.out.println("- " + book.getTitle() + " ($" + book.getPrice() + ")");
            }
            System.out.println("Total: $" + getTotalAmount());
        }
        System.out.println("--------------------");
    }
}
