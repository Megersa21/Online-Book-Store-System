public class ConfirmedState implements OrderState {
    @Override
    public void nextState(Order order) {
        order.setState(new ShippedState());
    }

    @Override
    public void printStatus() {
        System.out.println("Order is Confirmed.");
    }
}
