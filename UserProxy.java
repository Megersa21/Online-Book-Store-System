public class UserProxy implements User {
    private RealUser realUser;
    private boolean isLoggedIn;

    public UserProxy(String username, boolean isLoggedIn) {
        this.realUser = new RealUser(username);
        this.isLoggedIn = isLoggedIn;
    }

    @Override
    public void placeOrder(Order order) {
        if (isLoggedIn) {
            realUser.placeOrder(order);
        } else {
            System.out.println("Error: User " + realUser.getUsername() + " must be logged in to place an order.");
        }
    }
    
    public void login() {
        this.isLoggedIn = true;
        System.out.println(realUser.getUsername() + " logged in successfully.");
    }
}
