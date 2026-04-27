public class ConcreteBookCover implements BookCover {
    private String imageName; // Intrinsic state

    public ConcreteBookCover(String imageName) {
        this.imageName = imageName;
        System.out.println("Loading image from disk: " + imageName);
    }

    @Override
    public void display(String bookTitle) {
        System.out.println("Displaying cover '" + imageName + "' for book '" + bookTitle + "'");
    }
}
