import java.sql.*;

public class Pet_db {

    public void addPet(String name, String species, int age) {
        String sql = "INSERT INTO pets(name, species, age) VALUES (?, ?, ?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, species);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            System.out.println("Animal adaugat cu succes!");

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showAvailablePets() {
        String sql = "SELECT * FROM pets WHERE adopted = FALSE";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Animale disponibile ---");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("pet_id") + ". " +
                                rs.getString("name") + " | " +
                                rs.getString("species") + " | " +
                                rs.getInt("age") + " ani"
                );
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void searchBySpecies(String species) {
        String sql = "SELECT * FROM pets WHERE LOWER(species) = LOWER(?) AND adopted = FALSE";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, species);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\n--- Animale disponibile: " + species + " ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("pet_id") + ". " +
                                rs.getString("name") + " | " +
                                rs.getString("species") + " | " +
                                rs.getInt("age") + " ani"
                );
            }
            if (!found) {
                System.out.println("Nu exista animale disponibile din aceasta specie.");
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }

    public void showLongWaitingPets() {
        String sql = """
                SELECT p.pet_id, p.name, p.species,
                       CURRENT_DATE - p.created_at::DATE AS days_waiting
                FROM pets p
                WHERE p.adopted = FALSE
                ORDER BY days_waiting DESC
                """;

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Animale care asteapta adoptie ---");
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getString("name") + " (" +
                                rs.getString("species") + ") — asteapta de " +
                                rs.getInt("days_waiting") + " zile"
                );
            }
            if (!found) {
                System.out.println("Nu exista animale in asteptare.");
            }

        } catch (SQLException e) {
            System.out.println("Eroare: " + e.getMessage());
        }
    }
}