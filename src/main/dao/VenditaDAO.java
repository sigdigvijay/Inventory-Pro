package src.main.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class VenditaDAO {

    private static final String FILE_PATH = "data/vendite.csv";

    // Add a sale
    public static void addSale(String[] saleData) {
        List<String[]> allSales = getAllSales();
        allSales.add(saleData);
        saveAllSales(allSales);
    }

    // Get all sales
    public static List<String[]> getAllSales() {
        List<String[]> sales = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return sales;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                sales.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sales;
    }

    // Save all sales to CSV
    private static void saveAllSales(List<String[]> sales) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] s : sales) {
                bw.write(String.join(",", s));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Record a sale and update product stock
    public static void recordSale(String saleId, String productId, int quantity, double salePrice, String customer) {
        long timestamp = System.currentTimeMillis();
        String[] saleData = new String[]{
                saleId, productId, String.valueOf(quantity), String.valueOf(salePrice),
                String.valueOf(timestamp), customer != null ? customer : ""
        };

        addSale(saleData);

        // Update stock
        String[] product = ProductsDAO.getProductById(productId);
        if (product != null) {
            int stock = Integer.parseInt(product[6]);
            stock -= quantity;
            product[6] = String.valueOf(stock);
            ProductsDAO.updateProduct(product[0], product);
        }
    }

    // Generate unique sale ID
    public static String generateId() {
        return "SALE-" + System.currentTimeMillis();
    }
}
