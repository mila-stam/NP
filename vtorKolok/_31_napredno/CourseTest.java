package np.vtor_kolok._31_napredno;


import java.util.*;
import java.util.stream.Collectors;

class MaxNumOfPointsException extends Exception {

}

class Student {
    String index;
    String name;
    int midterm1;
    int midterm2;
    int labs;

    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.midterm1 = 0;
        this.midterm2 = 0;
        this.labs = 0;
    }

    public void setPoints(String activity, int points) throws MaxNumOfPointsException {
        if (activity.equals("midterm1")) {
            if (midterm1 == 100) {
                throw new MaxNumOfPointsException();
            } else {
                midterm1 += points;
            }
        }
        if (activity.equals("midterm2")) {
            if (midterm2 == 100) {
                throw new MaxNumOfPointsException();
            } else {
                midterm2 += points;
            }
        }
        if(activity.equals("labs")){
            if (labs == 10) {
                throw new MaxNumOfPointsException();
            } else {
                labs += points;
            }
        }

    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getMidterm1() {
        return midterm1;
    }

    public int getMidterm2() {
        return midterm2;
    }

    public int getLabs() {
        return labs;
    }

    public int getGrade() {
        if (getSumPoints() <= 50) {
            return 5;
        } else if (getSumPoints() > 50 && getSumPoints() <= 60) {
            return 6;
        } else if (getSumPoints() > 60 && getSumPoints() <= 70) {
            return 7;
        } else if (getSumPoints() > 70 && getSumPoints() <= 80) {
            return 8;
        } else if (getSumPoints() > 80 && getSumPoints() <= 90) {
            return 9;
        } else {
            return 10;
        }
    }

    public double getSumPoints() {
        return (midterm1 * 0.45) + (midterm2 * 0.45) + labs;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d", index, name, midterm1, midterm2, labs, getSumPoints(), getGrade());
    }
}

class AdvancedProgrammingCourse {
    Map<String, Student> allStudents;

    AdvancedProgrammingCourse() {
        allStudents = new HashMap<>();
    }

    public void addStudent(Student s) {
        allStudents.put(s.getIndex(), s);
    }

    public void updateStudent(String idNumber, String activity, int points) {
        try {
            allStudents.get(idNumber).setPoints(activity, points);
        } catch (MaxNumOfPointsException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Student> getFirstNStudents(int n) {
        return allStudents.values().stream()
                .sorted(Comparator.comparing(Student::getSumPoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<Integer, Integer> getGradeDistribution() {
        Map<Integer, List<Student>> studentsByGrade = new TreeMap<>();
        studentsByGrade.put(5, new ArrayList<>());
        studentsByGrade.put(6, new ArrayList<>());
        studentsByGrade.put(7, new ArrayList<>());
        studentsByGrade.put(8, new ArrayList<>());
        studentsByGrade.put(9, new ArrayList<>());
        studentsByGrade.put(10, new ArrayList<>());
        allStudents.values()
                .forEach(student -> {
                    studentsByGrade.get(student.getGrade()).add(student);
                });
        return studentsByGrade.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (int)entry.getValue().stream().mapToInt(Student::getGrade).count()
                ));
        /*return allStudents.values().stream()
                .collect(Collectors.groupingBy(
                        Student::getGrade,
                        TreeMap::new,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));*/
    }

    public void printStatistics() {
        DoubleSummaryStatistics stats = allStudents.values().stream()
                .mapToDouble(Student::getSumPoints)
                .filter(points -> points > 50)
                .summaryStatistics();
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f%n", stats.getCount(), stats.getMin(), stats.getAverage(), stats.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
