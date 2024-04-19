package interfaces;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchBookCommand implements Command {
    private static Scanner scanner = new Scanner(System.in);
    private Connection conn;

    public SearchBookCommand(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void execute(Connection conn) {
        System.out.println("Book Search");
        System.out.println("To query a book by ISBN, Book Title and Author Name");
        System.out.println("What do you want to search??");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice?...");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        switch (choice) {
            case 1:
                searchByISBN();
                break;
            case 2:
                searchByBookTitle();
                break;
            case 3:
                searchByAuthorName();
                break;
            case 4:
                System.out.println("Exiting book search...");
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private void searchByISBN() {
        System.out.print("Enter the ISBN: ");
        String isbn = scanner.nextLine();

        try {
            // Prepare the SQL query
            String query = "SELECT * FROM BOOKS WHERE ISBN = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, isbn);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Book found, display the details
                String bookTitle = resultSet.getString("BOOK_TITLE");
                float unitPrice = resultSet.getFloat("UNIT_PRICE");
                int copiesAvailable = resultSet.getInt("COPIES_AVAILABLE");

                System.out.println("Book Details:");
                System.out.println("ISBN: " + isbn);
                System.out.println("Book Title: " + bookTitle);
                System.out.println("Unit Price: " + unitPrice);
                System.out.println("Copies Available: " + copiesAvailable);
            } else {
                System.out.println("No book found with ISBN: " + isbn);
            }

            // Close the resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByBookTitle() {
        System.out.print("Enter the book title: ");
        String title = scanner.nextLine();
        // Implement logic to search for a book by title
        System.out.println("Searching for book with title: " + title);
        // ...
    }

    private void searchByAuthorName() {
        System.out.print("Enter the author name: ");
        String authorName = scanner.nextLine();
        // Implement logic to search for a book by author name
        System.out.println("Searching for book with author: " + authorName);
        // ...
    }
}