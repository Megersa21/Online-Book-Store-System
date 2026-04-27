public class Inventory {
    private Mediator mediator;
    private Category rootCategory;

    public Inventory(Mediator mediator) {
        this.mediator = mediator;
        this.rootCategory = new Category("All Books");
    }

    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }
    
    public void addCategory(Category category) {
        rootCategory.addComponent(category);
    }

    public void displayInventory() {
        rootCategory.displayInfo();
    }
    
    public void checkAvailability() {
        System.out.println("Inventory: Checking book availability...");
        mediator.notify(this, "InventoryChecked");
    }
}
