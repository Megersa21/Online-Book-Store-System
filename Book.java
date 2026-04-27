public class Book implements BookComponent, Cloneable {
    private String title;
    private double price;
    private String coverImageName;

    public Book(String title, double price, String coverImageName) {
        this.title = title;
        this.price = price;
        this.coverImageName = coverImageName;
    }

    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public String getCoverImageName() { return coverImageName; }

    @Override
    public void displayInfo() {
        System.out.println("Book: " + title + " | Price: $" + price);
        BookCover cover = BookCoverFactory.getCover(coverImageName);
        cover.display(title);
    }

    @Override
    public Book clone() {
        try {
            Book cloned = (Book) super.clone();
            cloned.title = cloned.title + " (Special Edition)";
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
