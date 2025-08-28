import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class FileHandler {

    private FileHandler() {
        // Private constructor to prevent instantiation
    }

    public static <T> List<T> loadFromFile(String fileName, Function<String, T> converter) {
        List<T> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                T item = converter.apply(line);
                if (item != null) {
                    data.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
        }
        return data;
    }

    public static <T> void saveToFile(String fileName, List<T> data, Function<T, String> converter) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (T item : data) {
                String line = converter.apply(item);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + fileName);
        }
    }
}
