import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OnlineBookstoreApp extends JFrame {
    private Inventory inventory;
    private ShoppingCart cart;

    private JComboBox<String> categoryComboBox;
    private DefaultListModel<Book> inventoryModel;
    private DefaultListModel<Book> cartModel;
    private JList<Book> inventoryList;
    private JList<Book> cartList;
    private JLabel cartTotalLabel;

    private static final String INVENTORY_FILE = "inventory.dat";
    private static final String ORDERS_FILE = "orders.txt";

    // High Contrast Custom Colors (Dark Theme)
    private final Color bgColor = new Color(30, 30, 30);
    private final Color fgColor = new Color(240, 240, 240);
    private final Color panelColor = new Color(45, 45, 48);
    private final Color buttonColor = new Color(70, 130, 180); // Steel blue
    private final Color selectionBgColor = new Color(100, 149, 237); // Cornflower blue
    private final Font customFont = new Font("Segoe UI", Font.BOLD, 15);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 16);

    public OnlineBookstoreApp() {
        super("Modern Online Bookstore");
        cart = new ShoppingCart();
        
        loadInventory();
        setupGUI();
        populateCategories();
        refreshCart();
    }

    private void loadInventory() {
        File file = new File(INVENTORY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                inventory = (Inventory) ois.readObject();
            } catch (Exception e) {
                setupData();
                saveInventory();
            }
        } else {
            setupData();
            saveInventory();
        }
    }

    private void saveInventory() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOrderToFile(ShoppingCart currentCart) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDERS_FILE, true))) {
            writer.println("======================================");
            writer.println("Order Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Items Purchased:");
            for (Book book : currentCart.getBooks()) {
                writer.println("- " + book.getTitle() + " (" + book.getCategory().getName() + ") : $" + String.format("%.2f", book.getPrice()));
            }
            writer.println("Total Paid: $" + String.format("%.2f", currentCart.getTotalAmount()));
            writer.println("======================================\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupData() {
        inventory = new Inventory();
        
        Category fiction = new Category("Fiction");
        fiction.addBook(new Book("The Great Gatsby", 10.99, fiction));
        fiction.addBook(new Book("1984", 14.50, fiction));
        fiction.addBook(new Book("Dune", 20.00, fiction));

        Category science = new Category("Science");
        science.addBook(new Book("A Brief History of Time", 18.99, science));
        science.addBook(new Book("Cosmos", 22.50, science));
        science.addBook(new Book("The Selfish Gene", 15.00, science));

        Category philosophy = new Category("Philosophy");
        philosophy.addBook(new Book("Meditations", 9.99, philosophy));
        philosophy.addBook(new Book("Beyond Good and Evil", 12.50, philosophy));
        philosophy.addBook(new Book("The Republic", 11.00, philosophy));

        Category psychology = new Category("Psychology");
        psychology.addBook(new Book("Thinking, Fast and Slow", 16.00, psychology));
        psychology.addBook(new Book("Man's Search for Meaning", 14.00, psychology));

        Category dictionary = new Category("Dictionary");
        dictionary.addBook(new Book("Oxford English Dictionary", 35.00, dictionary));
        dictionary.addBook(new Book("Merriam-Webster", 25.00, dictionary));

        Category history = new Category("History");
        history.addBook(new Book("Sapiens", 21.00, history));
        history.addBook(new Book("Guns, Germs, and Steel", 19.50, history));

        inventory.addCategory(fiction);
        inventory.addCategory(science);
        inventory.addCategory(philosophy);
        inventory.addCategory(psychology);
        inventory.addCategory(dictionary);
        inventory.addCategory(history);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(buttonColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor, 2),
                new EmptyBorder(10, 20, 10, 20)
        ));
    }

    private TitledBorder createCustomTitledBorder(String title) {
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(buttonColor, 2), title);
        border.setTitleColor(new Color(255, 215, 0)); // Gold color for titles
        border.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        return border;
    }

    private void setupGUI() {
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(bgColor);

        // Header
        JLabel headerLabel = new JLabel("Welcome to the Modern Online Bookstore", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(255, 215, 0)); // Gold title
        headerLabel.setBorder(new EmptyBorder(20, 10, 10, 10));
        add(headerLabel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // --- Inventory Panel (Left) ---
        JPanel inventoryContainer = new JPanel(new BorderLayout(0, 10));
        inventoryContainer.setBackground(bgColor);

        // Category Dropdown
        JPanel categoryPanel = new JPanel(new BorderLayout(10, 0));
        categoryPanel.setBackground(bgColor);
        JLabel categoryLabel = new JLabel("Select Genre:");
        categoryLabel.setFont(boldFont);
        categoryLabel.setForeground(fgColor);
        categoryComboBox = new JComboBox<>();
        categoryComboBox.setFont(customFont);
        categoryComboBox.setBackground(panelColor);
        categoryComboBox.setForeground(Color.WHITE);
        categoryComboBox.addActionListener(e -> refreshInventoryList());
        
        categoryPanel.add(categoryLabel, BorderLayout.WEST);
        categoryPanel.add(categoryComboBox, BorderLayout.CENTER);
        inventoryContainer.add(categoryPanel, BorderLayout.NORTH);

        inventoryModel = new DefaultListModel<>();
        inventoryList = new JList<>(inventoryModel);
        inventoryList.setBackground(panelColor);
        inventoryList.setFont(customFont);
        inventoryList.setSelectionBackground(selectionBgColor);
        inventoryList.setSelectionForeground(Color.WHITE);
        inventoryList.setCellRenderer(new BookListCellRenderer());
        
        JScrollPane inventoryScroll = new JScrollPane(inventoryList);
        inventoryScroll.setBorder(createCustomTitledBorder("Available Books"));
        inventoryScroll.getViewport().setBackground(panelColor);
        inventoryContainer.add(inventoryScroll, BorderLayout.CENTER);
        
        mainPanel.add(inventoryContainer);
        
        // --- Cart Panel (Right) ---
        JPanel cartContainer = new JPanel(new BorderLayout(0, 10));
        cartContainer.setBackground(bgColor);
        cartModel = new DefaultListModel<>();
        cartList = new JList<>(cartModel);
        cartList.setBackground(panelColor);
        cartList.setFont(customFont);
        cartList.setSelectionBackground(selectionBgColor);
        cartList.setSelectionForeground(Color.WHITE);
        cartList.setCellRenderer(new BookListCellRenderer());
        
        JScrollPane cartScroll = new JScrollPane(cartList);
        cartScroll.setBorder(createCustomTitledBorder("Your Shopping Cart"));
        cartScroll.getViewport().setBackground(panelColor);
        cartContainer.add(cartScroll, BorderLayout.CENTER);
        
        cartTotalLabel = new JLabel("Total: $0.00");
        cartTotalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cartTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cartTotalLabel.setForeground(new Color(152, 251, 152)); // Light green for money
        cartTotalLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cartContainer.add(cartTotalLabel, BorderLayout.SOUTH);
        
        mainPanel.add(cartContainer);
        add(mainPanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));

        JButton btnAddCart = new JButton("Add Selected to Cart");
        JButton btnRemoveCart = new JButton("Remove Selected");
        JButton btnClearCart = new JButton("Clear Cart");
        JButton btnCheckout = new JButton("Checkout");

        styleButton(btnAddCart);
        styleButton(btnRemoveCart);
        styleButton(btnClearCart);
        styleButton(btnCheckout);
        
        btnCheckout.setBackground(new Color(40, 160, 80)); // Vibrant green checkout

        buttonPanel.add(btnAddCart);
        buttonPanel.add(btnRemoveCart);
        buttonPanel.add(btnClearCart);
        buttonPanel.add(btnCheckout);

        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnAddCart.addActionListener(e -> {
            Book selected = inventoryList.getSelectedValue();
            if (selected != null) {
                cart.addBook(selected);
                refreshCart();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book from the inventory.");
            }
        });

        btnRemoveCart.addActionListener(e -> {
            Book selected = cartList.getSelectedValue();
            if (selected != null) {
                cart.removeBook(selected);
                refreshCart();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book from the cart to remove.");
            }
        });

        btnClearCart.addActionListener(e -> {
            cart.clearCart();
            refreshCart();
        });

        btnCheckout.addActionListener(e -> {
            if (cart.getBooks().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }
            
            double total = cart.getTotalAmount();
            saveOrderToFile(cart);
            
            JOptionPane.showMessageDialog(this, String.format("Checkout successful!\nYou paid: $%.2f\nReceipt saved to orders.txt", total), 
                                          "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
            cart.clearCart();
            refreshCart();
        });
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveInventory();
            }
        });
    }

    private void populateCategories() {
        categoryComboBox.addItem("All Categories");
        for (Category category : inventory.getCategories()) {
            categoryComboBox.addItem(category.getName());
        }
        refreshInventoryList();
    }

    private void refreshInventoryList() {
        inventoryModel.clear();
        String selected = (String) categoryComboBox.getSelectedItem();
        
        for (Category category : inventory.getCategories()) {
            if ("All Categories".equals(selected) || category.getName().equals(selected)) {
                for (Book book : category.getBooks()) {
                    inventoryModel.addElement(book);
                }
            }
        }
    }

    private void refreshCart() {
        cartModel.clear();
        for (Book book : cart.getBooks()) {
            cartModel.addElement(book);
        }
        cartTotalLabel.setText(String.format("Total: $%.2f", cart.getTotalAmount()));
    }

    // Custom Cell Renderer to add colors based on Genre
    class BookListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Book) {
                Book book = (Book) value;
                String categoryName = book.getCategory().getName();
                
                // Keep it white if selected for readability, otherwise color code by genre
                if (!isSelected) {
                    switch (categoryName) {
                        case "Fiction": c.setForeground(new Color(255, 160, 122)); break; // Light Salmon
                        case "Science": c.setForeground(new Color(135, 206, 250)); break; // Light Sky Blue
                        case "Philosophy": c.setForeground(new Color(221, 160, 221)); break; // Plum
                        case "Psychology": c.setForeground(new Color(255, 182, 193)); break; // Light Pink
                        case "Dictionary": c.setForeground(new Color(240, 230, 140)); break; // Khaki
                        case "History": c.setForeground(new Color(255, 218, 185)); break; // Peach Puff
                        default: c.setForeground(fgColor);
                    }
                } else {
                    c.setForeground(Color.WHITE);
                }
            }
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            OnlineBookstoreApp app = new OnlineBookstoreApp();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}
