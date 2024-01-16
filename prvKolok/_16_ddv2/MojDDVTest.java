package np.zadachi_za_vezhbanje.prv_kolokvium._16_ddv2;

import java.io.*;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(int sum) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", sum));
    }
}

enum TYPE {
    A, B, V
}

class Item {
    public Integer price;
    public TYPE type;

    public Item(Integer price, TYPE type) {
        this.price = price;
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }
    public double taxReturn(){
        if (type.equals(TYPE.A)){
            return price*0.18*0.15;
        } else if (type.equals(TYPE.B)) {
            return price*0.05*0.15;
        }else return 0;
    }
}

class Record {
    public String id;
    public List<Item> items;

    public Record(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");

        List<Item> tempItems = new ArrayList<>();
        for (int i = 1; i < parts.length; i += 2) {
            tempItems.add(new Item(Integer.parseInt(parts[i]), TYPE.valueOf(parts[i + 1])));
        }
        int sum = tempItems.stream().mapToInt(Item::getPrice).sum();
        if (sum > 30000) {
            throw new AmountNotAllowedException(sum);
        }
        this.id = parts[0];
        items = new ArrayList<>(tempItems);
    }
    public int sumAmounts(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    public double totalTax(){
        return items.stream().mapToDouble(Item::taxReturn).sum();
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, sumAmounts(), totalTax());
    }
}

class MojDDV {
    List<Record> records;

    public MojDDV() {
        records = new ArrayList<>();
    }

    public void readRecords(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        records = br.lines().map(line -> {
                    try {
                        return new Record(line);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public void printTaxReturns(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        records.forEach(pw::println);
        pw.flush();
    }
    public void printStatistics (OutputStream outputStream){
        DoubleSummaryStatistics dss = records.stream().mapToDouble(Record::totalTax).summaryStatistics();
        PrintWriter pw = new PrintWriter(outputStream);
        pw.println(String.format("min:\t%5.3f",dss.getMin()));
        pw.println(String.format("max:\t%5.3f",dss.getMax()));
        pw.println(String.format("sum:\t%5.3f",dss.getSum()));
        pw.println(String.format("count:\t%-5d",dss.getCount()));
        pw.println(String.format("avg:\t%5.3f",dss.getAverage()));
        pw.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}
