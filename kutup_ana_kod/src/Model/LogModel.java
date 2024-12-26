package Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogModel {
    public boolean addLog(int userId, String action) {
        String query = "INSERT INTO Logs (user_id, action) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, action);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
