import java.io.*;
import java.util.ArrayList;
import java.util.function.Function;

public class FileHandler {

    public static <T> void saveToFile(String filename, ArrayList<T> list, Function<T, String> formatter) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (T item : list) {
                bw.write(formatter.apply(item));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to" + filename + ":" + e.getMessage());
        }
    }

    public static <T> ArrayList<T> loadFromFile(String filename, Function<String, T> parser){
        ArrayList<T> list = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while((line = br.readLine()) != null) {
                T item = parser.apply(line);
                if (item != null) {
                    list.add(item);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(filename + "not found.");
        } catch (IOException e) {
            System.out.println("Error reading from" + filename + ": " + e.getMessage());
        }
        return list;

    }

}
