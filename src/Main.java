import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User_db userDb = new User_db();
        Pet_db petDb = new Pet_db();
        Adoption_db adoptionDb = new Adoption_db();

        while (true) {
            System.out.println("\n=== Pet Adoption System ===");
            System.out.println("1. Add user");
            System.out.println("2. Add pet");
            System.out.println("3. Adopt pet");
            System.out.println("4. Show adoptions");
            System.out.println("5. Show available pets");
            System.out.println("6. Statistics");
            System.out.println("7. Search pet by species");
            System.out.println("8. Show waiting pets");
            System.out.println("9. Find my perfect pet (Quiz)");
            System.out.println("10. Show all users");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Phone: ");
                    String phone = scanner.nextLine();
                    userDb.addUser(name, email, phone);
                }
                case 2 -> {
                    System.out.print("Pet name: ");
                    String name = scanner.nextLine();
                    System.out.print("Species: ");
                    String species = scanner.nextLine();
                    System.out.print("Age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    petDb.addPet(name, species, age);
                }
                case 3 -> {
                    System.out.print("User ID: ");
                    int userId = scanner.nextInt();
                    System.out.print("Pet ID: ");
                    int petId = scanner.nextInt();
                    adoptionDb.adoptPet(userId, petId);
                }
                case 4 -> adoptionDb.showAdoptions();
                case 5 -> petDb.showAvailablePets();
                case 6 -> adoptionDb.showStatistics();
                case 7 -> {
                    System.out.print("Species (Dog/Cat/...): ");
                    String species = scanner.nextLine();
                    petDb.searchBySpecies(species);
                }
                case 8 -> petDb.showLongWaitingPets();
                case 9 -> new AdoptionQuiz().startQuiz();
                case 10 -> userDb.showUsers();
                case 0 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }
}