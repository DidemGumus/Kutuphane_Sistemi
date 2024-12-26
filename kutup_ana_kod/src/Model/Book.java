package Model;

public class Book {
    private String name;
    private BookState state;

    public Book(String name) {
        this.name = name;
        this.state = new AvailableState(); // Varsayılan başlangıç durumu rafta (Available)
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public BookState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public void borrow() {
        state.borrowBook(this);
    }

    public void returnBook() {
        state.returnBook(this);
    }

    public void reportLost() {
        state.reportLost(this);
    }
}
