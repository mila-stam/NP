package np.zadachi_za_vezhbanje.prv_kolokvium._25_shoppingCart;


import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    PS, WS
}

class InvalidOperationException extends Exception {
    public InvalidOperationException(String mess) {
        super(mess);
    }
}

class Product implements Comparable<Product> {
    public TYPE type;
    public Integer id;
    public String name;
    public Integer price;
    public Double quantity;

    public Product(String line) throws InvalidOperationException {
        String[] parts = line.split("[;]");
        if (Double.parseDouble(parts[4]) == 0) {
            throw new InvalidOperationException(String.format("The quantity of the product with id %d can not be 0.", Integer.parseInt(parts[1])));
        }
        this.type = TYPE.valueOf(parts[0]);
        this.id = Integer.parseInt(parts[1]);
        this.name = parts[2];
        this.price = Integer.parseInt(parts[3]);
        this.quantity = Double.parseDouble(parts[4]);
    }

    public Integer getId() {
        return id;
    }

    public Double totalPrice() {
        if (type == TYPE.WS) {
            return price * quantity;
        } else {
            return (quantity / 1000) * price;
        }
    }

    @Override
    public int compareTo(Product o) {
        return totalPrice().compareTo(o.totalPrice());
    }

    @Override
    public String toString() {
        return String.format("%d - %.2f", id, totalPrice());
    }
}

class ShoppingCart {
    List<Product> products;

    public ShoppingCart() {
        products = new ArrayList<>();
    }

    public void addItem(String itemData) throws InvalidOperationException {
        products.add(new Product(itemData));
    }

    public void printShoppingCart(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        products.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(pw::println);
        pw.flush();
    }

    public void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if (discountItems.isEmpty()){
            throw new InvalidOperationException("There are no products with discount.");
        }
        List<Product> productsOnSale = new ArrayList<>();
        for (Product p : products) {
            for (Integer i : discountItems) {
                if(p.getId().equals(i)){
                    productsOnSale.add(p);
                }
            }
        }

        PrintWriter pw = new PrintWriter(os);
        for (Product p : productsOnSale){
            pw.println(String.format("%d - %.2f", p.getId(), p.totalPrice()*0.1));
        }
        pw.flush();
    }
}

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}