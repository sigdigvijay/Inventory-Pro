package src.main.dao;

import src.main.util.CSVHelper;

import java.util.ArrayList;
import java.util.List;

public class OrdineDAO {

    private static final String FILE_PATH_ORDINI = "data/ordini.csv";
    private static final String FILE_PATH_RIGHE = "data/righe_ordine.csv";

    private static final String SEPARATOR = ",";

    // ---------------- Orders ----------------

    public static List<String[]> getAllOrders() {
        return CSVHelper.readCSV(FILE_PATH_ORDINI, SEPARATOR);
    }

    public static void addOrder(String[] orderData) {
        CSVHelper.appendCSV(FILE_PATH_ORDINI, orderData, SEPARATOR);
    }

    public static void updateOrder(String id, String[] updatedData) {
        List<String[]> orders = getAllOrders();
        List<String[]> updatedList = new ArrayList<>();
        for(String[] o : orders) {
            if(o[0].equals(id)) updatedList.add(updatedData);
            else updatedList.add(o);
        }
        CSVHelper.writeCSV(FILE_PATH_ORDINI, updatedList, SEPARATOR);
    }

    public static void deleteOrder(String id) {
        // Delete order lines first
        List<String[]> lines = getAllOrderLines();
        List<String[]> updatedLines = new ArrayList<>();
        for(String[] l : lines) {
            if(!l[0].equals(id)) updatedLines.add(l);
        }
        CSVHelper.writeCSV(FILE_PATH_RIGHE, updatedLines, SEPARATOR);

        // Delete order
        List<String[]> orders = getAllOrders();
        List<String[]> updatedOrders = new ArrayList<>();
        for(String[] o : orders) {
            if(!o[0].equals(id)) updatedOrders.add(o);
        }
        CSVHelper.writeCSV(FILE_PATH_ORDINI, updatedOrders, SEPARATOR);
    }

    public static String generateId() {
        List<String[]> orders = getAllOrders();
        int maxId = 0;
        for(String[] o : orders) {
            int id = Integer.parseInt(o[0]);
            if(id > maxId) maxId = id;
        }
        return String.valueOf(maxId + 1);
    }

    // ---------------- Order Lines ----------------

    public static List<String[]> getAllOrderLines() {
        return CSVHelper.readCSV(FILE_PATH_RIGHE, SEPARATOR);
    }

    public static void addOrderLine(String[] lineData) {
        CSVHelper.appendCSV(FILE_PATH_RIGHE, lineData, SEPARATOR);
    }

    public static List<String[]> getLinesByOrderId(String orderId) {
        List<String[]> allLines = getAllOrderLines();
        List<String[]> result = new ArrayList<>();
        for(String[] l : allLines) {
            if(l[0].equals(orderId)) result.add(l);
        }
        return result;
    }

    public static String generateLineId() {
        List<String[]> lines = getAllOrderLines();
        int maxId = 0;
        for(String[] l : lines) {
            int id = Integer.parseInt(l[0]); // first column is lineId
            if(id > maxId) maxId = id;
        }
        return String.valueOf(maxId + 1);
    }

    // ---------------- Receive Order ----------------

    public static void receiveOrder(String orderId) {
        List<String[]> lines = getLinesByOrderId(orderId);
        for(String[] line : lines) {
            String productId = line[1];
            int qty = Integer.parseInt(line[2]);

            // Update stock
            ProductsDAO.updateStock(productId, qty);

            // Log movement
            String movementId = MovementsDAO.generateId();
            String[] movement = new String[]{
                    movementId, productId, "carico", String.valueOf(qty), "Ricezione ordine",
                    String.valueOf(System.currentTimeMillis()), "", orderId
            };
            MovementsDAO.addMovement(movement);
        }

        // Update order status to "ricevuto"
        List<String[]> orders = getAllOrders();
        for(String[] o : orders) {
            if(o[0].equals(orderId)) o[5] = "ricevuto"; // status column
        }
        CSVHelper.writeCSV(FILE_PATH_ORDINI, orders, SEPARATOR);
    }
}
