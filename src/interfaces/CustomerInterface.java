package interfaces;

import java.util.Scanner;
import java.sql.Connection;

public class CustomerInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayCustomerInterface(Connection conn) {
        System.out.println("<This is the Customer interface.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. Book Search");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to main menu.");
        System.out.print("Please enter your choice??..");
        int choice = scanner.nextInt();

        handleChoice(choice, conn);
    }

    private static void handleChoice(int choice, Connection conn) {
        switch (choice) {
            case 1:
                SearchBook.displaySearchMenu(conn); // Use the existing instance
                break;
            case 2:
                CreateOrder(conn);
                
                displayCustomerInterface(conn);
                break;
            case 3:
                OrderAltering(conn);
                
                break;
            case 4:
                OrderQuery(conn);

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



    private static void CreateOrder(Connection conn) {


    }

    private static void OrderAltering(Connection conn) {


    }

    private static void OrderQuery(Connection conn) {


    }

}