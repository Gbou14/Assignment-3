import java.io.*;
import java.util.*;
public class ProductManagement {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();

        // Load the file from resources
        String fileName = "amazon-product-data.csv";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                ProductManagement.class.getClassLoader().getResourceAsStream(fileName)))) {

            if (br == null) {
                throw new FileNotFoundException("Resource file '" + fileName + "' not found.");
            }

            String line;
            boolean isFirstLine = true; // Flag to skip the header
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the first line (header)
                    continue;
                }

                String[] values = line.split(","); // Assuming CSV fields are separated by commas

                // Ensure the row has at least 4 fields
                if (values.length < 4) {
                    System.err.println("Skipping malformed row: " + Arrays.toString(values));
                    continue;
                }

                String productId = values[0].trim();
                String name = values[1].trim();
                String category = values[2].trim();
                String price;

                // New parser logic to handle invalid price values and "$" symbol
                try {
                    String priceString = values[3].trim(); // Extract the price field

                    // Remove $ symbol if present
                    if (priceString.startsWith("$")) {
                        priceString = priceString.substring(1); // Strip the $ symbol
                    }

                    try {
                        Double.parseDouble(priceString); // Attempt to parse as a number
                        price = "$" + priceString; // Re-add the $ symbol for display
                    } catch (NumberFormatException e) {
                        price = "Invalid Price"; // Set to "Invalid Price" for non-numeric values
                    }

                    Product product = new Product(productId, name, category, price);
                    tree.insert(product);
                } catch (Exception e) {
                    System.err.println("Error processing product: " + Arrays.toString(values));
                    e.printStackTrace();
                }
            }

            System.out.println("Products loaded successfully!");

        } catch (FileNotFoundException e) {
            System.err.println("Error: The resource file '" + fileName + "' was not found.");
            return; // Exit the program
        } catch (IOException e) {
            System.err.println("Error reading the resource file: " + fileName);
            e.printStackTrace();
            return; // Exit the program
        }

        // Example of searching for a product
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter product ID to search: ");
        String productId = scanner.nextLine();
        Product result = tree.search(productId);
        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("Product not found.");
        }
    }
}

