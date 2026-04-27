public class PaymentSystem {
    private Mediator mediator;
    private PaymentHandler paymentChain;

    public PaymentSystem(Mediator mediator, PaymentHandler paymentChain) {
        this.mediator = mediator;
        this.paymentChain = paymentChain;
    }
    
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void processPayment(double amount) {
        System.out.println("PaymentSystem: Processing payment...");
        paymentChain.handlePayment(amount);
        mediator.notify(this, "PaymentSuccess");
    }
}
