package interfaces;

import java.util.Scanner;
import java.sql.Connection;


public class BookstoreInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayBookstoreInterface(Connection conn) {
        System.out.println("<This is the bookstore interface.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.");

        System.out.print("Please enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Implement logic for Order Update
                break;
            case 2:
                // Implement logic for Order Query
                break;
            case 3:
                // Implement logic for N most Popular Book Query
                break;
            case 4:
                System.out.println("Going back to main menu...");
                MainInterface.displayMainMenu(conn);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }
}