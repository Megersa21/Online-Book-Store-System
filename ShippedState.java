public class ShippedState implements OrderState {
    @Override
    public void nextState(Order order) {
        order.setState(new DeliveredState());
    }

    @Override
    public void printStatus() {
        System.out.println("Order has been Shipped.");
    }
}
