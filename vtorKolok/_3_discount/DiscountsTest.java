package np.vtor_kolok._3_discount;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Price{
    int price;
    int discounted;

    public int getPrice() {
        return Math.abs(price-discounted);
    }

    public int getDiscounted() {
        return (price - discounted) * 100 / price;
    }

    public Price(int price, int discounted) {
        this.price = price;
        this.discounted = discounted;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", getDiscounted(), discounted, price);
    }
}
class Store{
    String name;
    List<Price> prices;

    public Store(String name, List<Price> prices) {
        this.name = name;
        this.prices=prices;
    }

    public String getName() {
        return name;
    }

    public double avgDiscount(){
        return prices.stream()
                .mapToInt(Price::getDiscounted)
                .average()
                .orElse(0);
    }
    public int totalDiscount(){
        return prices.stream()
                .mapToInt(Price::getPrice)
                .sum();
    }
    public List<Price> sort(){
        return prices.stream()
                .sorted(Comparator.comparing(Price::getDiscounted).thenComparing(Price::getPrice).reversed())
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Price> result = sort();
        sb.append(name).append("\n");
        sb.append(String.format("Average discount: %.1f%%\n", avgDiscount()));
        sb.append(String.format("Total discount: %d\n", totalDiscount()));
        for (int i=0; i< result.size()-1;i++){
            sb.append(result.get(i).toString()).append("\n");
        }
        sb.append(result.getLast());
        return sb.toString();
    }
}
class Discounts{
    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            String name = parts[0];
            List<Price> prices = new ArrayList<>();

            for (int i=1; i< parts.length; i++){
                String [] curr = parts[i].split(":");
                int price = Integer.parseInt(curr[1]);
                int disc = Integer.parseInt(curr[0]);
                prices.add(new Price(price, disc));
            }
            stores.add(new Store(name, prices));
        }
        return stores.size();
    }
    public List<Store> byAverageDiscount(){
        return stores.stream()
                .sorted(Comparator.comparing(Store::avgDiscount).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        return stores.stream()
                .sorted(Comparator.comparing(Store::totalDiscount))
                .limit(3)
                .collect(Collectors.toList());
    }
}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
