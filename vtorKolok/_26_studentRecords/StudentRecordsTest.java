//package np.vtor_kolok._26_studentRecords;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class Student {
    String code;
    String major;
    List<Integer> grades;

    public Student(String code, String major, List<Integer> grades) {
        this.code = code;
        this.major = major;
        this.grades = grades;
    }

    public double getAvg() {
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }

    public String getCode() {
        return code;
    }

}

class StudentRecords {
    Map<String, Set<Student>> studentsByMajor;
    Map<String, List<Integer>> gradesByMajor;

    StudentRecords() {
        studentsByMajor = new TreeMap<>();
        gradesByMajor = new TreeMap<>();
    }

    public int readRecords(InputStream inputStream) {
        int count = 0;
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String code = parts[0];
            String major = parts[1];
            List<Integer> grades = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }
            Student student = new Student(code, major, grades);
            studentsByMajor.putIfAbsent(major, new HashSet<>());
            studentsByMajor.get(major).add(student);

            gradesByMajor.putIfAbsent(major, new ArrayList<>());
            gradesByMajor.get(major).addAll(grades);
            count++;
        }
        return count;
    }

    public void writeTable(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        Comparator<Student> comp = Comparator.comparing(Student::getAvg).reversed().thenComparing(Student::getCode);
        studentsByMajor.forEach((major, list) -> {
            pw.println(major);
            list.stream().sorted(comp).forEach(s -> pw.println(String.format("%s %.2f", s.getCode(), s.getAvg())));
        });
        pw.flush();
    }

    public String numStars(int num) {
        if (num % 10 == 0) {
            return "*".repeat(num / 10);
        } else {
            return "*".repeat((num / 10) + 1);
        }
    }

    private String getRatio(List<Integer> grades) {
        int count6 = 0;
        int count7 = 0;
        int count8 = 0;
        int count9 = 0;
        int count10 = 0;
        for (Integer i : grades) {
            switch (i) {
                case 6:
                    count6++;
                    break;
                case 7:
                    count7++;
                    break;
                case 8:
                    count8++;
                    break;
                case 9:
                    count9++;
                    break;
                case 10:
                    count10++;
                    break;
            }
        }
        Integer[] gr = {count6, count7, count8, count9, count10};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(String.format("%2d | ", i + 6))
                    .append(numStars(gr[i]))
                    .append(String.format("(%d)", gr[i]))
                    .append("\n");
        }
        sb.append(String.format("%2d | ", 10))
                .append(numStars(gr[4]))
                .append(String.format("(%d)", gr[4]));
        return sb.toString();
    }

    private int getNum10(List<Integer> grades) {
        int count = 0;
        for (Integer i : grades) {
            if (i == 10) {
                count++;
            }
        }
        return count;
    }

    public void writeDistribution(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        gradesByMajor.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, List<Integer>> entry) -> getNum10(entry.getValue())).reversed())
                .forEach((Map.Entry<String, List<Integer>> entry) -> {
                    pw.println(entry.getKey());
                    if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                        pw.println(getRatio(entry.getValue()));
                    } else {
                        pw.println("No grades available.");
                    }
                });
        pw.flush();
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here
