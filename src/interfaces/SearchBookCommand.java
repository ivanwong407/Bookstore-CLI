package interfaces;

import java.util.Scanner;

public class SearchBookCommand implements Command {
    private static Scanner scanner = new Scanner(System.in);

    @Override
    public void execute() {
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

    private static void searchByISBN() {
        System.out.print("Enter the ISBN: ");
        String isbn = scanner.nextLine();
        // Implement logic to search for a book by ISBN
        System.out.println("Searching for book with ISBN: " + isbn);
        // ...
    }

    private static void searchByBookTitle() {
        System.out.print("Enter the book title: ");
        String title = scanner.nextLine();
        // Implement logic to search for a book by title
        System.out.println("Searching for book with title: " + title);
        // ...
    }

    private static void searchByAuthorName() {
        System.out.print("Enter the author name: ");
        String authorName = scanner.nextLine();
        // Implement logic to search for a book by author name
        System.out.println("Searching for book with author: " + authorName);
        // ...
    }
}