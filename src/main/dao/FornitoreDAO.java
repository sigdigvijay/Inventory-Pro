package src.main.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FornitoreDAO {

    private static final String FILE_PATH = "data/fornitori.csv";

    // Add supplier
    public static void addSupplier(String[] supplierData) {
        List<String[]> allSuppliers = getAllSuppliers();
        allSuppliers.add(supplierData);
        saveAllSuppliers(allSuppliers);
    }

    // Get all suppliers
    public static List<String[]> getAllSuppliers() {
        List<String[]> suppliers = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return suppliers;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                suppliers.add(line.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    // Save all suppliers
    private static void saveAllSuppliers(List<String[]> suppliers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String[] s : suppliers) {
                bw.write(String.join(",", s));
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update supplier by ID
    public static void updateSupplier(String supplierId, String[] supplierData) {
        List<String[]> suppliers = getAllSuppliers();
        for (int i = 0; i < suppliers.size(); i++) {
            if (suppliers.get(i)[0].equals(supplierId)) {
                suppliers.set(i, supplierData);
                break;
            }
        }
        saveAllSuppliers(suppliers);
    }

    // Delete supplier by ID
    public static void deleteSupplier(String supplierId) {
        List<String[]> suppliers = getAllSuppliers();
        suppliers.removeIf(s -> s[0].equals(supplierId));
        saveAllSuppliers(suppliers);
    }

    // Get supplier by ID
    public static String[] getSupplierById(String supplierId) {
        for (String[] s : getAllSuppliers()) {
            if (s[0].equals(supplierId)) return s;
        }
        return null;
    }
    // Check if supplier exists by VAT
    public static boolean existsByVAT(String vat) {
        for (String[] s : getAllSuppliers()) {
            if (s[2].equals(vat)) return true; // assuming index 2 = partita_iva
        }
        return false;
    }


    // Check if supplier exists by ID
    public static boolean existsById(String supplierId) {
        return getSupplierById(supplierId) != null;
    }

    // Generate unique supplier ID
    public static String generateId() {
        return "SUP-" + System.currentTimeMillis();
    }

    // For testing
    public static void main(String[] args) {
        String id = generateId();
        String[] s = {id, "Fornitore Test", "12345678901", "Via Roma 1", "0123456789", "test@email.com", "Note"};
        addSupplier(s);

        List<String[]> all = getAllSuppliers();
        for (String[] sup : all) {
            System.out.println(String.join(" | ", sup));
        }
    }
}
