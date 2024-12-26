package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    public int getBookIdByName(String bookName) {
        String query = "SELECT book_id FROM Books WHERE book_name = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, bookName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("book_id");
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kitap adı eşleşmesi başarısız. Kitap Adı: " + bookName);
            e.printStackTrace();
        }
        return -1; // Kitap bulunamazsa -1 döndür
    }




    // Ödünç alma işlemi
    public boolean addTransaction(int bookId, int userId) {
        String query = "INSERT INTO Transactions (book_id, user_id, status) VALUES (?, ?, 'Borrowed')";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Ödünç alma işlemi başarısız. Kitap ID: " + bookId + ", Kullanıcı ID: " + userId);
            e.printStackTrace();
        }
        return false;
    }

    // İade işlemi
    public boolean returnBook(int bookId) {
        String query = "UPDATE Transactions SET status = 'Returned' WHERE book_id = ? AND status = 'Borrowed'";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookId);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                return true;
            } else {
                System.err.println("Hata: İade edilecek kayıt bulunamadı. Kitap ID: " + bookId);
            }
        } catch (SQLException e) {
            System.err.println("Hata: İade işlemi başarısız. Kitap ID: " + bookId);
            e.printStackTrace();
        }
        return false;
    }




    // Kullanıcının ödünç aldığı kitapları listeleme
    public List<String> getBorrowedBooksByUser(int userId) {
        String query = "SELECT Books.book_id, Books.book_name " +
                "FROM Transactions " +
                "JOIN Books ON Transactions.book_id = Books.book_id " +
                "WHERE Transactions.user_id = ? AND Transactions.status = 'Borrowed'";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                String bookName = rs.getString("book_name");
                books.add(bookId + " - " + bookName); // ID ve ad birleştiriliyor
            }
        } catch (SQLException e) {
            System.err.println("Hata: Ödünç alınmış kitaplar listelenemedi. Kullanıcı ID: " + userId);
            e.printStackTrace();
        }
        return books;
    }
}
