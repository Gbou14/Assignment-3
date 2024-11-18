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
            boolean isFirstLine = true; 
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                String[] values = parseCSVLine(line);

                if (values.length < 4) {
                    System.err.println("Skipping malformed row: " + Arrays.toString(values));
                    continue;
                }

                String productId = values[0].trim();
                String name = values[1].trim();
                String category = values[2].trim();
                String price;
                
                try {
                    String priceString = values[3].trim(); 

                    if (priceString.startsWith("$")) {
                        priceString = priceString.substring(1); 
                    }

                    priceString = priceString.replace(",", ""); 

                    Product product = new Product(productId, name, category, "$" + priceString);
                    tree.insert(product);

                } catch (Exception e) {
                    System.err.println("Error processing product: " + Arrays.toString(values));
                    e.printStackTrace();
                }
            }

            System.out.println("Products loaded successfully!");

        } catch (FileNotFoundException e) {
            System.err.println("Error: The resource file '" + fileName + "' was not found.");
            return; 
        } catch (IOException e) {
            System.err.println("Error reading the resource file: " + fileName);
            e.printStackTrace();
            return; 
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Insert a new product");
            System.out.println("2. Search for a product");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 1) {
                System.out.print("Enter Product ID: ");
                String productId = scanner.nextLine();
                System.out.print("Enter Product Name: ");
                String name = scanner.nextLine();
                System.out.print("Enter Product Category: ");
                String category = scanner.nextLine();
                System.out.print("Enter Product Price (e.g., $49.00): ");
                String price = scanner.nextLine();

                Product newProduct = new Product(productId, name, category, price);

                try {
                    tree.insert(newProduct); 
                    System.out.println("Product inserted successfully!");
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage()); 
                }
            } else if (choice == 2) {
                // Search for a product
                System.out.print("Enter product ID to search: ");
                String productId = scanner.nextLine();
                Product result = tree.search(productId);
                if (result != null) {
                    System.out.println(result);
                } else {
                    System.out.println("Product not found.");
                }
            } else if (choice == 3) {
              
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice. Please choose again.");
            }
        }

        scanner.close();
    }
    public static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"' && (currentField.length() == 0 || currentField.charAt(currentField.length() - 1) != '\\')) {
                inQuotes = !inQuotes; 
            } else if (c == ',' && !inQuotes) {
                result.add(currentField.toString().trim());
                currentField = new StringBuilder(); 
            } else {
                currentField.append(c);
            }
        }

        if (currentField.length() > 0) {
            result.add(currentField.toString().trim());
        }

        return result.toArray(new String[0]);
    }
}

