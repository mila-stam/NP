package np.vtor_kolok._15_airports_daSePrereshiSoMap;

import java.util.*;
import java.util.stream.Collectors;

class Flight {
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
    public int getTime(){
        return time;
    }

    public int getDurationMin() {
        return duration;
    }

    public String getStamp() {
        int timeArrival = time + duration;
        int hD = time / 60;
        int minD = time % 60;
        int hA = timeArrival / 60;
        int minA = timeArrival % 60;
        if (hA>23){
            hA=hA-24;
            return String.format("%02d:%02d-%02d:%02d +1d", hD, minD, hA, minA);
        }
        return String.format("%02d:%02d-%02d:%02d", hD, minD, hA, minA);
    }

    public String getDuration() {
        int hours = duration / 60;
        int min = duration % 60;
        return String.format("%dh%02dm", hours, min);
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s %s", from, to, getStamp(), getDuration());
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengers;
    List<Flight> flightsTo;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flightsTo = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    public List<Flight> getFlightsTo() {
        return flightsTo;
    }

    public List<Flight> getFilteredFlights(String to) {
        return flightsTo.stream()
                .filter(s -> s.getTo().equals(to))
                .collect(Collectors.toList());
    }

    public boolean matchCode(String code) {
        return this.code.equals(code);
    }

    public void addFlightTo(Flight flight) {
        flightsTo.add(flight);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s (%s)\n%s\n%d\n", name, code, country, passengers));
        Comparator<Flight> comp = Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime);
        int i = 1;
        flightsTo.sort(comp);
        for (int j=0; j<flightsTo.size()-1; j++) {
            sb.append(i).append(". ").append(flightsTo.get(j)).append("\n");
            i++;
        }
        sb.append(flightsTo.size()).append(". ").append(flightsTo.get(flightsTo.size()));
        return sb.toString();
    }
}

class Airports {
    List<Airport> airports;

    public Airports() {
        airports = new ArrayList<>();
    }

    public void addAirport(String name, String country, String code, int passengers) {
        airports.add(new Airport(name, country, code, passengers));
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight flight = new Flight(from, to, time, duration);
        airports.stream()
                .filter(airport -> airport.matchCode(from))
                .findFirst()
                .ifPresent(airport -> airport.addFlightTo(flight));
    }

    public void showFlightsFromAirport(String code) {
        Airport airport = airports.stream()
                .filter(a -> a.matchCode(code))
                .findFirst()
                .orElse(null);
        System.out.println(airport);
        /*airport.getFlightsTo().stream()
                .sorted(Comparator.comparing(Flight::getTo).thenComparing(Flight::getDurationMin))
                .forEach(System.out::println);*/
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport airport = airports.stream()
                .filter(a -> a.matchCode(from))
                .findFirst()
                .orElse(null);
        List<Flight> flights = airport.getFlightsTo().stream()
                .filter(flight -> flight.getTo().equals(to))
                .collect(Collectors.toList());
        if (flights.isEmpty()) {
            System.out.printf("No flights from %s to %s\n", from, to);
        } else {
            flights.forEach(System.out::println);
        }
    }

    public void showDirectFlightsTo(String to) {
        Comparator<Flight> comp = Comparator.comparing(Flight::getTime);
        List<Flight> allFlights = airports.stream()
                .flatMap(a -> a.getFilteredFlights(to).stream())
                .sorted(comp)
                .collect(Collectors.toList());
        if (!allFlights.isEmpty()){
            allFlights.forEach(System.out::println);
        }
    }
}

public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


