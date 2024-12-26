package Model;

public class BorrowedState implements BookState {

    @Override
    public void borrowBook(Book book) {
        System.out.println("Kitap zaten ödünç alınmış.");
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("Kitap iade edildi: " + book.getName());
        book.setState(new AvailableState());
    }

    @Override
    public void reportLost(Book book) {
        System.out.println("Kitap kayıp olarak bildirildi: " + book.getName());
        book.setState(new LostState());
    }
}
