public class PayPalPayment extends PaymentHandler {
    private double balance;

    public PayPalPayment(double balance) {
        this.balance = balance;
    }

    @Override
    public void handlePayment(double amount) {
        if (balance >= amount) {
            System.out.println("Paid $" + amount + " using PayPal.");
            balance -= amount;
        } else if (nextHandler != null) {
            System.out.println("Insufficient PayPal balance. Checking next payment method...");
            nextHandler.handlePayment(amount);
        } else {
            System.out.println("Payment failed. Insufficient funds across all methods.");
        }
    }
}
