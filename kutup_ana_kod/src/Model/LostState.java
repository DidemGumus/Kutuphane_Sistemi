package Model;

public class LostState implements BookState {

    @Override
    public void borrowBook(Book book) {
        System.out.println("Kayıp kitap ödünç alınamaz.");
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("Kayıp kitap iade edilemez.");
    }

    @Override
    public void reportLost(Book book) {
        System.out.println("Kitap zaten kayıp olarak işaretlenmiş.");
    }
}
