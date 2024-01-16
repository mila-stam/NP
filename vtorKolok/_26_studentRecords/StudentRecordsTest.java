package np.vtor_kolok._26_studentRecords;

import java.io.*;
import java.util.*;
class Student{
    String code;
    String major;
    List<Integer> grades;

    public Student(String code, String major, List<Integer> grades) {
        this.code = code;
        this.major = major;
        this.grades = grades;
    }
    public double getAvg(){
        return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
    }
}
class StudentRecords{
    Map<String, Set<Student>> studentsByMajor;
    StudentRecords(){
        studentsByMajor = new TreeMap<>();
    }
    public int readRecords(InputStream inputStream){
        int count=0;
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");
            String code = parts[0];
            String major = parts[1];
            List<Integer> grades = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                grades.add(Integer.parseInt(parts[i]));
            }
            Student student = new Student(code, major, grades);
            studentsByMajor.putIfAbsent(major, new HashSet<>());
            studentsByMajor.get(major).add(student);
            count++;
        }
        return count;
    }
    public void writeTable(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        
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
