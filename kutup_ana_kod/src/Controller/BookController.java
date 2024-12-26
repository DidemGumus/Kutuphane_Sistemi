package Controller;

import Model.BookModel;
import Model.InventorySubject;
import Model.Book;
import Strategy.BookSorter;
import Strategy.SortByName;
import Strategy.SortByDate;


import java.util.List;

public class BookController {

    private final BookModel bookModel;
    private final InventorySubject inventorySubject; // Observer deseni için InventorySubject

    // Constructor
    public BookController(InventorySubject inventorySubject) {
        this.bookModel = new BookModel();
        this.inventorySubject = inventorySubject; // InventorySubject'i bağla
    }

    public int getBookIdByName(String bookName) {
        return bookModel.getBookIdByName(bookName);
    }

    // Öğrenciler için uygun kitapları listelenir(Lost olanlar listelenmez ya da ödünç alınmış olanlar)
    public List<String> getAvailableBooksForStudent(int userId) {
        return bookModel.getAvailableBooksForStudent(userId);
    }

    public List<String> getBorrowedBooksForStudent(int userId) {
        return bookModel.getBorrowedBooksForStudent(userId);
    }


    // Personel için tüm kitapları listelenir
    public List<String> getAllBooksForPersonnel() {
        return bookModel.getAllBooksForPersonnel();
    }

    //kitap durumunu veritabanında günceller kayıp mı ödünç mü alınmış gibi
    public void updateBookState(int bookId, String newState) {
        bookModel.updateBookStatus(bookId, newState);
    }

    // Kitap eklenir
    public void addBook(String bookName, String author, String genre, int publicationYear, String status, String location) {
        boolean isAdded = bookModel.addBook(bookName, author, genre, publicationYear, status, location);
        if (isAdded) {
            inventorySubject.notifyObservers("Yeni kitap eklendi: " + bookName);
            System.out.println("Kitap başarıyla eklendi.");
        } else {
            System.err.println("Hata: Kitap eklenirken bir sorun oluştu.");
        }
    }

    // Kitap güncellenir ismi vs
    public void updateBook(int bookId, String bookName, String author, String genre, int publicationYear, String status, String location) {
        boolean isUpdated = bookModel.updateBook(bookId, bookName, author, genre, publicationYear, status, location);
        if (isUpdated) {
            inventorySubject.notifyObservers("Kitap güncellendi: " + bookName);
            System.out.println("Kitap başarıyla güncellendi.");
        } else {
            System.err.println("Hata: Kitap güncellenirken bir sorun oluştu.");
        }
    }

    // Kitap silinir
    public void deleteBook(int bookId) {
        boolean isDeleted = bookModel.deleteBook(bookId);
        if (isDeleted) {
            inventorySubject.notifyObservers("Kitap silindi. ID: " + bookId);
            System.out.println("Kitap başarıyla silindi.");
        } else {
            System.err.println("Hata: Kitap silinirken bir sorun oluştu.");
        }
    }

    public void manageBookState(Book book, String action) {
        switch (action.toLowerCase()) {
            case "borrow":
                book.borrow();
                break;
            case "return":
                book.returnBook();
                break;
            case "lost":
                book.reportLost();
                break;
            default:
                System.out.println("Geçersiz işlem.");
        }
    }

    // Detaylı kitap arama (ana kitap tablosundan, author ve genre filtresi ile)
    public List<String> searchBooksWithFilters(String keyword, String authorFilter, String genreFilter) {
        return bookModel.searchBooksWithFilters(keyword, authorFilter, genreFilter);
    }

    // Popüler kitapları listeleme
    public List<String> getPopularBooks() {
        return bookModel.getPopularBooks();
    }
    // kitapları Strategy kullanarak sıralayacak bir metot
    public List<String> getSortedBooks(List<String> books, BookSorter sorter) {
        return sorter.sort(books);
    }

}

