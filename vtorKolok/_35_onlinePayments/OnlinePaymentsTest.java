package np.vtor_kolok._35_onlinePayments;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Payment {
    String itemName;
    int price;

    public Payment(String itemName, int price) {
        this.itemName = itemName;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return " " + itemName + " " + price;
    }
}

class OnlinePayments {
    Map<String, List<Payment>> paymentsById;

    OnlinePayments() {
        paymentsById = new HashMap<>();
    }

    public void readItems(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String id = parts[0];
            String itemName = parts[1];
            int price = Integer.parseInt(parts[2]);
            Payment p = new Payment(itemName, price);
            paymentsById.putIfAbsent(id, new ArrayList<>());
            paymentsById.get(id).add(p);
        }
    }

    public int getNeto(String id) {
        return paymentsById.get(id).stream()
                .mapToInt(Payment::getPrice)
                .sum();
    }

    public int getFee(String id) {
        int fee = (int) Math.round(getNeto(id) * 0.0114);
        if (fee < 3) {
            return 3;
        } else if (fee > 300) {
            return 300;
        } else {
            return fee;
        }
    }

    void printStudentReport(String index, OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        if (!paymentsById.containsKey(index)) {
            pw.println("Student " + index + " not found!");
        } else{
            pw.printf("Student: %s Net: %d Fee: %d Total: %d\nItems:\n", index, getNeto(index), getFee(index), getNeto(index)+getFee(index));
            List<Payment> payments = paymentsById.get(index).stream()
                    .sorted(Comparator.comparing(Payment::getPrice).reversed())
                    .collect(Collectors.toList());
            int i =1;
            for (Payment payment:payments){
                pw.println(i + "."+ payment);
                i++;
            }
        }
        pw.flush();
    }
}

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
