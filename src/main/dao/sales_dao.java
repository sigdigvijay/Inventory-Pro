package src.main.dao;

import java.util.*;
import java.time.LocalDateTime;
import src.main.logic.stock_logic;

public class sales_dao {

    private static final String FILE_PATH = "data/sales.csv";

    // Get all sales
    public static List<String[]> get_all_sales() {
        return csv_utils.read_csv(FILE_PATH);
    }

    // Add a new sale
    public static void add_sale(String product_id, int quantity, String sale_price, String client) {
        List<String[]> sales = get_all_sales();
        int id = csv_utils.get_next_id(sales);
        String dateTime = LocalDateTime.now().toString();
        String[] newSale = {String.valueOf(id), product_id, String.valueOf(quantity), sale_price, dateTime, client};
        sales.add(newSale);
        csv_utils.write_csv(FILE_PATH, sales);

        // Update stock
        stock_logic.update_stock(product_id, -quantity);

        // Log movement
        String movementId = MovementsDAO.generateId();
        String[] movement = new String[] {
                movementId,              // movement id
                product_id,              // product id
                String.valueOf(quantity),// quantity
                "scarico",               // type
                "Vendita",               // causale
                client                   // note
        };
        MovementsDAO.addMovement(movement);
    }

    // Delete a sale by ID
    public static void delete_sale(String id) {
        List<String[]> sales = get_all_sales();
        String[] sale = csv_utils.find_by_id(sales, id);
        if(sale != null) {
            int quantity = Integer.parseInt(sale[2]);
            String product_id = sale[1];

            // Restore stock
            stock_logic.update_stock(product_id, quantity);

            // Remove sale record
            csv_utils.remove_by_id(sales, id);
            csv_utils.write_csv(FILE_PATH, sales);

            // Optionally log reversal movement
            String movementId = MovementsDAO.generateId();
            String[] movement = new String[] {
                    movementId,
                    product_id,
                    String.valueOf(quantity),
                    "carico",
                    "Cancellazione vendita",
                    ""
            };
            MovementsDAO.addMovement(movement);
        }
    }

    // Get sale by ID
    public static String[] get_sale_by_id(String id) {
        List<String[]> sales = get_all_sales();
        return csv_utils.find_by_id(sales, id);
    }

    // Get sales for a product
    public static List<String[]> get_sales_by_product(String product_id) {
        List<String[]> sales = get_all_sales();
        List<String[]> result = new ArrayList<>();
        for(String[] s : sales) {
            if(s[1].equals(product_id)) result.add(s);
        }
        return result;
    }

    // Optional: Filter sales by date range
    public static List<String[]> get_sales_by_date(String start, String end) {
        List<String[]> sales = get_all_sales();
        List<String[]> result = new ArrayList<>();
        for(String[] s : sales) {
            String dateTime = s[4]; // dateTime column
            if(dateTime.compareTo(start) >= 0 && dateTime.compareTo(end) <= 0) {
                result.add(s);
            }
        }
        return result;
    }
}
