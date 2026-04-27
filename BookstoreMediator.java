public class BookstoreMediator implements Mediator {
    private ShoppingCart cart;
    private Inventory inventory;
    private PaymentSystem paymentSystem;
    private Order currentOrder;

    public void setColleagues(ShoppingCart cart, Inventory inventory, PaymentSystem paymentSystem) {
        this.cart = cart;
        this.inventory = inventory;
        this.paymentSystem = paymentSystem;
    }
    
    public void setCurrentOrder(Order order) {
        this.currentOrder = order;
    }

    @Override
    public void notify(Object sender, String event) {
        if (sender instanceof Order && event.equals("OrderPlaced")) {
            cart.checkout();
        } else if (sender instanceof ShoppingCart && event.equals("Checkout")) {
            inventory.checkAvailability();
        } else if (sender instanceof Inventory && event.equals("InventoryChecked")) {
            paymentSystem.processPayment(cart.getTotalAmount());
        } else if (sender instanceof PaymentSystem && event.equals("PaymentSuccess")) {
            if (currentOrder != null) {
                currentOrder.nextState(); // Move from Pending to Confirmed
                cart.clearCart();
            }
        }
    }
}
