package src.main.logic;

import java.util.*;

import src.main.dao.sales_dao;
import src.main.dao.ProductsDAO; // ✅


public class statistics_calculator {

    // Total revenue for all sales
    public static double get_total_revenue() {
        List<String[]> sales = sales_dao.get_all_sales();
        double total = 0.0;
        for(String[] s : sales) {
            try {
                int qty = Integer.parseInt(s[2]);
                double price = Double.parseDouble(s[3]);
                total += qty * price;
            } catch(NumberFormatException e) {}
        }
        return total;
    }

    // Total margin per product: (sale_price - purchase_price) * quantity
    public static double get_margin_for_product(String product_id) {
        String[] product = ProductsDAO.getProductById(product_id);
        if(product == null) return 0;
        double purchase_price = Double.parseDouble(product[6]);
        List<String[]> sales = sales_dao.get_sales_by_product(product_id);
        double margin = 0.0;
        for(String[] s : sales) {
            try {
                int qty = Integer.parseInt(s[2]);
                double sale_price = Double.parseDouble(s[3]);
                margin += (sale_price - purchase_price) * qty;
            } catch(NumberFormatException e) {}
        }
        return margin;
    }

    // Total margin for all products
    // Total margin for all products
    public static double get_total_margin() {
        double totalMargin = 0.0;

        // Loop over all products
        for (String[] product : ProductsDAO.getAllProducts()) {
            String productId = product[0]; // index 0 = product ID
            totalMargin += get_margin_for_product(productId);
        }

        return totalMargin;
    }


    // Top N selling products by quantity
    public static List<String[]> get_top_selling_products(int topN) {
        Map<String, Integer> productSales = new HashMap<>();
        List<String[]> sales = sales_dao.get_all_sales();
        for(String[] s : sales) {
            String product_id = s[1];
            int qty = 0;
            try {
                qty = Integer.parseInt(s[2]);
            } catch(NumberFormatException e) {}
            productSales.put(product_id, productSales.getOrDefault(product_id, 0) + qty);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(productSales.entrySet());
        list.sort((a,b) -> b.getValue() - a.getValue()); // descending

        List<String[]> result = new ArrayList<>();
        for(int i=0; i<Math.min(topN, list.size()); i++) {
            String product_id = list.get(i).getKey();
            int qty = list.get(i).getValue();
            String[] product = ProductsDAO.getProductById(product_id);
            if(product != null) {
                result.add(new String[]{product_id, product[2], String.valueOf(qty)}); // id, name, qty sold
            }
        }
        return result;
    }

    // Least selling products by quantity
    public static List<String[]> get_least_selling_products(int bottomN) {
        Map<String, Integer> productSales = new HashMap<>();
        List<String[]> sales = sales_dao.get_all_sales();
        for(String[] s : sales) {
            String product_id = s[1];
            int qty = 0;
            try { qty = Integer.parseInt(s[2]); } catch(NumberFormatException e) {}
            productSales.put(product_id, productSales.getOrDefault(product_id, 0) + qty);
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>(productSales.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue)); // ascending

        List<String[]> result = new ArrayList<>();
        for(int i=0; i<Math.min(bottomN, list.size()); i++) {
            String product_id = list.get(i).getKey();
            int qty = list.get(i).getValue();
            String[] product = ProductsDAO.getProductById(product_id);
            if(product != null) {
                result.add(new String[]{product_id, product[2], String.valueOf(qty)}); // id, name, qty sold
            }
        }
        return result;
    }
}
