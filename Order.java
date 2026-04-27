public class Order {
    private OrderState state;
    private Mediator mediator;

    public Order(Mediator mediator) {
        this.state = new PendingState();
        this.mediator = mediator;
    }
    
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void processOrder() {
        state.printStatus();
        mediator.notify(this, "OrderPlaced");
    }
    
    public void nextState() {
        state.nextState(this);
        state.printStatus();
    }
    
    public OrderState getState() {
        return state;
    }
}
