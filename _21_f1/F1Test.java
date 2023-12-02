package np.zadachi_za_vezhbanje.prv_kolokvium._21_f1;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Time implements Comparable<Time> {
    private int min;
    private int sec;
    private int mili;

    public Time(String time) {
        String []parts=time.split(":");
        this.min = Integer.parseInt(parts[0]);
        this.sec = Integer.parseInt(parts[1]);
        this.mili = Integer.parseInt(parts[2]);
    }
    public Integer total(){
        return (min*60000) + (sec*1000) + mili;
    }
    @Override
    public int compareTo(Time other) {
        return total().compareTo(other.total());
    }

    @Override
    public String toString() {
        return String.format("  %d:%02d:%03d", min, sec, mili);
    }
}

class Racer implements Comparable<Racer> {
    private String name;
    private Time time1;
    private Time time2;
    private Time time3;

    public Racer(String line){
        String []parts = line.split("\\s+");
        this.name = parts[0];
        this.time1 = new Time(parts[1]);
        this.time2 = new Time(parts[2]);
        this.time3 = new Time(parts[3]);
    }
    public Time bestTime(){
        if (time1.compareTo(time2) < 0 && time1.compareTo(time3) < 0){
            return time1;
        }
        if (time2.compareTo(time1) < 0 && time2.compareTo(time3) < 0){
            return time2;
        }
        if (time3.compareTo(time2) < 0 && time3.compareTo(time1) < 0){
            return time3;
        }
        return null;
    }

    @Override
    public int compareTo(Racer other) {
        return bestTime().compareTo(other.bestTime());
    }

    @Override
    public String toString() {
        return String.format("%-10s", name) + bestTime();
    }
}
class F1Race {
    // vashiot kod ovde
    private List<Racer> racers;

    public F1Race() {
        racers = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        racers = br.lines()
                .map(Racer::new)
                .collect(Collectors.toList());
    }
    public void printSorted(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        racers = racers.stream().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        int num =1;
        for (Racer r:racers){
            pw.println(String.format("%d. %s", num, r.toString()));
            num++;
        }
        pw.flush();
    }
}

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}


