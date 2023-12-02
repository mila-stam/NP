package np.zadachi_za_vezhbanje.prv_kolokvium._7_timeTable;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String time) {
        super(time);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String time) {
        super(time);
    }
}

class Time implements Comparable<Time> {
    int hours;
    int minutes;

    public Time(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public Time(String line) throws UnsupportedFormatException, InvalidTimeException {
        if (line.contains(":") || line.contains(".")) {
            String[] parts = line.split("[:.]");
            if ((Integer.parseInt(parts[0]) <= 23 || Integer.parseInt(parts[0]) >= 0) || (Integer.parseInt(parts[1]) <= 59 || Integer.parseInt(parts[1]) >= 0)) {
                this.hours = Integer.parseInt(parts[0]);
                this.minutes = Integer.parseInt(parts[1]);
            } else throw new InvalidTimeException(line);
        } else throw new UnsupportedFormatException(line);
    }

    public Time transform(){
        int transformedHours = hours;
        if (transformedHours == 0) {
            transformedHours = 12;
        }
        if (transformedHours >= 13 && transformedHours <= 23) {
            transformedHours -= 12;
        }
        return new Time(transformedHours, minutes);
    }

    @Override
    public String toString() {

        return String.format("%2d:%02d", hours, minutes);
    }

    @Override
    public int compareTo(Time other) {
        int hourComp = Integer.compare(this.hours, other.hours);
        if (hourComp == 0) {
            return Integer.compare(this.minutes, other.minutes);
        }
        return hourComp;
    }
}

class TimeTable {
    List<Time> times;

    TimeTable() {
        times = new ArrayList<>();
    }

    void readTimes(InputStream inputStream) throws InvalidTimeException, UnsupportedFormatException {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNext()) {
            String s = sc.next();
            try {
                Time t = new Time(s);
                times.add(t);
            } catch (UnsupportedFormatException e) {
                System.out.println("UnsupportedFormatException: " + e.getMessage());
            } catch (InvalidTimeException e) {
                System.out.println("InvalidTimeException: " + e.getMessage());
            }

        }
    }


    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);
        List<Time> formated = times;
        formated = times.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        if(format.equals(TimeFormat.FORMAT_AMPM)){
            String [] stamp = new String[formated.size()];
            for (int i=0; i< formated.size(); i++){
                if (formated.get(i).getHours() < 12){
                    stamp[i] = "AM";
                } else{
                    stamp[i] = "PM";
                }
            }
            formated = formated.stream()
                    .map(Time::transform)
                    .collect(Collectors.toList());
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<formated.size(); i++){
                sb.append(formated.get(i)).append(" ").append(stamp[i]).append("\n");
            }
            pw.println(sb);
        } else{
            for (Time t:formated){
                pw.println(t);
            }
        }

        pw.flush();
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}


