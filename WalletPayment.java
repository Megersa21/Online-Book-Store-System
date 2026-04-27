public class WalletPayment extends PaymentHandler {
    private double balance;

    public WalletPayment(double balance) {
        this.balance = balance;
    }

    @Override
    public void handlePayment(double amount) {
        if (balance >= amount) {
            System.out.println("Paid $" + amount + " using Wallet.");
            balance -= amount;
        } else if (nextHandler != null) {
            System.out.println("Insufficient Wallet balance. Checking next payment method...");
            nextHandler.handlePayment(amount);
        } else {
            System.out.println("Payment failed. Insufficient funds across all methods.");
        }
    }
}
