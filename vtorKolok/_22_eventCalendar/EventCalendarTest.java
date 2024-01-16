package np.vtor_kolok._22_eventCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

class WrongDateException extends Exception{
    WrongDateException(LocalDateTime date){
        super(String.format("Wrong date: %s %s %d %d:%02d:00 UTC %d",
                date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                date.getDayOfMonth(),
                date.getHour(),
                date.getMinute(),
                date.getYear()));
    }
}
class Event implements Comparable<Event>{
    String name;
    String location;
    LocalDateTime date;

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public int compareTo(Event o) {
        int res = date.compareTo(o.getDate());
        if (res == 0){
            return name.compareTo(o.getName());
        }
        return res;
    }

    @Override
    public String toString() {
        return String.format("%d %s, %d %d:%02d at %s, %s",
                date.getDayOfMonth(),
                date.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                date.getYear(),
                date.getHour(),
                date.getMinute(),
                location,
                name);
    }
}
class EventCalendar{
    int year;
    Map<LocalDateTime, Set<Event>> eventsByDate;
    Map<Integer, List<Event>> eventsByMonth;

    public EventCalendar(int year) {
        this.year = year;
        eventsByDate = new HashMap<>();
        eventsByMonth = new TreeMap<>();
        for (int i=1; i<13; i++){
            eventsByMonth.put(i, new ArrayList<>());
        }

    }
    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        Event e = new Event(name, location, date);
        if (date.getYear() != year){
            throw new WrongDateException(date);
        }
        eventsByMonth.get(date.getMonthValue()).add(e);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime d = LocalDateTime.parse(String.format("%02d.%02d.%d 00:00", date.getDayOfMonth(), date.getMonthValue(), date.getYear()), dtf);
        eventsByDate.putIfAbsent(d, new HashSet<>());
        eventsByDate.get(d).add(e);
    }
    public void listEvents(LocalDateTime date){
        Set<Event> events = eventsByDate.get(date);
        if (events == null){
            System.out.println("No events on this day!");
        }else {
            events.stream().sorted().forEach(System.out::println);
        }
    }
    public void listByMonth(){
        eventsByMonth.forEach((month, events) -> {
            if (events.isEmpty()){
                System.out.println(month + " : 0");
            }else{
                System.out.println(month + " : " + events.size());
            }

        });
    }
}
public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2],dtf);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        String nl = scanner.nextLine();
        LocalDateTime date = LocalDateTime.parse(nl,dtf);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

// vashiot kod ovde
