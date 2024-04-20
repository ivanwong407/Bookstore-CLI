package interfaces;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import utils.DateTimeUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; //show system date
import java.util.Collections;
import java.sql.SQLIntegrityConstraintViolationException;

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
                scanner.nextLine();
                CreateTable(conn);
                displaySystemInterface(conn);
                break;
            case 2:
                scanner.nextLine();
                DeleteTable(conn);
                displaySystemInterface(conn);
                break;
            case 3:
                scanner.nextLine();
                InsertData(conn);
                displaySystemInterface(conn);
                break;
            case 4:
                SetSystemDate();
                displaySystemInterface(conn);
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

    private static void CreateTable(Connection conn) {
        try {
            Statement stmt = conn.createStatement();

            // Prompt the user to enter the table definition
            System.out.println("Enter the table definition (e.g., CREATE TABLE TABLENAME (COLUMN1 TYPE, COLUMN2 TYPE, ...);):");
            String tableDefinition = scanner.nextLine();

            // Execute the SQL statement
            stmt.executeUpdate(tableDefinition);
            System.out.println("Table created successfully.");

            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void DeleteTable(Connection conn) {
        try {
            // Prompt the user for the table name
            System.out.print("Enter the table name: ");
            String tableName = scanner.nextLine();

            // Create the SQL statement
            String sql = "DROP TABLE " + tableName;

            // Execute the SQL statement
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Table " + tableName + " deleted successfully.");

            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void InsertData(Connection conn) {
        try {
            // Prompt the user for the folder path containing the data files
            System.out.print("Please enter the folder path: ");
            String folderPath = scanner.nextLine();

            File folder = new File(folderPath);
            if (!folder.exists() || !folder.isDirectory()) {
                System.out.println("Invalid folder path.");
                return;
            }

            // Load data from books.txt into BOOKS table
            String booksFile = folderPath + File.separator + "book.txt";
            insertDataFromFile(conn, booksFile, "BOOKS", "ISBN, BOOK_TITLE, UNIT_PRICE, COPIES_AVAILABLE");

            // Load data from customer.txt into CUSTOMERS table
            String customerFile = folderPath + File.separator + "customer.txt";
            insertDataFromFile(conn, customerFile, "CUSTOMERS",
                    "CUSTOMER_ID, CUSTOMER_NAME, SHIPPING_ADDRESS, CREDIT_CARD_NO");

            // Load data from orders.txt into ORDERS table
            String ordersFile = folderPath + File.separator + "orders.txt";
            insertDataFromFile(conn, ordersFile, "ORDERS",
                    "ORDER_ID, ORDER_DATE, SHIPPING_STATUS, CHARGE, CUSTOMER_ID");

            // Load data from ordering.txt into BOOK_ORDERED table
            String orderingFile = folderPath + File.separator + "ordering.txt";
            insertDataFromFile(conn, orderingFile, "BOOK_ORDERED", "ORDER_ID, ISBN, QUANTITY");

            // Load data from book_author.txt into AUTHORS table
            String bookAuthorFile = folderPath + File.separator + "book_author.txt";
            insertDataFromFile(conn, bookAuthorFile, "AUTHORS", "ISBN, AUTHOR_NAME");

            System.out.println("Processing...Data is loaded!");

        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void insertDataFromFile(Connection conn, String filePath, String tableName, String columnNames)
            throws SQLException, FileNotFoundException {
        String[] columns = columnNames.split(", ");
        String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));
        String sql = "INSERT INTO " + tableName + " (" + columnNames + ") VALUES (" + placeholders + ")";

        PreparedStatement stmt = conn.prepareStatement(sql);

        Scanner scanner = new Scanner(new File(filePath));

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split("\\|");

                for (int i = 0; i < values.length; i++) {
                    if (columns[i].equals("ORDER_DATE")) {
                        // Assuming the date format in the text file is "yyyy-MM-dd"
                        LocalDate date = LocalDate.parse(values[i]);
                        stmt.setDate(i + 1, java.sql.Date.valueOf(date));
                    } else {
                        stmt.setString(i + 1, values[i]);
                    }
                }

                try {
                    stmt.executeUpdate();
                } catch (SQLIntegrityConstraintViolationException ex) {
                    // Ignore the unique constraint violation

                } catch (SQLException ex) {
                    // Ignore other SQL exceptions
                    System.out.println("Ignoring SQL exception for table: " + tableName);
                }
            }

            System.out.println("Data successfully inserted into " + tableName);
        } catch (SQLException ex) {
            System.out.println("Error inserting data into " + tableName);
            ex.printStackTrace();
        } finally {
            scanner.close();
            stmt.close();
        }
    }

    private static void SetSystemDate() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(System.in);

            // Prompt the user for the new system date
            System.out.print("Enter the new system date (YYYY-MM-DD): ");
            String newDateString = scanner.nextLine();

            // Parse the input date string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate newDate = LocalDate.parse(newDateString, formatter);

            // Set the new system date
            LocalDateTime newDateTime = newDate.atStartOfDay();
            DateTimeUtils.setCurrentDateTime(newDateTime);

            System.out.println("System date updated successfully!");
        } catch (Exception ex) {
            System.out.println("Error occurred while setting system date: " + ex.getMessage());
        }
    }

}