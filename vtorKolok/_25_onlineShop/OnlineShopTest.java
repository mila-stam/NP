package np.vtor_kolok._25_onlineShop;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String id) {
        super(String.format("Product with id %s does not exist in the online shop!", id));
    }
}

class ComparatorBuilder{
    public static Comparator<Product> buildComparator(COMPARATOR_TYPE type){
        switch (type) {
            case NEWEST_FIRST :
                return Comparator.comparing(Product::getCreatedAt).reversed();
            case OLDEST_FIRST :
                return Comparator.comparing(Product::getCreatedAt);
            case LOWEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice);
            case HIGHEST_PRICE_FIRST:
                return Comparator.comparing(Product::getPrice).reversed();
            case MOST_SOLD_FIRST:
                return Comparator.comparing(Product::getNumOfSoldPieces).reversed();
            case LEAST_SOLD_FIRST:
                return Comparator.comparing(Product::getNumOfSoldPieces);
            default: return null;
        }
    }
}
class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;
    int numOfSoldPieces;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public int getNumOfSoldPieces() {
        return numOfSoldPieces;
    }

    public void increaseNumSold(int quantity){
        numOfSoldPieces+=quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold=" + numOfSoldPieces +
                '}';
    }
}


class OnlineShop {
    Map<String, List<Product>> productsByCategory;
    Map<String, Product> idToProduct;

    OnlineShop() {
        productsByCategory = new HashMap<>();
        idToProduct = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product p = new Product(category, id, name, createdAt, price);
        productsByCategory.putIfAbsent(category, new ArrayList<>());
        productsByCategory.get(category).add(p);
        idToProduct.put(id, p);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if (!idToProduct.containsKey(id)){
            throw new ProductNotFoundException(id);
        }
        idToProduct.get(id).increaseNumSold(quantity);
        return idToProduct.get(id).getPrice()*quantity;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        Comparator<Product> comp = ComparatorBuilder.buildComparator(comparatorType);
        List<List<Product>> result = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        if (category != null) {
            products = productsByCategory.get(category).stream().sorted(comp).collect(Collectors.toList());
        } else{
            products = idToProduct.values().stream().sorted(comp).collect(Collectors.toList());
        }
        for (int i = 0; i < products.size(); i+= pageSize) {
            int endIdx = Math.min(i + pageSize, products.size());
            List<Product> subList = new ArrayList<>(products.subList(i, endIdx));
            result.add(subList);
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


