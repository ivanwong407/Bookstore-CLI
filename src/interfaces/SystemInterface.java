package interfaces;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.sql.SQLException;

import utils.DateTimeUtils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; //show system date
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;





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
            // Get the database metadata
            DatabaseMetaData dbmd = conn.getMetaData();
    
            // Prompt the user for the table name
            System.out.print("Enter the table name: ");
            String tableName = scanner.nextLine();
    
            // Check if the table already exists
            ResultSet tables = dbmd.getTables(null, null, tableName, null);
            if (tables.next()) {
                System.out.println("Table " + tableName + " already exists.");
                return;
            }
    
            // Prompt the user for the column definitions
            System.out.println("Enter the column definitions (e.g., columnName dataType, columnName dataType, ...):");
            String columnDefinitions = scanner.nextLine();
    
            // Create the SQL statement
            String sql = "CREATE TABLE " + tableName + " (" + columnDefinitions + ")";
    
            // Execute the SQL statement
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("Table " + tableName + " created successfully.");
    
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
            // Prompt the user for the folder path
            System.out.print("Enter the path of the folder containing the data files: ");
            String folderPath = scanner.nextLine();
    
            File folder = new File(folderPath);
            if (!folder.isDirectory()) {
                System.out.println("Invalid folder path!");
                return;
            }
    
            // Get all files in the folder
            File[] files = folder.listFiles();
            if (files == null || files.length == 0) {
                System.out.println("No files found in the specified folder.");
                return;
            }
    
            for (File file : files) {
                try {
                    // Parse the file contents and create the SQL statement
                    String tableName = getTableNameFromFile(file);
                    String sql = generateInsertSql(file, tableName, 4);
    
                    // Execute the SQL statement
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.executeUpdate();
                    System.out.println("Data from file " + file.getName() + " inserted into table " + tableName);
                    stmt.close();
                
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getTableNameFromFile(File file) {
        // Extract the table name from the file name or file contents
        // Example: Assuming the file name is "table_name.csv"
        return file.getName().split("\\.")[0];
    }
    
    private static String generateInsertSql(File file, String tableName, int numColumns) {
        StringBuilder sql = new StringBuilder();
        try (Scanner fileScanner = new Scanner(file)) {
            // Read the file line by line
            
            while (fileScanner.hasNextLine()) {
                
                String line = fileScanner.nextLine();
                // Parse the line and append the values to the SQL statement
                String[] values = line.split(",");
                if (values.length != numColumns) {
                    System.out.println("Error: Number of columns in the data file does not match the table schema.");
                    continue; // Skip this line and move to the next line
                }
                
                sql.append("INSERT INTO ").append(tableName).append(" VALUES (");
                for (String value : values) {
                    sql.append("'").append(value).append("',");
                }
                sql.setLength(sql.length() - 1); // Remove the trailing comma
                sql.append(");"); // Add the semicolon to terminate the SQL statement
                sql.append("\n"); // Add a newline for better readability
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return sql.toString();
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