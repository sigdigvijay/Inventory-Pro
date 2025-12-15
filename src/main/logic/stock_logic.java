package src.main.logic;

import src.main.dao.inventory_dao;
import src.main.dao.ProductsDAO;

import java.util.*;

public class stock_logic {

    // Get current stock of a product
    public static int get_stock(String product_id) {
        return inventory_dao.get_quantity(product_id);
    }

    // Update stock by adding or removing quantity
    public static void update_stock(String product_id, int change) {
        if(change >= 0) {
            inventory_dao.add_stock(product_id, change);
        } else {
            inventory_dao.remove_stock(product_id, -change);
        }
    }

    // Check if stock is below minimum threshold
    public static boolean is_below_minimum(String productID) {
        String[] product = ProductsDAO.getProductById(productID);
        if(product == null) return false;
        try {
            int min_stock = Integer.parseInt(product[8]); // min_stock column
            int current_stock = get_stock(productID);
            return current_stock < min_stock;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    // Get list of products below minimum stock
    public static List<String[]> get_low_stock_products() {
        List<String[]> allProducts = ProductsDAO.getAllProducts();
        List<String[]> lowStock = new ArrayList<>();
        for(String[] p : allProducts) {
            if(is_below_minimum(p[0])) {
                lowStock.add(p);
            }
        }
        return lowStock;
    }
}
