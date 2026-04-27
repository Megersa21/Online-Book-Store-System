public class CreditCardPayment extends PaymentHandler {
    private double limit;

    public CreditCardPayment(double limit) {
        this.limit = limit;
    }

    @Override
    public void handlePayment(double amount) {
        if (limit >= amount) {
            System.out.println("Paid $" + amount + " using Credit Card.");
            limit -= amount;
        } else if (nextHandler != null) {
            System.out.println("Credit Card limit exceeded. Checking next payment method...");
            nextHandler.handlePayment(amount);
        } else {
            System.out.println("Payment failed. Insufficient funds across all methods.");
        }
    }
}
