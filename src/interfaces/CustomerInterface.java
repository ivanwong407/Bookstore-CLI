package interfaces;

import java.util.Scanner;
import java.sql.Connection;
import java.util.Map;
import java.util.HashMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        switch (choice) {
            case 1:
                SearchBook.displaySearchMenu(conn); // Use the existing instance
                break;
            case 2:
                CreateOrder(conn);
                displayCustomerInterface(conn);
                break;
            case 3:
                OrderAltering(conn);
                displayCustomerInterface(conn);
                break;
            case 4:
                OrderQuery(conn);
                displayCustomerInterface(conn);
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

    private static boolean isCustomerIdValid(Connection conn, String customerId) {
        String query = "SELECT COUNT(*) FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static boolean isBookAvailable(Connection conn, String isbn, int quantity) {
        String query = "SELECT COPIES_AVAILABLE FROM BOOKS WHERE ISBN = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int copiesAvailable = rs.getInt("COPIES_AVAILABLE");
                return copiesAvailable >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static int getNextOrderId(Connection conn) {
        String query = "SELECT MAX(ORDER_ID) FROM ORDERS";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                int maxOrderId = rs.getInt(1);
                return maxOrderId + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Return 1 if the ORDERS table is empty
    }
    
    private static void insertOrder(Connection conn, int orderId, String customerId, float charge) {
        String query = "INSERT INTO ORDERS (ORDER_ID, ORDER_DATE, SHIPPING_STATUS, CHARGE, CUSTOMER_ID) VALUES (?, ?, 'N', ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            stmt.setFloat(3, charge);
            stmt.setString(4, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static float calculateTotalCharge(Connection conn, Map<String, Integer> orderDetails) {
        float totalCharge = 0.0f;
        String query = "SELECT UNIT_PRICE FROM BOOKS WHERE ISBN = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Map.Entry<String, Integer> entry : orderDetails.entrySet()) {
                String isbn = entry.getKey();
                int quantity = entry.getValue();
    
                stmt.setString(1, isbn);
                ResultSet rs = stmt.executeQuery();
    
                if (rs.next()) {
                    float unitPrice = rs.getFloat("UNIT_PRICE");
                    totalCharge += unitPrice * quantity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return totalCharge;
    }

    private static void insertBookOrdered(Connection conn, int orderId, String isbn, int quantity) {
        String query = "INSERT INTO BOOK_ORDERED (ORDER_ID, ISBN, QUANTITY) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            stmt.setString(2, isbn);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void updateBookCopies(Connection conn, String isbn, int quantity) {
        String query = "UPDATE BOOKS SET COPIES_AVAILABLE = COPIES_AVAILABLE - ? WHERE ISBN = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void displayOrderedBooks(Map<String, Integer> orderDetails) {
        if (orderDetails.isEmpty()) {
            System.out.println("No books ordered yet.");
        } else {
            System.out.println("Ordered Books:");
            for (Map.Entry<String, Integer> entry : orderDetails.entrySet()) {
                String isbn = entry.getKey();
                int quantity = entry.getValue();
                System.out.println("ISBN: " + isbn + ", Quantity: " + quantity);
            }
        }
    }

    private static void CreateOrder(Connection conn) {
        System.out.print("Please enter your customerID: ");
        String customerId = scanner.next();
    
        // Check if the customer ID exists
        if (!isCustomerIdValid(conn, customerId)) {
            System.out.println("Invalid customer ID. Please try again.");
            return;
        }
    
        Map<String, Integer> orderDetails = new HashMap<>();
        boolean orderingBooks = true;
    
        while (orderingBooks) {
            System.out.print("Please enter the book's ISBN (or 'L' to see ordered list, or 'F' to finish ordering): ");
            String input = scanner.next();
    
            if (input.equalsIgnoreCase("L")) {
                displayOrderedBooks(orderDetails);
            } else if (input.equalsIgnoreCase("F")) {
                orderingBooks = false;
            } else {
                System.out.print("Please enter the quantity of the order: ");
                int quantity = scanner.nextInt();
    
                if (isBookAvailable(conn, input, quantity)) {
                    orderDetails.put(input, quantity);
                } else {
                    System.out.println("Requested quantity not available for the book.");
                }
            }
        }
    
        if (orderDetails.isEmpty()) {
            System.out.println("No books ordered.");
            return;
        }
    
        int nextOrderId = getNextOrderId(conn);
        float totalCharge = calculateTotalCharge(conn, orderDetails);
        insertOrder(conn, nextOrderId, customerId, totalCharge);
    
        for (Map.Entry<String, Integer> entry : orderDetails.entrySet()) {
            String isbn = entry.getKey();
            int quantity = entry.getValue();
            insertBookOrdered(conn, nextOrderId, isbn, quantity);
            updateBookCopies(conn, isbn, quantity);
        }
    
        System.out.println("Order created successfully with Order ID: " + nextOrderId);
    }




    private static void OrderAltering(Connection conn) {


    }

    private static void OrderQuery(Connection conn) {


    }

}