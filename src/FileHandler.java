import java.io.*;
import java.util.*;
import java.util.function.Function;

public class FileHandler {

    // Updated to accept any List type, not just ArrayList
    public static <T> void saveToFile(String filename, List<T> list, Function<T, String> formatter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (T item : list) {
                bw.write(formatter.apply(item));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to " + filename + ": " + e.getMessage());
        }
    }

    // Updated to return ArrayList for backward compatibility
    public static <T> ArrayList<T> loadFromFile(String filename, Function<String, T> parser) {
        ArrayList<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                T item = parser.apply(line);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(filename + " not found. Creating new file.");
        } catch (IOException e) {
            System.out.println("Error reading from " + filename + ": " + e.getMessage());
        }
        return list;
    }

    // Additional overload for simple string lists
    public static void saveStringsToFile(String filename, List<String> lines) {
        saveToFile(filename, lines, s -> s);
    }

    // Additional overload for simple string loading
    public static ArrayList<String> loadStringsFromFile(String filename) {
        return loadFromFile(filename, s -> s);
    }
}