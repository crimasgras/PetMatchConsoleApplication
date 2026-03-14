import java.sql.*;

public class User_db {

    public void addUser(String name, String email, String phone) {
        String sql = "INSERT INTO users(name, email, phone) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.executeUpdate();
            System.out.println("User adaugat cu succes!");

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showUsers() {
        String sql = "SELECT * FROM users";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Useri ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("user_id") + ". " +
                                rs.getString("name") + " | " +
                                rs.getString("email") + " | " +
                                rs.getString("phone")
                );
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}