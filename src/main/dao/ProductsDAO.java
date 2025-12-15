package src.main.dao;

import src.main.util.CSVHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductsDAO {

    private static final String FILE_PATH = "data/prodotti.csv";
    private static final String SEPARATOR = ",";

    /**
     * Get all products
     * @return List of String arrays representing products
     */
    public static List<String[]> getAllProducts() {
        return CSVHelper.readCSV(FILE_PATH, SEPARATOR);
    }

    /**
     * Add a new product
     * @param productData String array: id, code, name, description, salePrice, purchasePrice, stock, minStock, maxStock, image, dateInserted
     */
    public static void addProduct(String[] productData) {
        CSVHelper.appendCSV(FILE_PATH, productData, SEPARATOR);
    }

    /**
     * Update a product by ID
     */
    public static void updateProduct(String id, String[] updatedData) {
        List<String[]> products = getAllProducts();
        List<String[]> updatedList = new ArrayList<>();
        for(String[] p : products) {
            if(p[0].equals(id)) {
                updatedList.add(updatedData);
            } else {
                updatedList.add(p);
            }
        }
        CSVHelper.writeCSV(FILE_PATH, updatedList, SEPARATOR);
    }

    // Get single product by ID
    public static String[] getProductById(String productId) {
        for (String[] product : getAllProducts()) {
            if (product[0].equals(productId)) { // assuming index 0 = id
                return product;
            }
        }
        return null;
    }

    // Get current stock/quantity for a product by ID
    public static int getStock(String productId) {
        String[] product = getProductById(productId);
        if (product == null) return 0;
        try {
            return Integer.parseInt(product[7]); // index 7 = giacenza in CSV
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getMinStock(String productId) {
        List<String[]> products = getAllProducts(); // your method to read all products
        for (String[] p : products) {
            if (p[0].equals(productId)) { // assuming column 0 is productId
                try {
                    return Integer.parseInt(p[8]); // assuming column 8 is scorta_minima
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0; // default if not found
    }

    /**
     * Delete a product by ID
     */
    public static void deleteProduct(String id) {
        List<String[]> products = getAllProducts();
        List<String[]> updatedList = new ArrayList<>();
        for(String[] p : products) {
            if(!p[0].equals(id)) {
                updatedList.add(p);
            }
        }
        CSVHelper.writeCSV(FILE_PATH, updatedList, SEPARATOR);
    }

    /**
     * Check if product code exists
     */
    public static boolean existsByCode(String code) {
        List<String[]> products = getAllProducts();
        for(String[] p : products) {
            if(p[1].equals(code)) return true;
        }
        return false;
    }

    /**
     * Generate new product ID
     */
    public static String generateId() {
        List<String[]> products = getAllProducts();
        int maxId = 0;
        for(String[] p : products) {
            int id = Integer.parseInt(p[0]);
            if(id > maxId) maxId = id;
        }
        return String.valueOf(maxId + 1);
    }

    /**
     * Update stock for a product
     * @param productId ID of the product
     * @param delta Quantity change (+/-)
     */
    public static void updateStock(String productId, int delta) {
        List<String[]> products = getAllProducts();
        List<String[]> updatedList = new ArrayList<>();
        for(String[] p : products) {
            if(p[0].equals(productId)) {
                int stock = Integer.parseInt(p[6]);
                stock += delta;
                p[6] = String.valueOf(stock);
            }
            updatedList.add(p);
        }
        CSVHelper.writeCSV(FILE_PATH, updatedList, SEPARATOR);
    }
}
