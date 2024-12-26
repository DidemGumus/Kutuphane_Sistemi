package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/kutuphane"; // Veritabanı URL
    private String username = "root"; // Veritabanı kullanıcı adı
    private String password = ""; // Veritabanı şifre

    private DatabaseConnection() {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException ex) {
            System.err.println("Veritabanı bağlantısı başarısız: " + ex.getMessage());
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                this.connection = DriverManager.getConnection(url, username, password);
            }
        } catch (SQLException ex) {
            System.err.println("Bağlantı yenileme hatası: " + ex.getMessage());
        }
        return connection;
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
