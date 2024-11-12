// Red-Black Tree Implementation
public class RedBlackTree {
    private enum Color {RED, BLACK}

    private class Node {
        Product product;
        Node left, right, parent;
        Color color;

        public Node(Product product) {
            this.product = product;
            this.color = Color.RED; // New nodes are always red
            this.left = this.right = this.parent = null;
        }
    }

    private Node root;
    private Node TNULL; // Sentinel node representing leaves

    public RedBlackTree() {
        TNULL = new Node(null); // Sentinel node for leaves
        TNULL.color = Color.BLACK;
        root = TNULL;
    }

    // Insert a new product into the tree
    public void insert(Product product) {
        Node newNode = new Node(product);
        Node y = null;
        Node x = root;

        while (x != TNULL) {
            y = x;
            if (newNode.product.getProductId().compareTo(x.product.getProductId()) < 0) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        newNode.parent = y;
        if (y == null) {
            root = newNode;
        } else if (newNode.product.getProductId().compareTo(y.product.getProductId()) < 0) {
            y.left = newNode;
        } else {
            y.right = newNode;
        }

        newNode.left = TNULL;
        newNode.right = TNULL;
        newNode.color = Color.RED;

        fixInsert(newNode);
    }

    // Fix the tree after insertion to maintain Red-Black properties
    private void fixInsert(Node k) {
        Node u;
        while (k.parent.color == Color.RED) {
            if (k.parent == k.parent.parent.left) {
                u = k.parent.parent.right;
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    rightRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.left;
                if (u.color == Color.RED) {
                    u.color = Color.BLACK;
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = Color.BLACK;
                    k.parent.parent.color = Color.RED;
                    leftRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = Color.BLACK;
    }

    // Left rotate operation
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    // Right rotate operation
    private void rightRotate(Node x) {
        Node y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }

    // Search for a product by its productId
    public Product search(String productId) {
        Node current = root;
        while (current != TNULL) {
            if (productId.equals(current.product.getProductId())) {
                return current.product;
            } else if (productId.compareTo(current.product.getProductId()) < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        return null; // Product not found
    }
}
