package interfaces;
import java.sql.Connection;
import java.util.Scanner;
import java.time.LocalDateTime; //show system date
import java.time.format.DateTimeFormatter; //show system date
import utils.DateTimeUtils;


public class MainInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(Connection conn) {
        displayMainMenu(conn);
    }
    
    public static void displayMainMenu(Connection conn) {
        String currentDate = getCurrentDate();
        System.out.println("The System Date is now: " + currentDate);
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system.....");

        System.out.print("Please enter your choice??..");
        int choice = scanner.nextInt();

        handleChoice(choice, conn);
    }

    private static void handleChoice(int choice, Connection conn) {
        switch (choice) {
            case 1:
                SystemInterface.displaySystemInterface(conn);
                break;
            case 2:
                CustomerInterface.displayCustomerInterface(conn);
                break;
            // Add cases for other interfaces
            case 3:
                BookstoreInterface.displayBookstoreInterface(conn);
                break;
            case 4:
                showSystemDate();
                displayMainMenu(conn);
                break;
            case 5:
                System.out.println("Quitting the system...");
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }


    private static String getCurrentDate() {
        LocalDateTime currentDateTime = DateTimeUtils.getCurrentDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }
    
    private static void showSystemDate() {
        String currentDate = getCurrentDate();
        System.out.println("The current system date is: " + currentDate);
    }


} 
