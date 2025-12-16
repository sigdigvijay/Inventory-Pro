package src.main.dao;

import java.util.*;
import java.time.LocalDate;

public class CategoriaDAO {

    private static final String FILE_PATH = "data/categories.csv";

    // Get all categories
    public static List<String[]> get_all_categories() {
        return csv_utils.read_csv(FILE_PATH);
    }

    // Add a new category
    public static void add_category(String name, String description, String parent_id, String color) {
        List<String[]> categories = get_all_categories();
        int id = csv_utils.get_next_id(categories);
        String created_at = LocalDate.now().toString();
        if(color == null || color.isEmpty()) color = "#3498db"; // default color
        String[] newCategory = {String.valueOf(id), name, description, parent_id, color, created_at};
        categories.add(newCategory);
        csv_utils.write_csv(FILE_PATH, categories);
    }

    // Update existing category by ID
    public static void update_category(String id, String name, String description, String parent_id, String color) {
        List<String[]> categories = get_all_categories();
        for(String[] c : categories) {
            if(c[0].equals(id)) {
                c[1] = name;
                c[2] = description;
                c[3] = parent_id;
                c[4] = color != null ? color : "#3498db";
                break;
            }
        }
        csv_utils.write_csv(FILE_PATH, categories);
    }

    // Delete category by ID
    public static void delete_category(String id) {
        List<String[]> categories = get_all_categories();
        csv_utils.remove_by_id(categories, id);
        csv_utils.write_csv(FILE_PATH, categories);
    }

    // Find category by ID
    public static String[] get_category_by_id(String id) {
        List<String[]> categories = get_all_categories();
        return csv_utils.find_by_id(categories, id);
    }

    // Get categories with a specific parent
    public static List<String[]> get_subcategories(String parent_id) {
        List<String[]> categories = get_all_categories();
        List<String[]> result = new ArrayList<>();
        for(String[] c : categories) {
            if(parent_id == null) {
                if(c[3] == null || c[3].isEmpty()) result.add(c);
            } else {
                if(parent_id.equals(c[3])) result.add(c);
            }
        }
        return result;
    }

    // Optional: Search categories by name (partial)
    public static List<String[]> search_categories(String query) {
        List<String[]> categories = get_all_categories();
        List<String[]> result = new ArrayList<>();
        for(String[] c : categories) {
            if(c[1].toLowerCase().contains(query.toLowerCase())) result.add(c);
        }
        return result;
    }
}
