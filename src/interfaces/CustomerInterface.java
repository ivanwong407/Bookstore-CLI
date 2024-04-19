package interfaces;

import java.util.Scanner;

public class CustomerInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayCustomerInterface() {
        System.out.println("<This is the Customer interface.>");
        System.out.println("-------------------------------------------");
        System.out.println("1.Book Search");
        System.out.println("2. Order Creation.");
        System.out.println("3. Order Altering.");
        System.out.println("4. Order Query.");
        System.out.println("5. Back to main menu.");
        System.out.print("Please enter your choice??..");
        int choice = scanner.nextInt();

        handleChoice(choice);
    }

    private static void handleChoice(int choice) {
        Command command;
        switch (choice) {
            case 1:
                command = new SearchBookCommand();
                command.execute();
                break;
            case 2:
                command = new CreateOrderCommand();
                command.execute();
                break;
            case 3:
                command = new OrderAlteringCommand();
                command.execute();
                break;
            case 4:
                command = new OrderQueryCommand();
                command.execute();
                break;
            case 5:
                System.out.println("Going back to main menu...");
                MainInterface.displayMainMenu();
                break;  
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private static class SearchBookCommand implements Command {
        @Override
        public void execute() {
            // Implement logic to search for a book
            System.out.println("Searching for a book...");
        }
    }

    private static class CreateOrderCommand implements Command {
        @Override
        public void execute() {
            // Implement logic to place an order
            System.out.println("Placing an order...");
        }
    }

    private static class OrderAlteringCommand implements Command {
        @Override
        public void execute() {
            // Implement logic to view order history
            System.out.println("Order Altering...");
        }
    }

    private static class OrderQueryCommand implements Command {
        @Override
        public void execute() {
            // Implement logic to view order history
            System.out.println("Order query...");
        }
    }

}