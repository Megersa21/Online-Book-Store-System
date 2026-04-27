import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

public class OnlineBookstoreApp extends JFrame {
    private JTextArea outputArea;
    private BookstoreMediator mediator;
    private Inventory inventory;
    private ShoppingCart cart;
    private PaymentSystem paymentSystem;
    private UserProxy user;
    private CartMemento savedCart;
    private Book origBook, specialEdition, javaBook;

    public OnlineBookstoreApp() {
        super("Online Bookstore Management System");
        setupBackend();
        setupGUI();
        redirectSystemOut();
    }

    private void setupBackend() {
        // Setup Chain of Responsibility
        PaymentHandler wallet = new WalletPayment(50.0);
        PaymentHandler card = new CreditCardPayment(200.0);
        PaymentHandler paypal = new PayPalPayment(500.0);
        wallet.setNext(card);
        card.setNext(paypal);
        
        // Setup Mediator and Colleagues
        mediator = new BookstoreMediator();
        inventory = new Inventory(mediator);
        cart = new ShoppingCart(mediator);
        paymentSystem = new PaymentSystem(mediator, wallet);
        
        mediator.setColleagues(cart, inventory, paymentSystem);
        
        // Setup Composite (Inventory) and Prototype
        Category fiction = new Category("Fiction");
        Category sciFi = new Category("Sci-Fi");
        fiction.addComponent(sciFi);
        
        origBook = new Book("Dune", 25.0, "dune_cover.jpg");
        specialEdition = origBook.clone(); // Prototype pattern
        
        sciFi.addComponent(origBook);
        sciFi.addComponent(specialEdition);
        
        Category programming = new Category("Programming");
        javaBook = new Book("Effective Java", 45.0, "java_cover.jpg");
        programming.addComponent(javaBook);
        
        inventory.addCategory(fiction);
        inventory.addCategory(programming);
        
        // User Setup (Proxy)
        user = new UserProxy("Alice", false);
    }

    private void setupGUI() {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBackground(new Color(30, 30, 30));
        outputArea.setForeground(new Color(0, 255, 0)); // Green text on dark background for cool terminal look
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnInventory = new JButton("1. View Inventory");
        JButton btnAddCart = new JButton("2. Add Book to Cart");
        JButton btnSaveCart = new JButton("3. Save Cart");
        JButton btnRestoreCart = new JButton("4. Restore Cart");
        JButton btnViewCart = new JButton("5. View Cart");
        JButton btnLogin = new JButton("6. Login (Proxy)");
        JButton btnCheckout = new JButton("7. Checkout");
        JButton btnClear = new JButton("Clear Output");

        buttonPanel.add(btnInventory);
        buttonPanel.add(btnAddCart);
        buttonPanel.add(btnSaveCart);
        buttonPanel.add(btnRestoreCart);
        buttonPanel.add(btnViewCart);
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnCheckout);
        buttonPanel.add(btnClear);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnInventory.addActionListener(e -> {
            System.out.println("\n--- Viewing Inventory (Composite, Flyweight) ---");
            inventory.displayInventory();
        });

        btnAddCart.addActionListener(e -> {
            String[] options = {"Dune ($25.0)", "Dune (Special Edition) ($25.0)", "Effective Java ($45.0)"};
            int choice = JOptionPane.showOptionDialog(this, "Select a book to add:", "Add Book",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            
            System.out.println("\n--- Adding to Cart ---");
            if (choice == 0) cart.addBook(origBook);
            else if (choice == 1) cart.addBook(specialEdition);
            else if (choice == 2) cart.addBook(javaBook);
        });

        btnSaveCart.addActionListener(e -> {
            System.out.println("\n--- Saving Cart (Memento) ---");
            savedCart = cart.save();
            System.out.println("Cart state saved.");
        });

        btnRestoreCart.addActionListener(e -> {
            System.out.println("\n--- Restoring Cart (Memento) ---");
            if (savedCart != null) {
                cart.restore(savedCart);
            } else {
                System.out.println("No saved cart state found.");
            }
        });

        btnViewCart.addActionListener(e -> {
            System.out.println("\n--- Viewing Cart ---");
            cart.showCart();
        });

        btnLogin.addActionListener(e -> {
            System.out.println("\n--- Login (Proxy) ---");
            user.login();
        });

        btnCheckout.addActionListener(e -> {
            System.out.println("\n--- Checkout (Proxy, State, Chain of Resp, Mediator) ---");
            if (cart.getTotalAmount() > 0) {
                Order newOrder = new Order(mediator);
                mediator.setCurrentOrder(newOrder);
                user.placeOrder(newOrder);
                
                if (newOrder.getState() instanceof ConfirmedState) {
                    System.out.println("\nAdmin action: Shipping the order...");
                    newOrder.nextState();
                    System.out.println("Admin action: Delivering the order...");
                    newOrder.nextState();
                }
            } else {
                System.out.println("Cart is empty. Please add items first.");
            }
        });

        btnClear.addActionListener(e -> outputArea.setText(""));
    }

    private void redirectSystemOut() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                outputArea.append(String.valueOf((char) b));
                outputArea.setCaretPosition(outputArea.getDocument().getLength());
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            OnlineBookstoreApp app = new OnlineBookstoreApp();
            app.setVisible(true);
            System.out.println("=== Online Bookstore Management System (GUI Version) ===");
            System.out.println("Welcome! Please select an option from the buttons below to interact with the system.");
            System.out.println("All outputs from the Design Patterns will be displayed here.");
        });
    }
}
