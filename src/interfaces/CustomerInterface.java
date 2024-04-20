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

    //-------------CreateOrder---------------//
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

//-------------OrderAltering---------------//
    private static void OrderAltering(Connection conn) {
    System.out.print("Please enter the OrderID that you want to change: ");
    int orderId = scanner.nextInt();

    // Retrieve the order details
    Map<String, Integer> orderedBooks = getOrderDetails(conn, orderId);
    
    if (orderedBooks.isEmpty()) {
        System.out.println("No books found for the given order.");
        return;
    }
    displayOrderDetails(conn, orderId);
    // Display the ordered books and prompt the user to select one
    int selectedIndex = displayOrderedBooksAndSelectBook(orderedBooks, conn);
    if (selectedIndex == -1) {
        return;
    }

    String isbn = getIsbNFromOrderedBooks(orderedBooks, selectedIndex);

    System.out.print("Input add or remove: ");
    String action = scanner.next().toLowerCase();

    if (action.equals("add")) {
        addCopiesOfBook(conn, orderId, isbn);
    } else if (action.equals("remove")) {
        removeCopiesOfBook(conn, orderId, isbn);
    } else {
        System.out.println("Invalid action.");
    }
}
    
    private static int displayOrderedBooksAndSelectBook(Map<String, Integer> orderedBooks, Connection conn) {
    if (orderedBooks.isEmpty()) {
        System.out.println("No books ordered yet.");
        return -1;
    }

    System.out.println("Ordered Books:");
    int i = 0;
    for (Map.Entry<String, Integer> entry : orderedBooks.entrySet()) {
        String isbn = entry.getKey();
        int quantity = entry.getValue();
        String bookTitle = getBookTitle(conn, isbn);
        System.out.println((i + 1) + ". " + bookTitle + ", Quantity: " + quantity);
        i++;
    }

    System.out.print("Which book you want to alter (input book no.): ");
    int bookNo = scanner.nextInt() - 1;

    if (bookNo < 0 || bookNo >= orderedBooks.size()) {
        System.out.println("Invalid book number.");
        return -1;
    }

    return bookNo;
}

    private static String getIsbNFromOrderedBooks(Map<String, Integer> orderedBooks, int index) {
    int i = 0;
    for (String isbn : orderedBooks.keySet()) {
        if (i == index) {
            return isbn;
        }
        i++;
    }
    return null;
}

    private static String getBookTitle(Connection conn, String isbn) {  
    String query = "SELECT BOOK_TITLE FROM BOOKS WHERE ISBN = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getString("BOOK_TITLE");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return "Unknown Book";
}    

    private static Map<String, Integer> getOrderDetails(Connection conn, int orderId) {
        Map<String, Integer> orderedBooks = new HashMap<>();
        String query = "SELECT ISBN, QUANTITY FROM BOOK_ORDERED WHERE ORDER_ID = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String isbn = rs.getString("ISBN");
                int quantity = rs.getInt("QUANTITY");
                orderedBooks.put(isbn, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return orderedBooks;
    }
    
    private static void addCopiesOfBook(Connection conn, int orderId, String isbn) {
        System.out.print("Input the number: ");
        int additionalQuantity = scanner.nextInt();
    
        String updateOrderQuery = "UPDATE BOOK_ORDERED SET QUANTITY = QUANTITY + ? WHERE ORDER_ID = ? AND ISBN = ?";
        String updateBookQuery = "UPDATE BOOKS SET COPIES_AVAILABLE = COPIES_AVAILABLE - ? WHERE ISBN = ?";
    
        try (PreparedStatement orderStmt = conn.prepareStatement(updateOrderQuery);
             PreparedStatement bookStmt = conn.prepareStatement(updateBookQuery)) {
    
            orderStmt.setInt(1, additionalQuantity);
            orderStmt.setInt(2, orderId);
            orderStmt.setString(3, isbn);
            int rowsUpdated = orderStmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                bookStmt.setInt(1, additionalQuantity);
                bookStmt.setString(2, isbn);
                bookStmt.executeUpdate();
    
                System.out.println("Update is ok!");
                System.out.println("Update done!!");
                updateOrderCharge(conn, orderId);
            } else {
                System.out.println("Failed to update the order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void removeCopiesOfBook(Connection conn, int orderId, String isbn) {
        System.out.print("Input the number: ");
        int quantityToRemove = scanner.nextInt();
    
        String updateOrderQuery = "UPDATE BOOK_ORDERED SET QUANTITY = QUANTITY - ? WHERE ORDER_ID = ? AND ISBN = ?";
        String updateBookQuery = "UPDATE BOOKS SET COPIES_AVAILABLE = COPIES_AVAILABLE + ? WHERE ISBN = ?";
    
        try (PreparedStatement orderStmt = conn.prepareStatement(updateOrderQuery);
             PreparedStatement bookStmt = conn.prepareStatement(updateBookQuery)) {
    
            orderStmt.setInt(1, quantityToRemove);
            orderStmt.setInt(2, orderId);
            orderStmt.setString(3, isbn);
            int rowsUpdated = orderStmt.executeUpdate();
    
            if (rowsUpdated > 0) {
                bookStmt.setInt(1, quantityToRemove);
                bookStmt.setString(2, isbn);
                bookStmt.executeUpdate();
    
                System.out.println("Update is ok!");
                System.out.println("Update done!!");
                updateOrderCharge(conn, orderId);
            } else {
                System.out.println("Failed to update the order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void updateOrderCharge(Connection conn, int orderId) {
        Map<String, Integer> orderedBooks = getOrderDetails(conn, orderId);
        float totalCharge = calculateTotalCharge(conn, orderedBooks);
    
        String updateQuery = "UPDATE ORDERS SET CHARGE = ? WHERE ORDER_ID = ?";
    
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setFloat(1, totalCharge);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
    
            System.out.println("Updated charge:");
            displayOrderDetails(conn, orderId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void displayOrderDetails(Connection conn, int orderId) {
        String query = "SELECT o.ORDER_ID, o.SHIPPING_STATUS, o.CHARGE, o.CUSTOMER_ID, SUM(bo.QUANTITY) AS TOTAL_QUANTITY " +
                       "FROM ORDERS o " +
                       "LEFT JOIN BOOK_ORDERED bo ON o.ORDER_ID = bo.ORDER_ID " +
                       "WHERE o.ORDER_ID = ? " +
                       "GROUP BY o.ORDER_ID, o.SHIPPING_STATUS, o.CHARGE, o.CUSTOMER_ID";
    
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                int orderid = rs.getInt("ORDER_ID");
                String shippingStatus = rs.getString("SHIPPING_STATUS");
                float charge = rs.getFloat("CHARGE");
                String customerId = rs.getString("CUSTOMER_ID");
                int totalQuantity = rs.getInt("TOTAL_QUANTITY");
    
                System.out.println("order_id:" + orderid + " shipping:" + shippingStatus + " charge=" + charge + " customerId=" + customerId + " total_quantity=" + totalQuantity);
            } else {
                System.out.println("Order not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//-------------OrderQuery---------------//
    private static void OrderQuery(Connection conn) {
    System.out.print("Please enter Customer ID: ");
    String customerId = scanner.next();

    // Check if the customer ID exists
    if (!isCustomerIdValid(conn, customerId)) {
        System.out.println("Invalid customer ID. Please try again.");
        return;
    }

    System.out.print("Please input the year: ");
    int year = scanner.nextInt();

    String query = "SELECT o.ORDER_ID, o.ORDER_DATE, bo.ISBN, b.BOOK_TITLE, bo.QUANTITY, o.CHARGE, o.SHIPPING_STATUS " +
    "FROM ORDERS o " +
    "JOIN BOOK_ORDERED bo ON o.ORDER_ID = bo.ORDER_ID " +
    "JOIN BOOKS b ON bo.ISBN = b.ISBN " +
    "WHERE o.CUSTOMER_ID = ? AND EXTRACT(YEAR FROM o.ORDER_DATE) = ? " +
    "ORDER BY o.ORDER_ID ASC";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, customerId);
        stmt.setInt(2, year);
        ResultSet rs = stmt.executeQuery();

        if (!rs.isBeforeFirst()) {
            System.out.println("No orders found for the given customer in the specified year.");
        } else {
            System.out.println("Order ID\tOrder Date\tBooks Ordered\tCharge\tShipping Status");
            while (rs.next()) {
                int orderId = rs.getInt("ORDER_ID");
                java.sql.Date orderDate = rs.getDate("ORDER_DATE");
                //String isbn = rs.getString("ISBN");
                String bookTitle = rs.getString("BOOK_TITLE");
                int quantity = rs.getInt("QUANTITY");
                float charge = rs.getFloat("CHARGE");
                String shippingStatus = rs.getString("SHIPPING_STATUS");

                System.out.printf("%d\t\t%tF\t%s (%d)\t\t%.2f\t%s%n", orderId, orderDate, bookTitle, quantity, charge, shippingStatus);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}