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
        Command command;
        switch (choice) {
            case 1:
                command = new CreateTableCommand();
                command.execute(conn);
                break;
            case 2:
                command = new DeleteTableCommand();
                command.execute(conn);
                break;
            case 3:
                command = new InsertDataCommand();
                command.execute(conn);
                break;
            case 4:
                command = new SetSystemDateCommand();
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

    private static class CreateTableCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to create a table
            System.out.println("Creating table...");
        }
    }

    private static class DeleteTableCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to delete a table
            System.out.println("Deleting table...");
        }
    }

    private static class InsertDataCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to insert data
            System.out.println("Inserting data...");
        }
    }

    private static class SetSystemDateCommand implements Command {
        @Override
        public void execute(Connection conn) {
            // Implement logic to set system date
            System.out.println("Setting system date...");
        }
    }
}