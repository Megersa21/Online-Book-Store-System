public class DeliveredState implements OrderState {
    @Override
    public void nextState(Order order) {
        System.out.println("Order is already delivered.");
    }

    @Override
    public void printStatus() {
        System.out.println("Order is Delivered.");
    }
}
