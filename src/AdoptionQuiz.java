import java.sql.*;
import java.util.Scanner;

public class AdoptionQuiz {

    private final Scanner scanner = new Scanner(System.in);

    public void startQuiz() {
        System.out.println("\n=== Find Your Perfect Pet ===");
        System.out.println("Answer a few questions to find your best match!\n");

        // Q1
        System.out.println("Q1: How much space do you have?");
        System.out.println("  1. Small apartment");
        System.out.println("  2. Large apartment or house");
        System.out.print("Your answer: ");
        int q1 = scanner.nextInt();

        // Q2
        System.out.println("\nQ2: How active are you?");
        System.out.println("  1. I prefer staying home");
        System.out.println("  2. I go out and exercise often");
        System.out.print("Your answer: ");
        int q2 = scanner.nextInt();

        // Q3
        System.out.println("\nQ3: How much time do you spend at home daily?");
        System.out.println("  1. A lot — I work from home or stay in");
        System.out.println("  2. Not much — I am out most of the day");
        System.out.print("Your answer: ");
        int q3 = scanner.nextInt();

        // Q4
        System.out.println("\nQ4: Have you had a pet before?");
        System.out.println("  1. No, this is my first time");
        System.out.println("  2. Yes, I have experience");
        System.out.print("Your answer: ");
        int q4 = scanner.nextInt();

        // Q5
        System.out.println("\nQ5: Do you have allergies?");
        System.out.println("  1. Yes, I am allergic to fur");
        System.out.println("  2. No allergies");
        System.out.print("Your answer: ");
        int q5 = scanner.nextInt();
        scanner.nextLine();

        // Calculate recommendation
        String recommended = getRecommendation(q1, q2, q3, q4, q5);

        System.out.println("\n=== Your Result ===");
        System.out.println("We recommend: " + recommended);
        System.out.println("\nAvailable " + recommended + "s for adoption:");

        showRecommendedPets(recommended);
        saveResult(recommended);
    }

    private String getRecommendation(int q1, int q2, int q3, int q4, int q5) {

        // Allergies → direct to Fish or Bird
        if (q5 == 1) {
            if (q1 == 1) return "Fish";
            else return "Bird";
        }

        int dogScore    = 0;
        int catScore    = 0;
        int rabbitScore = 0;

        // Space
        if (q1 == 1) { catScore++; rabbitScore++; }
        else dogScore += 2;

        // Activity
        if (q2 == 1) { catScore++; rabbitScore++; }
        else dogScore += 2;

        // Time at home
        if (q3 == 1) { dogScore++; rabbitScore++; }
        else catScore++;

        // Experience
        if (q4 == 1) { catScore++; rabbitScore++; }
        else dogScore++;

        if (dogScore >= catScore && dogScore >= rabbitScore) return "Dog";
        if (catScore >= dogScore && catScore >= rabbitScore) return "Cat";
        return "Rabbit";
    }

    private void showRecommendedPets(String species) {
        String sql = """
                SELECT pet_id, name, species, age
                FROM pets
                WHERE LOWER(species) = LOWER(?)
                AND adopted = FALSE
                ORDER BY age ASC
                """;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, species);
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println(
                        "ID: "  + rs.getInt("pet_id")      + " | " +
                                rs.getString("name")                + " | " +
                                rs.getString("species")             + " | " +
                                rs.getInt("age") + " years old"
                );
            }

            if (!found) {
                System.out.println("No " + species + "s available right now. Check back soon!");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void saveResult(String recommended) {
        String sql = "INSERT INTO quiz_results(recommended) VALUES (?)";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recommended);
            stmt.executeUpdate();
            System.out.println("\nResult saved! Come back anytime to adopt your perfect pet.");

        } catch (SQLException e) {
            System.out.println("Error saving result: " + e.getMessage());
        }
    }
}