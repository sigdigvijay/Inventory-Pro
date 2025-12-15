package src.main.dao;

import java.util.*;

public class inventory_dao {

    private static final String FILE_PATH = "data/inventory.csv";

    // Get all inventory records
    public static List<String[]> get_all_inventory() {
        return csv_utils.read_csv(FILE_PATH);
    }

    // Get quantity for a product by product_id
    public static int get_quantity(String product_id) {
        List<String[]> inventory = get_all_inventory();
        for(String[] row : inventory) {
            if(row[0].equals(product_id)) {
                try {
                    return Integer.parseInt(row[1]);
                } catch(NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    // Set quantity for a product
    public static void set_quantity(String product_id, int quantity) {
        List<String[]> inventory = get_all_inventory();
        boolean found = false;
        for(String[] row : inventory) {
            if(row[0].equals(product_id)) {
                row[1] = String.valueOf(quantity);
                found = true;
                break;
            }
        }
        if(!found) {
            inventory.add(new String[]{product_id, String.valueOf(quantity)});
        }
        csv_utils.write_csv(FILE_PATH, inventory);
    }

    // Increase quantity
    public static void add_stock(String product_id, int amount) {
        int current = get_quantity(product_id);
        set_quantity(product_id, current + amount);
    }

    // Decrease quantity
    public static void remove_stock(String product_id, int amount) {
        int current = get_quantity(product_id);
        set_quantity(product_id, current - amount);
    }
}
