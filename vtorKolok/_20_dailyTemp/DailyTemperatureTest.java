package np.vtor_kolok._20_dailyTemp;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Temp {
    int temp;
    String type;

    public Temp(String t) {
        this.temp = Integer.parseInt(t.substring(0, t.length() - 1));
        this.type = t.substring(t.length() - 1);
    }

    public double getTempByType(char t) {
        if (type.equals("C") && t == 'F') {
            return ((temp * 9.0) / 5) + 32;
        } else if (type.equals("F") && t == 'C') {
            return ((temp - 32) * 5.0) / 9;
        } else {
            return temp * 1.0;
        }
    }
}

class DailyTemperatures {
    Map<Integer, List<Temp>> tempByDay;

    DailyTemperatures() {
        tempByDay = new TreeMap<>();
    }

    public void readTemperatures(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = br.lines().collect(Collectors.toList());
        for (String line : lines) {
            String[] parts = line.split("\\s+");
            int day = Integer.parseInt(parts[0]);
            List<Temp> temps = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                temps.add(new Temp(parts[i]));
            }
            tempByDay.put(day, temps);
        }
    }

    public void writeDailyStats(OutputStream outputStream, char scale) {
        PrintWriter pw = new PrintWriter(outputStream);
        if (scale == 'C') {
            tempByDay.forEach((day, list) -> {
                pw.printf("%3d: ", day);
                DoubleSummaryStatistics dss = list.stream()
                        .mapToDouble(t -> t.getTempByType(scale))
                        .summaryStatistics();
                pw.printf("Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC\n", dss.getCount(), dss.getMin(), dss.getMax(), dss.getAverage());
            });
        } else {
            tempByDay.forEach((day, list) -> {
                pw.printf("%3d: ", day);
                DoubleSummaryStatistics dss = list.stream()
                        .mapToDouble(t -> t.getTempByType(scale))
                        .summaryStatistics();
                pw.printf("Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF\n", dss.getCount(), dss.getMin(), dss.getMax(), dss.getAverage());
            });
        }


        pw.flush();
    }
}

public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}


