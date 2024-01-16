
package np.vtor_kolok._23_labVezhbi;

import java.util.*;
import java.util.stream.Collectors;
class Student{
    String index;
    List<Integer> labPoints;

    public Student(String index, List<Integer> labPoints) {
        this.index = index;
        this.labPoints = labPoints;
    }
    public double getSumPoints(){
        return labPoints.stream().mapToInt(x->x).sum()/10.0;
    }

    public String getIndex() {
        return index;
    }
    public int getFinishedLabs(){
        return labPoints.size();
    }
    public boolean hasSig(){
        return 10 - getFinishedLabs() <= 2;
    }
    public int getYear(){
        return 20 - Integer.parseInt(index.substring(0,2));
    }
    @Override
    public String toString() {
        if (hasSig()){
            return String.format("%s YES %.2f", index, getSumPoints());
        } else {
            return String.format("%s NO %.2f", index, getSumPoints());
        }
    }
}
class LabExercises{
    List<Student> students;
    LabExercises(){
        students = new ArrayList<>();
    }
    public void addStudent(Student student){
        students.add(student);
    }
    public void printByAveragePoints(boolean ascending, int n){
        if (ascending){
            students.stream()
                    .sorted(Comparator.comparing(Student::getSumPoints).thenComparing(Student::getIndex))
                    .limit(n)
                    .forEach(System.out::println);
        }else {
            students.stream()
                    .sorted(Comparator.comparing(Student::getSumPoints).thenComparing(Student::getIndex).reversed())
                    .limit(n)
                    .forEach(System.out::println);
        }
    }
    public List<Student> failedStudents (){
        return students.stream()
                .filter(student -> !student.hasSig())
                .sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::getSumPoints))
                .collect(Collectors.toList());
    }
    public double getAvgSum(List<Student> list){
        return list.stream()
                .mapToDouble(Student::getSumPoints)
                .average()
                .orElse(0);
    }
    public Map<Integer,Double> getStatisticsByYear(){
        return students.stream()
                .filter(Student::hasSig)
                .collect(Collectors.groupingBy(
                        Student::getYear,
                        Collectors.averagingDouble(Student::getSumPoints)
                ));
    }
}
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
