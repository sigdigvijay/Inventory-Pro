package src.main.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    private static final String DEFAULT_SEPARATOR = ",";

    /**
     * Read CSV file with custom separator
     */
    public static List<String[]> readCSV(String filePath, String separator) {
        List<String[]> rows = new ArrayList<>();
        File file = new File(filePath);

        if(!file.exists()) {
            try {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return rows;
            }
        }

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line = br.readLine()) != null) {
                String[] data = line.split(separator, -1);
                rows.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

    /**
     * Read CSV with default separator
     */
    public static List<String[]> readCSV(String filePath) {
        return readCSV(filePath, DEFAULT_SEPARATOR);
    }

    /**
     * Append a row with custom separator
     */
    public static void appendCSV(String filePath, String[] row, String separator) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(String.join(separator, row));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Append a row with default separator
     */
    public static void appendToCSV(String filePath, String[] row) {
        appendCSV(filePath, row, DEFAULT_SEPARATOR);
    }

    /**
     * Overwrite CSV with custom separator
     */
    public static void writeCSV(String filePath, List<String[]> data, String separator) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for(String[] row : data) {
                bw.write(String.join(separator, row));
                bw.newLine();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overwrite CSV with default separator
     */
    public static void writeCSV(String filePath, List<String[]> data) {
        writeCSV(filePath, data, DEFAULT_SEPARATOR);
    }
}
