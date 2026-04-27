public class PendingState implements OrderState {
    @Override
    public void nextState(Order order) {
        order.setState(new ConfirmedState());
    }

    @Override
    public void printStatus() {
        System.out.println("Order is in Pending state.");
    }
}
