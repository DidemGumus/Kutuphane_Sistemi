package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserModel {

    // Kullanıcı doğrulama: Kullanıcıyı kontrol edip user_id döndüren yöntem
    public int verifyUser(String username, String password) {
        String query = "SELECT user_id FROM Users WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password); // Şifreyi hash'leyerek kontrol et
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id"); // Kullanıcı ID'sini döndür
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kullanıcı doğrulama işlemi başarısız. Kullanıcı adı: " + username);
            e.printStackTrace();
        }
        return -1; // Kullanıcı bulunamadıysa -1 döndür
    }

    // Kullanıcı ekleme: Yeni kullanıcıyı veritabanına ekleme yöntemi
    public boolean addUser(String username, String password, String userType, String email) {
        String query = "INSERT INTO Users (username, password, user_type, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password); // Şifreyi hash'leyerek sakla
            ps.setString(3, userType);
            ps.setString(4, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Hata: Kullanıcı ekleme işlemi başarısız. Kullanıcı adı: " + username);
            e.printStackTrace();
        }
        return false;
    }
    public String getUserType(String username) {
        String query = "SELECT user_type FROM Users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("user_type");
            }
        } catch (SQLException e) {
            System.err.println("Hata: Kullanıcı türü alınamadı.");
            e.printStackTrace();
        }
        return null; // Eğer kullanıcı türü bulunamazsa null dönecek
    }


}

