package Model;

public interface BookState {
    void borrowBook(Book book);     // Ödünç alma işlemi
    void returnBook(Book book);     // İade işlemi
    void reportLost(Book book);     // Kayıp bildirme işlemi
}
