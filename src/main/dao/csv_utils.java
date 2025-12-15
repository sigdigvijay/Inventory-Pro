package src.main.dao;

import java.io.*;
import java.util.*;

public class csv_utils {

    // Read CSV into List of String arrays
    public static List<String[]> read_csv(String filePath) {
        List<String[]> data = new ArrayList<>();
        File file = new File(filePath);
        if(!file.exists()) {
            // Create file with empty content
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Errore creazione CSV: " + filePath);
            }
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                data.add(line.split(",", -1)); // keep empty fields
            }
        } catch (IOException e) {
            System.err.println("Errore lettura CSV: " + filePath);
        }
        return data;
    }

    // Write List of String arrays to CSV
    public static void write_csv(String filePath, List<String[]> data) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {
                for(String[] row : data) {
                    pw.println(String.join(",", row));
                }
            }
        } catch (IOException e) {
            System.err.println("Errore scrittura CSV: " + filePath);
        }
    }

    // Generate next ID (assume first column is ID)
    public static int get_next_id(List<String[]> data) {
        int max = 0;
        for(String[] row : data) {
            try {
                int id = Integer.parseInt(row[0]);
                if(id > max) max = id;
            } catch(Exception ignored) {}
        }
        return max + 1;
    }

    // Utility to find row by first column (ID)
    public static String[] find_by_id(List<String[]> data, String id) {
        for(String[] row : data) {
            if(row.length > 0 && row[0].equals(id)) return row;
        }
        return null;
    }

    // Utility to remove row by first column (ID)
    public static void remove_by_id(List<String[]> data, String id) {
        data.removeIf(row -> row.length > 0 && row[0].equals(id));
    }
}
