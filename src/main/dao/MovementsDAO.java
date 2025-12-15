package src.main.dao;

import java.util.*;
import java.io.*;
import src.main.util.CSVHelper;

public class MovementsDAO {

    private static final String CSV_FILE = "data/movements.csv";

    // Add movement using individual parameters
    public static void addMovement(String productId, String type, int quantity, String reason, String orderId) {
        String date = new Date().toString(); // current timestamp
        String[] movement = {productId, type, String.valueOf(quantity), reason, orderId, date};
        CSVHelper.appendToCSV(CSV_FILE, movement);
    }

    // Generate a unique movement ID
    public static String generateId() {
        return "M" + System.currentTimeMillis(); // e.g., M1699500000000
    }


    // Overload: Add movement using String array
    public static void addMovement(String[] movement) {
        // movement array should be: {productId, type, quantity, reason, orderId, date (optional)}
        String productId = movement[0];
        String type = movement[1];
        int quantity = Integer.parseInt(movement[2]);
        String reason = movement[3];
        String orderId = movement[4];
        String date = movement.length > 5 ? movement[5] : new Date().toString();

        String[] movementData = {productId, type, String.valueOf(quantity), reason, orderId, date};
        CSVHelper.appendToCSV(CSV_FILE, movementData);
    }

    // Optional: Get all movements
    public static List<String[]> getAllMovements() {
        return CSVHelper.readCSV(CSV_FILE);
    }

}
