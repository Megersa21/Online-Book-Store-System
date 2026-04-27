public abstract class PaymentHandler {
    protected PaymentHandler nextHandler;

    public void setNext(PaymentHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handlePayment(double amount);
}
