package np.zadachi_za_vezhbanje.prv_kolokvium._14_weatherStation_neProagjaNaCourses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Measurement implements Comparable<Measurement> {
    private Float temp;
    private Float wind;
    private Float humidity;
    private Float visibility;
    private Date date;

    public Measurement(Float temp, Float wind, Float humidity, Float visibility, Date date) {
        this.temp = temp;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    public Float getTemp() {
        return temp;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public int compareTo(Measurement other) {
        return date.compareTo(other.getDate());
    }

    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km ", temp, wind, humidity, visibility) + date;
    }
}

class WeatherStation {
    private List<Measurement> measurements;
    int days;

    public WeatherStation(int days) {
        this.days = days;
        measurements = new ArrayList<>();
    }

    public void addMeasurement(float temperature, float wind, float humidity, float visibility, Date date) {
        Measurement ms = new Measurement(temperature, wind, humidity, visibility, date);
        if(!measurements.isEmpty()){
            /*for (Measurement  m:measurements){
                if(ms.getDate().getDay()-m.getDate().getDay() > 4 ){
                    measurements.remove(m);
                }
            }*/
            Iterator<Measurement> iterator = measurements.iterator();
            while (iterator.hasNext()) {
                Measurement m = iterator.next();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                int newDay = Integer.parseInt(sdf.format(ms.getDate()).substring(0, 2));
                int day = Integer.parseInt(sdf.format(m.getDate()).substring(0, 2));
                if ( (newDay - day)  > 4) {
                    iterator.remove();
                }
            }
            if(!measurements.isEmpty()){
                double newMin = ms.getDate().getMinutes() + (ms.getDate().getSeconds() / 60.0);
                double lastMin = measurements.getLast().getDate().getMinutes() + (measurements.getLast().getDate().getSeconds() / 60.0);
                if (Math.abs(newMin - lastMin) > 2.5) {
                    measurements.add(ms);
                }
            } else{
                measurements.add(ms);
            }
            //measurements.removeIf(m -> ms.getDate().getDay() - m.getDate().getDay() > 4);
        }else{
            measurements.add(ms);
        }

    }

    public int total() {
        return measurements.size();
    }

    public void status(Date from, Date to) {
        List<Measurement> filtered = measurements.stream()
                .filter(ms -> ms.getDate().compareTo(from) >= 0 && ms.getDate().compareTo(to) <= 0)
                .toList();
        if (filtered.isEmpty()) {
            throw new RuntimeException();
        }
        filtered.stream()
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);
        double avg = filtered.stream()
                .mapToDouble(Measurement::getTemp)
                .average()
                .orElse(0.0);
        System.out.printf("Average temperature: %.2f", avg);
    }
}

public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurement(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde
