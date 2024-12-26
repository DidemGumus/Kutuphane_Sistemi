package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookModel {

    // Popüler kitapları Transactions tablosundan hesaplama
    public List<String> getPopularBooks() {
        String query = "SELECT b.book_id, b.book_name, COUNT(t.transaction_id) AS loan_count " +
                "FROM Books b " +
                "JOIN Transactions t ON b.book_id = t.book_id " +
                "WHERE t.status = 'Borrowed' OR t.status = 'Returned' " + // Tüm işlemleri dahil et
                "GROUP BY b.book_id, b.book_name " +
                "ORDER BY loan_count DESC " +
                "LIMIT 5";

        List<String> popularBooks = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String bookDetails = rs.getString("book_name") + " - Ödünç Sayısı: " + rs.getInt("loan_count");
                popularBooks.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Popüler kitaplar listelenemedi.");
            e.printStackTrace();
        }
        return popularBooks;
    }



    // Kitap arama (Books tablosunda)
    public List<String> searchBooks(String keyword, String authorFilter, String genreFilter) {
        String query = "SELECT book_name, author, genre, status, location " +
                "FROM Books " +
                "WHERE (book_name LIKE ? OR author LIKE ? OR genre LIKE ?) " +
                "AND (author = ? OR ? IS NULL) " +
                "AND (genre = ? OR ? IS NULL)";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            String likeKeyword = "%" + keyword + "%";
            ps.setString(1, likeKeyword); // Genel arama (book_name)
            ps.setString(2, likeKeyword); // Genel arama (author)
            ps.setString(3, likeKeyword); // Genel arama (genre)

            ps.setString(4, authorFilter); // Author filtresi
            ps.setString(5, authorFilter); // Author filtresi (NULL kontrolü)

            ps.setString(6, genreFilter); // Genre filtresi
            ps.setString(7, genreFilter); // Genre filtresi (NULL kontrolü)

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String bookDetails = rs.getString("book_name") + " - " +
                        rs.getString("author") + " (" +
                        rs.getString("genre") + ") - Durum: " +
                        rs.getString("status") + " - Konum: " +
                        rs.getString("location");
                books.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kitap arama işlemi başarısız.");
            e.printStackTrace();
        }

        return books;
    }

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
            System.err.println("Hata: Kitap adı ile kitap ID'si bulunamadı.");
            e.printStackTrace();
        }
        return -1; // Kitap bulunamazsa -1 döner
    }


    // Kitap ekleme
    public boolean addBook(String bookName, String author, String genre, int publicationYear, String status, String location) {
        String query = "INSERT INTO Books (book_name, author, genre, publication_year, status, location) VALUES (?, ?, ?, ?, ?, ?)";
        String searchKeywords = bookName + " " + author + " " + genre;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, bookName);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, publicationYear);
            ps.setString(5, status);
            ps.setString(6, location);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int bookId = generatedKeys.getInt(1);

                    // BookSearchIndex'e ekle
                    addBookSearchIndex(bookId, searchKeywords);
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Kitap eklenemedi.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean addBookSearchIndex(int bookId, String searchKeywords) {
        String query = "INSERT INTO BookSearchIndex (book_id, search_keywords) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookId);
            ps.setString(2, searchKeywords);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Kitap arama dizini eklenemedi.");
            e.printStackTrace();
        }
        return false;
    }


    // Öğrenciler için ödünç alınabilir kitapları listeleme
    public List<String> getAvailableBooksForStudent(int userId) {
        String query = "SELECT book_id, book_name, author, genre, publication_year, status, location " +
                "FROM Books " +
                "WHERE status = 'Available' " +
                "AND book_id NOT IN (SELECT book_id FROM Transactions WHERE user_id = ? AND status = 'Borrowed')";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bookDetails = rs.getInt("book_id") + " - " +
                        rs.getString("book_name") + " (" +
                        rs.getString("author") + ", " +
                        rs.getString("genre") + ", " +
                        rs.getInt("publication_year") + ", " +
                        rs.getString("status") + ", " +
                        rs.getString("location") + ")";
                books.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Uygun kitaplar listelenemedi.");
            e.printStackTrace();
        }

        return books;
    }

    //kullanıcının ödünç aldığı kitaplar
    public List<String> getBorrowedBooksForStudent(int userId) {
        String query = "SELECT b.book_id, b.book_name, b.author, b.genre, b.publication_year, b.status, b.location " +
                "FROM Books b " +
                "JOIN Transactions t ON b.book_id = t.book_id " +
                "WHERE t.user_id = ? AND t.status = 'Borrowed'";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bookDetails = rs.getInt("book_id") + " - " +
                        rs.getString("book_name") + " (" +
                        rs.getString("author") + ", " +
                        rs.getString("genre") + ", " +
                        rs.getInt("publication_year") + ", " +
                        rs.getString("status") + ", " +
                        rs.getString("location") + ")";
                books.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Ödünç alınmış kitaplar listelenemedi.");
            e.printStackTrace();
        }

        return books;
    }



    // Personel için tüm kitapları listeleme
    public List<String> getAllBooksForPersonnel() {
        String query = "SELECT book_id, book_name, author, genre, publication_year, status, location " +
                "FROM Books";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String bookDetails = rs.getInt("book_id") + " - " +
                        rs.getString("book_name") + " (" +
                        rs.getString("author") + ", " +
                        rs.getString("genre") + ", " +
                        rs.getInt("publication_year") + ", " +
                        rs.getString("status") + ", " +
                        rs.getString("location") + ")";
                books.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kitaplar listelenemedi.");
            e.printStackTrace();
        }

        return books;
    }
    public List<String> listBorrowedBooks(int userId) {
        String query = "SELECT b.book_id, b.book_name, b.author, b.genre " +
                "FROM Books b " +
                "JOIN Transactions t ON b.book_id = t.book_id " +
                "WHERE t.user_id = ? AND t.status = 'Borrowed'";
        List<String> borrowedBooks = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String bookDetails = rs.getInt("book_id") + " - " +
                        rs.getString("book_name") + " (" +
                        rs.getString("author") + ", " +
                        rs.getString("genre") + ")";
                borrowedBooks.add(bookDetails);
            }
        } catch (SQLException e) {
            System.err.println("Hata: Ödünç alınan kitaplar listelenemedi.");
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    public List<String> searchBooksWithFilters(String keyword, String author, String genre) {
        String query = "SELECT book_name, author, genre FROM Books " +
                "WHERE (book_name LIKE ? OR ? IS NULL) " +
                "AND (author LIKE ? OR ? IS NULL) " +
                "AND (genre LIKE ? OR ? IS NULL)";
        List<String> books = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, keyword == null ? null : "%" + keyword + "%");
            ps.setString(2, keyword);
            ps.setString(3, author == null ? null : "%" + author + "%");
            ps.setString(4, author);
            ps.setString(5, genre == null ? null : "%" + genre + "%");
            ps.setString(6, genre);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                books.add(rs.getString("book_name") + " - " +
                        rs.getString("author") + " (" +
                        rs.getString("genre") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kitap arama işlemi başarısız.");
            e.printStackTrace();
        }

        return books;
    }


    // Kitap güncelleme
    public boolean updateBook(int bookId, String bookName, String author, String genre, int publicationYear, String status, String location) {
        String query = "UPDATE Books SET book_name = ?, author = ?, genre = ?, publication_year = ?, status = ?, location = ? WHERE book_id = ?";
        String searchKeywords = bookName + " " + author + " " + genre;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, bookName);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, publicationYear);
            ps.setString(5, status);
            ps.setString(6, location);
            ps.setInt(7, bookId);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                String indexQuery = "UPDATE BookSearchIndex SET search_keywords = ? WHERE book_id = ?";
                try (PreparedStatement indexPs = connection.prepareStatement(indexQuery)) {
                    indexPs.setString(1, searchKeywords);
                    indexPs.setInt(2, bookId);
                    indexPs.executeUpdate();
                }
            }
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Kitap güncellenemedi.");
            e.printStackTrace();
        }
        return false;
    }

    // Kitap silme
    public boolean deleteBook(int bookId) {
        String deleteSearchIndexQuery = "DELETE FROM BookSearchIndex WHERE book_id = ?";
        String deleteBookQuery = "DELETE FROM Books WHERE book_id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement deleteSearchIndexPs = connection.prepareStatement(deleteSearchIndexQuery);
             PreparedStatement deleteBookPs = connection.prepareStatement(deleteBookQuery)) {

            deleteSearchIndexPs.setInt(1, bookId);
            deleteSearchIndexPs.executeUpdate();

            deleteBookPs.setInt(1, bookId);
            int rowsAffected = deleteBookPs.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Kitap silinemedi. Kitap ID: " + bookId);
            e.printStackTrace();
        }
        return false;
    }

    //kitap durumunu veritabanında günceller
    public void updateBookStatus(int bookId, String newState) {
        String query = "UPDATE Books SET status = ? WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, newState);
            ps.setInt(2, bookId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Hata: Kitap durumu güncellenemedi. Kitap ID: " + bookId);
            e.printStackTrace();
        }


    }


}
