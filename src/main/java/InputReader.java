import lombok.Data;
import model.AllBundles;
import model.AllOrders;
import model.EachBundle;
import model.EachOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class InputReader {
    private static final Logger logger = LogManager.getLogger(Calculation.class);

    public AllOrders readInputFile(String fileName) throws IOException {
        AllOrders allOrders = new AllOrders();
        BufferedReader fileReader = readFile(fileName);
        String line;
        while ((line = fileReader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length == 2) {
                allOrders.addOrder(new EachOrder(Integer.parseInt(parts[0]), parts[1]));
            } else {
                throw new IOException("Please check your input format");
            }
        }
        fileReader.close();
        return allOrders;
    }

    public AllBundles readPriceFile(String fileName) throws IOException {
        AllBundles allBundles = new AllBundles();
        BufferedReader fileReader = readFile(fileName);
        String line;
        while ((line = fileReader.readLine()) != null) {
            String formatCode = line.split(" ")[2];
            int startIndex = 4;
            int endIndex = line.split(" ").length;
            String[] sliced_line = Arrays.copyOfRange(line.split(" "), startIndex, endIndex);
            List<String> line_without_symbol = Arrays.stream(sliced_line).filter(x -> !x.equals("@")).collect(Collectors.toList());
            for (int i = 0; i < line_without_symbol.size(); i = i + 2) {
                allBundles.addBundle(new EachBundle(formatCode, Integer.parseInt(line_without_symbol.get(i)), Double.parseDouble(line_without_symbol.get(i + 1).replace("$", ""))));
            }

        }
        return allBundles;
    }

    private BufferedReader readFile(String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader fileReader = null;
        try {// create a new file if the file does not exist
            if (!file.exists()) {
                file.createNewFile();
                logger.info("A new file is created!!!");
            }
            fileReader = new BufferedReader(new java.io.FileReader(file));
        } catch (IOException ex) {
            throw new IOException("There is a problem with the file");
        }
        return fileReader;
    }
}
