package Controller;

import Model.TransactionModel;
import java.util.List;
import Model.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class TransactionController {

    private final TransactionModel transactionModel;

    // Constructor
    public TransactionController() {
        this.transactionModel = new TransactionModel();
    }
    public int getBookIdByName(String bookName) {
        return transactionModel.getBookIdByName(bookName);
    }

    // Yeni transaction kaydı ekleyen metod
    public void recordTransaction(int bookId, int userId, String status) {
        String query = "INSERT INTO transactions (book_id, user_id, status, borrow_date) VALUES (?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            ps.setString(3, status);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction başarıyla kaydedildi.");
            } else {
                System.out.println("Transaction kaydedilemedi.");
            }
        } catch (SQLException e) {
            System.err.println("Hata: Transaction kaydedilemedi.");
            e.printStackTrace();
        }
    }
    // Transactions tablosunda durumu güncelleyen yeni metod
    public void updateTransactionStatus(int bookId, int userId, String status) {
        String query = "UPDATE transactions SET status = ? WHERE book_id = ? AND user_id = ? AND status = 'Borrowed'";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setInt(2, bookId);
            ps.setInt(3, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction durumu güncellendi: " + status);
            } else {
                System.out.println("Hata: Transaction durumu güncellenemedi.");
            }
        } catch (SQLException e) {
            System.err.println("Hata: Transaction durumu güncellenemedi.");
            e.printStackTrace();
        }
    }


    // Kitap Ödünç Alma İşlemi
    public void borrowBook(int bookId, int userId) {
        boolean isBorrowed = transactionModel.addTransaction(bookId, userId);
        if (isBorrowed) {
            System.out.println("Kitap başarıyla ödünç alındı.");
        } else {
            System.err.println("Hata: Kitap ödünç alma işlemi başarısız.");
        }
    }

    // Kitap İade İşlemi
    public void returnBook(int bookId) {
        boolean isReturned = transactionModel.returnBook(bookId);
        if (isReturned) {
            System.out.println("Kitap başarıyla iade edildi.");
        } else {
            System.err.println("Hata: Kitap iade işlemi başarısız.");
        }
    }

    // Kullanıcının Ödünç Aldığı Kitapları Listeleme
    public List<String> listBorrowedBooks(int userId) {
        List<String> borrowedBooks = transactionModel.getBorrowedBooksByUser(userId);
        if (borrowedBooks == null || borrowedBooks.isEmpty()) {
            System.out.println("Bu kullanıcıya ait ödünç alınmış kitap bulunamadı.");
        } else {
            System.out.println("Kullanıcının Ödünç Aldığı Kitaplar:");
            borrowedBooks.forEach(book -> System.out.println("- " + book));
        }
        return borrowedBooks;
    }
}
