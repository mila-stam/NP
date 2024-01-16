package np.zadachi_za_vezhbanje.prv_kolokvium._15_ddv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
        //System.out.println(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

class Item {
    private int price;
    private String type;

    public Item(int price, String type) {
        this.price = price;
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public double cacluateTax() {
        if (type.equals("A")) {
            return price * 0.18*0.15;
        } else if (type.equals("B")) {
            return price * 0.05*0.15;
        } else return 0;
    }
}

class Receipt {
    String id;
    List<Item> items;

    public Receipt(String id, List<Item> items) {
        this.id = id;
        int sum = items.stream().mapToInt(Item::getPrice).sum();
        this.items = items;
    }

    public static Receipt createInstance(String input) throws AmountNotAllowedException {
        String[] parts = input.split("\\s+");
        List<Item> itemList = new ArrayList<>();
        String id = parts[0];

        //Item item = new Item();
        for (int i = 1; i < parts.length; i += 2) {
            itemList.add(new Item(Integer.parseInt(parts[i]), parts[i + 1]));
        }
        int sum = itemList.stream()
                .mapToInt(Item::getPrice)
                .sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        return new Receipt(id, itemList);
    }

    public int totalPrice() {
        return items.stream()
                .mapToInt(Item::getPrice)
                .sum();
    }

    public double taxReturn() {
        return items.stream()
                .mapToDouble(Item::cacluateTax)
                .sum();
    }

    @Override
    public String toString() {
        return String.format("%s %d %.2f", id, totalPrice(), taxReturn());
    }
}

class MojDDV {
    List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        receipts = br.lines().map(line -> {
                    try {
                        return Receipt.createInstance(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        /*for (Receipt receipt : receipts) {
            pw.println(receipt);
        }*/
        receipts.forEach(receipt -> pw.println(receipt.toString()));
        pw.flush();
    }

}

public class MojDDVTest {

    public static void main(String[] args){

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");

        mojDDV.readRecords(System.in);


        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
