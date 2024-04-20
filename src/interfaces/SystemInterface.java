package interfaces;

import java.util.Scanner;
import java.sql.Connection;
public class SystemInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void displaySystemInterface(Connection conn) {
        System.out.println("<This is the System interface.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. Create Table.");
        System.out.println("2. Delete Table.");
        System.out.println("3. Insert Data.");
        System.out.println("4. Set System Date.");
        System.out.println("5. Back to main menu.");

        System.out.print("Please enter your choice??..");
        int choice = scanner.nextInt();

        handleChoice(choice, conn);
    }

    private static void handleChoice(int choice, Connection conn) {
        switch (choice) {
            case 1:
                CreateTable();

                break;
            case 2:
                DeleteTable();
                
                break;
            case 3:
                InsertData();
                
                break;
            case 4:
                SetSystemDate();
                
                break;
            case 5:
                System.out.println("Going back to main menu...");
                MainInterface.displayMainMenu(conn);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private static void CreateTable() {
            // Implement logic to create a table
            System.out.println("Creating table...");

    }

    private static void DeleteTable() {
            // Implement logic to delete a table
            System.out.println("Deleting table...");

    }

    private static void InsertData() {
            // Implement logic to insert data
            System.out.println("Inserting data...");

    }

    private static void SetSystemDate() {
            // Implement logic to set system date
            System.out.println("Setting system date...");
    }
}