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
        Command command;
        switch (choice) {
            case 1:
                command = new SearchBookCommand(conn);
                command.execute(conn);
                break;
            case 2:
                command = new CreateOrderCommand();
                command.execute(conn);
                break;
            case 3:
                command = new OrderAlteringCommand();
                command.execute(conn);
                break;
            case 4:
                command = new OrderQueryCommand();
                command.execute(conn);
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



    private static class CreateOrderCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to place an order
            System.out.println("Placing an order...");
        }
    }

    private static class OrderAlteringCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to view order history
            System.out.println("Order Altering...");
        }
    }

    private static class OrderQueryCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to view order history
            System.out.println("Order query...");
        }
    }

}