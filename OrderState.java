public interface OrderState {
    void nextState(Order order);
    void printStatus();
}
