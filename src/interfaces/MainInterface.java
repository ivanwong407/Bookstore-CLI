package interfaces;

import java.util.Scanner;

public class MainInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        displayMainMenu();
    }

    public static void displayMainMenu() {
        System.out.println("The System Date is now: 0000-00-00");
        System.out.println("<This is the Book Ordering System.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. System interface.");
        System.out.println("2. Customer interface.");
        System.out.println("3. Bookstore interface.");
        System.out.println("4. Show System Date.");
        System.out.println("5. Quit the system.....");

        System.out.print("Please enter your choice??..");
        int choice = scanner.nextInt();

        handleChoice(choice);
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                SystemInterface.displaySystemInterface();
                break;
            case 2:
                CustomerInterface.displayCustomerInterface();
                break;
            // Add cases for other interfaces
            case 5:
                System.out.println("Quitting the system...");
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }
} // Added closing curly brace here