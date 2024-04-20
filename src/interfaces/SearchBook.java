package interfaces;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SearchBook {
    private static Scanner scanner = new Scanner(System.in);

    public static void displaySearchMenu(Connection conn) {
        System.out.println("Book Search Menu");
        System.out.println("What do you want to search??");
        System.out.println("1 ISBN");
        System.out.println("2 Book Title");
        System.out.println("3 Author Name");
        System.out.println("4 Exit");
        System.out.print("Your choice?...");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        handleChoice(choice, conn);
    }

    private static void handleChoice(int choice, Connection conn) {
        switch (choice) {
            case 1:
                searchByISBN(conn);
                displaySearchMenu(conn);
                break;
            case 2:
                searchByBookTitle(conn);
                displaySearchMenu(conn);
                break;
            case 3:
                searchByAuthorName(conn);
                displaySearchMenu(conn);
                break;
            case 4:
                System.out.println("Exiting book search...");
                CustomerInterface.displayCustomerInterface(conn);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private static void searchByISBN(Connection conn) {
        System.out.print("Enter the ISBN: ");
        String isbn = scanner.nextLine();
    
        try {
            // Prepare the SQL query to retrieve book details and authors
            String query = "SELECT b.BOOK_TITLE, b.ISBN, b.UNIT_PRICE, b.COPIES_AVAILABLE, a.AUTHOR_NAME " +
                           "FROM BOOKS b " +
                           "LEFT JOIN AUTHORS a ON b.ISBN = a.ISBN " +
                           "WHERE b.ISBN = ? " +
                           "ORDER BY b.BOOK_TITLE ASC, b.ISBN ASC, a.AUTHOR_NAME ASC";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, isbn);
    
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                // Book found, display the details
                String bookTitle = resultSet.getString("BOOK_TITLE");
                float unitPrice = resultSet.getFloat("UNIT_PRICE");
                int copiesAvailable = resultSet.getInt("COPIES_AVAILABLE");
                List<String> authors = new ArrayList<>();
    
                // Add the first author
                authors.add(resultSet.getString("AUTHOR_NAME"));
    
                // Fetch remaining authors for the same book
                while (resultSet.next()) {
                    String authorName = resultSet.getString("AUTHOR_NAME");
                    if (authorName != null) {
                        authors.add(authorName);
                    }
                }
    
                System.out.println("Book Details:");
                System.out.println("Book Title: " + bookTitle);
                System.out.println("ISBN: " + isbn);
                System.out.println("Unit Price: " + unitPrice);
                System.out.println("Copies Available: " + copiesAvailable);
                System.out.println("Authors:");
                for (int i = 0; i < authors.size(); i++) {
                    System.out.println((i + 1) + ". " + authors.get(i));
                }
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

    private static void searchByBookTitle(Connection conn) {
        System.out.print("Enter the book title (use '%' or '_' for wildcards): ");
        String title = scanner.nextLine();
    
        try {
            // Prepare the SQL query to retrieve book details and authors
            String query = "SELECT b.BOOK_TITLE, b.ISBN, b.UNIT_PRICE, b.COPIES_AVAILABLE, a.AUTHOR_NAME " +
                           "FROM BOOKS b " +
                           "LEFT JOIN AUTHORS a ON b.ISBN = a.ISBN " +
                           "WHERE LOWER(b.BOOK_TITLE) LIKE ? " +
                           "ORDER BY CASE WHEN LOWER(b.BOOK_TITLE) = ? THEN 1 ELSE 2 END, b.BOOK_TITLE ASC, b.ISBN ASC, a.AUTHOR_NAME ASC";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + title.toLowerCase().replaceAll("_", "__") + "%");
            statement.setString(2, title.toLowerCase().replaceAll("_", "__"));
    
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
    
            boolean foundBooks = false;
            String currentISBN = null;
            String currentBookTitle = null;
            float currentUnitPrice = 0.0f;
            int currentCopiesAvailable = 0;
            List<String> authors = new ArrayList<>();
    
            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                if (currentISBN == null || !currentISBN.equals(isbn)) {
                    // New book, print the details for the previous book (if any)
                    if (currentISBN != null) {
                        printBookDetails(currentBookTitle, currentISBN, currentUnitPrice, currentCopiesAvailable, authors);
                        authors.clear();
                    }
    
                    foundBooks = true;
                    currentISBN = isbn;
                    currentBookTitle = resultSet.getString("BOOK_TITLE");
                    currentUnitPrice = resultSet.getFloat("UNIT_PRICE");
                    currentCopiesAvailable = resultSet.getInt("COPIES_AVAILABLE");
    
                    // Add the first author
                    String authorName = resultSet.getString("AUTHOR_NAME");
                    if (authorName != null) {
                        authors.add(authorName);
                    }
                } else {
                    // Same book, add the author
                    String authorName = resultSet.getString("AUTHOR_NAME");
                    if (authorName != null) {
                        authors.add(authorName);
                    }
                }
            }
    
            // Print the details for the last book
            if (currentISBN != null) {
                printBookDetails(currentBookTitle, currentISBN, currentUnitPrice, currentCopiesAvailable, authors);
            }
    
            if (!foundBooks) {
                System.out.println("No books found with the title: " + title);
            }
    
            // Close the resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void searchByAuthorName(Connection conn) {
        System.out.print("Enter the author name: ");
        String authorName = scanner.nextLine();
    
        try {
            // Prepare the SQL query to retrieve book details and authors
            String query = "SELECT b.BOOK_TITLE, b.ISBN, b.UNIT_PRICE, b.COPIES_AVAILABLE, a.AUTHOR_NAME " +
                           "FROM BOOKS b " +
                           "INNER JOIN AUTHORS a ON b.ISBN = a.ISBN " +
                           "WHERE LOWER(a.AUTHOR_NAME) LIKE ? " +
                           "ORDER BY b.BOOK_TITLE ASC, b.ISBN ASC, a.AUTHOR_NAME ASC";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + authorName.toLowerCase() + "%");
    
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
    
            boolean foundBooks = false;
            String currentISBN = null;
            String currentBookTitle = null;
            float currentUnitPrice = 0.0f;
            int currentCopiesAvailable = 0;
            List<String> authors = new ArrayList<>();
    
            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                if (currentISBN == null || !currentISBN.equals(isbn)) {
                    // New book, print the details for the previous book (if any)
                    if (currentISBN != null) {
                        printBookDetails(currentBookTitle, currentISBN, currentUnitPrice, currentCopiesAvailable, authors);
                        authors.clear();
                    }
    
                    foundBooks = true;
                    currentISBN = isbn;
                    currentBookTitle = resultSet.getString("BOOK_TITLE");
                    currentUnitPrice = resultSet.getFloat("UNIT_PRICE");
                    currentCopiesAvailable = resultSet.getInt("COPIES_AVAILABLE");
    
                    // Add the first author
                    String authorNameFromResult = resultSet.getString("AUTHOR_NAME");
                    authors.add(authorNameFromResult);
                } else {
                    // Same book, add the author
                    String authorNameFromResult = resultSet.getString("AUTHOR_NAME");
                    authors.add(authorNameFromResult);
                }
            }
    
            // Print the details for the last book
            if (currentISBN != null) {
                printBookDetails(currentBookTitle, currentISBN, currentUnitPrice, currentCopiesAvailable, authors);
            }
    
            if (!foundBooks) {
                System.out.println("No books found for the author: " + authorName);
            }
    
            // Close the resources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void printBookDetails(String bookTitle, String isbn, float unitPrice, int copiesAvailable, List<String> authors) {
        System.out.println("Book Title: " + bookTitle);
        System.out.println("ISBN: " + isbn);
        System.out.println("Unit Price: " + unitPrice);
        System.out.println("Copies Available: " + copiesAvailable);
        System.out.println("Authors:");
        for (int i = 0; i < authors.size(); i++) {
            System.out.println((i + 1) + ". " + authors.get(i));
        }
        System.out.println();
    }
}