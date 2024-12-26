package Model;

public class AvailableState implements BookState {

    @Override
    public void borrowBook(Book book) {
        System.out.println("Kitap ödünç alındı: " + book.getName());
        book.setState(new BorrowedState());
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("Kitap zaten rafta, iade edilemez.");
    }

    @Override
    public void reportLost(Book book) {
        System.out.println("Kitap kayıp olarak bildirildi: " + book.getName());
        book.setState(new LostState());
    }
}
