import java.sql.*;

public class Adoption_db {

    public void adoptPet(int userId, int petId) {
        String checkSql = "SELECT adopted FROM pets WHERE pet_id = ?";
        String insertSql = "INSERT INTO adoptions(user_id, pet_id) VALUES (?, ?)";
        String updateSql = "UPDATE pets SET adopted = TRUE WHERE pet_id = ?";

        try (Connection conn = DataBaseConnection.getConnection()) {

            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, petId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("adopted")) {
                System.out.println("Acest animal este deja adoptat!");
                return;
            }

            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, userId);
            insertStmt.setInt(2, petId);
            insertStmt.executeUpdate();

            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setInt(1, petId);
            updateStmt.executeUpdate();

            System.out.println("Adoptie realizata cu succes!");

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showAdoptions() {
        String sql = """
                SELECT u.name AS user_name,
                       p.name AS pet_name,
                       p.species,
                       a.adopted_at
                FROM adoptions a
                JOIN users u ON a.user_id = u.user_id
                JOIN pets  p ON a.pet_id  = p.pet_id
                ORDER BY a.adopted_at DESC
                """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Adoptii ---");
            while (rs.next()) {
                System.out.println(
                        rs.getString("user_name") + " a adoptat " +
                                rs.getString("pet_name") + " (" +
                                rs.getString("species") + ") la " +
                                rs.getTimestamp("adopted_at")
                );
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showStatistics() {
        String sql = """
            SELECT
                (SELECT COUNT(*) FROM users)     AS total_users,
                (SELECT COUNT(*) FROM pets)      AS total_pets,
                (SELECT COUNT(*) FROM pets 
                 WHERE adopted = TRUE)           AS adopted_pets,
                (SELECT COUNT(*) FROM pets 
                 WHERE adopted = FALSE)          AS available_pets,
                (SELECT COUNT(*) FROM adoptions) AS total_adoptions
            """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("\n=== Statistici ===");
                System.out.println("Total useri:       " + rs.getInt("total_users"));
                System.out.println("Total animale:     " + rs.getInt("total_pets"));
                System.out.println("Animale adoptate:  " + rs.getInt("adopted_pets"));
                System.out.println("Animale libere:    " + rs.getInt("available_pets"));
                System.out.println("Total adoptii:     " + rs.getInt("total_adoptions"));
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}
