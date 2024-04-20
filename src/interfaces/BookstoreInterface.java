package interfaces;

import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class BookstoreInterface {
    private static Scanner scanner = new Scanner(System.in);

    public static void displayBookstoreInterface(Connection conn) {
        System.out.println("<This is the bookstore interface.>");
        System.out.println("-------------------------------------------");
        System.out.println("1. Order Update.");
        System.out.println("2. Order Query.");
        System.out.println("3. N most Popular Book Query.");
        System.out.println("4. Back to main menu.");
        System.out.print("Please enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the leftover newline character
        
        switch (choice) {
            case 1:
                // Implement logic for Order Update
                OrderUpdate(conn);
                displayBookstoreInterface(conn);
                break;
            case 2:
                // Implement logic for Order Query
                break;
            case 3:
                // Implement logic for N most Popular Book Query
                break;
            case 4:
                System.out.println("Going back to main menu...");
                MainInterface.displayMainMenu(conn);
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    private static void OrderUpdate(Connection conn) {
        System.out.print("Please input the order ID: ");
        String orderID = scanner.nextLine();

        try {
            // Check the original shipping status of the order
            String statusQuery = "SELECT SHIPPING_STATUS FROM ORDERS WHERE ORDER_ID = ?";
            PreparedStatement statusStatement = conn.prepareStatement(statusQuery);
            statusStatement.setString(1, orderID);
            ResultSet statusResult = statusStatement.executeQuery();

            if (statusResult.next()) {
                String originalStatus = statusResult.getString("SHIPPING_STATUS");

                // Check if the original status is "N"
                if (originalStatus.equals("N")) {
                    // Check if the order contains at least one book with quantity >= 1
                    String quantityQuery = "SELECT COUNT(*) AS BOOK_COUNT FROM BOOK_ORDERED WHERE ORDER_ID = ? AND QUANTITY >= 1";
                    PreparedStatement quantityStatement = conn.prepareStatement(quantityQuery);
                    quantityStatement.setString(1, orderID);
                    ResultSet quantityResult = quantityStatement.executeQuery();

                    if (quantityResult.next()) {
                        int bookCount = quantityResult.getInt("BOOK_COUNT");

                        if (bookCount > 0) {
                            System.out.println("The Shipping status of " + orderID + " is " + originalStatus + " and "
                                    + bookCount + " books ordered.");
                            System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
                            String updateChoice = scanner.nextLine();

                            if (updateChoice.equalsIgnoreCase("Y")) {
                                // Update the shipping status to "Y"
                                String updateQuery = "UPDATE ORDERS SET SHIPPING_STATUS = 'Y' WHERE ORDER_ID = ?";
                                PreparedStatement updateStatement = conn.prepareStatement(updateQuery);
                                updateStatement.setString(1, orderID);
                                updateStatement.executeUpdate();
                                System.out.println("Updated shipping status to 'Y'.");
                                updateStatement.close();
                            } else {
                                System.out.println("Shipping status update canceled.");
                            }
                        } else {
                            System.out.println("No books with quantity >= 1 found in the order.");
                        }
                    }

                    quantityResult.close();
                    quantityStatement.close();
                } else {
                    System.out.println("No update is allowed. The shipping status is already 'Y'.");
                }
            } else {
                System.out.println("No order found with Order ID: " + orderID);
            }

            statusResult.close();
            statusStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}