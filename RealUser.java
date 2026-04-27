public class RealUser implements User {
    private String username;

    public RealUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void placeOrder(Order order) {
        System.out.println(username + " is placing the order...");
        order.processOrder();
    }
}
