package np.zadachi_za_vezhbanje.prv_kolokvium._19_subtitles;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Time {
    private int hours;
    private int minutes;
    private int seconds;
    private int milisec;

    public Time(String line) {
        String[] parts = line.split("[:,]");
        this.hours = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
        this.seconds = Integer.parseInt(parts[2]);
        this.milisec = Integer.parseInt(parts[3]);
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public void setMilisec(int milisec) {
        this.milisec = milisec;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilisec() {
        return milisec;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d,%03d", hours, minutes, seconds, milisec);
    }
}

class Element {
    private int number;
    private Time startTime;
    private Time endTime;
    private String text;

    public Element(int number, String time, String text) {
        this.number = number;
        String[] parts = time.split("\\s+");
        this.startTime = new Time(parts[0]);
        this.endTime = new Time(parts[2]);
        this.text = text;
    }
    public void shiftMili(int ms){
        int newMiliStart = startTime.getMilisec() + (startTime.getSeconds()*1000) + (startTime.getMinutes()*60000) + ms;
        startTime.setMinutes(newMiliStart/60000);
        startTime.setSeconds((newMiliStart%60000)/1000);
        startTime.setMilisec(newMiliStart%1000);
        int newMiliEnd = endTime.getMilisec() + (endTime.getSeconds()*1000) +(endTime.getMinutes()*60000) + ms;
        endTime.setMinutes(newMiliEnd/60000);
        endTime.setSeconds((newMiliEnd%60000)/1000);
        endTime.setMilisec(newMiliEnd%1000);
    }

    @Override
    public String toString() {
        return number + "\n" +
                startTime.toString() + " --> " + endTime.toString() + "\n" +
                text + "\n";
    }
}

class Subtitles {
    private List<Element> elements;

    public Subtitles() {
        elements = new ArrayList<>();
    }

    public int loadSubtitles(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            int number = Integer.parseInt(line);
            String time = scanner.nextLine();
            StringBuilder text = new StringBuilder();
            while (true) {
                if (!scanner.hasNextLine()) break;
                line = scanner.nextLine();
                if (line.trim().isEmpty())
                    break;
                text.append(line);
                text.append("\n");
            }
            Element element = new Element(number, time, text.toString());
            elements.add(element);
        }
        return elements.size();
    }

    public void print() {
        for (Element e : elements) {
            System.out.print(e);
        }
    }
    public void shift(int ms){
            elements.forEach(e->e.shiftMili(ms));
    }
}

public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде
