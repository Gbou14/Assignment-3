import java.io.*;
import java.util.*;

public class ProductManagement {
    public static void main(String[] args) throws IOException {
        RedBlackTree tree = new RedBlackTree();

        // Read products from the CSV file and insert into the tree
        String fileName = "amazon-product-data.csv";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            String productId = values[0];
            String name = values[1];
            String category = values[2];
            double price = Double.parseDouble(values[3]);

            Product product = new Product(productId, name, category, price);
            tree.insert(product);
        }
        br.close();

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
