package np.vtor_kolok._28_faculty;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OperationNotAllowedException extends Exception {
    OperationNotAllowedException(String msg) {
        super(msg);
    }
}

class Student {
    String id;
    int yearsOfStudies;
    Map<Integer, List<Integer>> gradesByTerm;
    List<String> courses;

    public Student(String id, int yearsOfStudies) {
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        gradesByTerm = new HashMap<>();
        courses = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public int getYearsOfStudies() {
        return yearsOfStudies;
    }

    public Integer getCourses() {
        return courses.size();
    }

    public void setMap(int term, int grade) throws OperationNotAllowedException {
        gradesByTerm.putIfAbsent(term, new ArrayList<>());
        if (gradesByTerm.get(term).size() == 3) {
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d", id, term));
        }
        if (yearsOfStudies == 3) {
            if (term > 6) {
                throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
            }
        } else {
            if (term > 8) {
                throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s", term, id));
            }
        }
        //gradesByTerm.putIfAbsent(term, new ArrayList<>());
        gradesByTerm.get(term).add(grade);
    }

    public int getFinishedSubjects() {
        return gradesByTerm.values().stream()
                .mapToInt(List::size)
                .sum();
    }

    public void setCourses(String course) {
        courses.add(course);
    }

    public double getAvgGrade() {
        return gradesByTerm.values().stream()
                .flatMap(Collection::stream)
                .mapToInt(Integer::intValue)
                .sum() / (getFinishedSubjects() * 1.0);
    }

    public String getDetView() {
        StringBuilder sb = new StringBuilder();
        sb.append("Student: ").append(id).append("\n");
        for (int i = 1; i <= gradesByTerm.size(); i++) {
            sb.append("Term ").append(i).append(":\n");
            sb.append("Courses for term: ").append(gradesByTerm.get(i).size()).append("\n");
            sb.append(String.format("Average grade for term: %.2f\n", gradesByTerm.get(i).stream().mapToInt(Integer::intValue).average().orElse(0)));
        }
        sb.append(String.format("Average grade: %.2f\n", getAvgGrade()));
        sb.append("Courses attended: ").append(courses.stream().sorted().collect(Collectors.toList())); //ili da bide set
        return sb.toString();
    }

    @Override
    public String toString() {
        return String.format("Student: %s Courses passed: %d Average grade: %.2f", id, courses.size(), getAvgGrade());
    }
}
class Course{
    String name;
    List<Integer> grades;

    public Course(String name) {
        this.name = name;
        grades = new ArrayList<>();
    }
    public void addGrade(int grade){
        grades.add(grade);
    }

    public String getName() {
        return name;
    }

    public int countStudents(){
        return grades.size();
    }
    public double getAvg(){
        return grades.stream()
                .mapToDouble(Integer::intValue)
                .sum()/countStudents();
    }
    @Override
    public String toString() {
        return String.format("%s %d %.2f", name, countStudents(), getAvg());
    }
}
class Faculty {
    List<Student> allStudents;
    List<Course> listCourses;
    StringBuilder log;

    public Faculty() {
        allStudents = new ArrayList<>();
        listCourses = new ArrayList<>();
        log = new StringBuilder();
    }

    void addStudent(String id, int yearsOfStudies) {
        Student student = new Student(id, yearsOfStudies);
        allStudents.add(student);
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        for (Student s : allStudents) {
            if (s.getId().equals(studentId)) {
                s.setCourses(courseName);
                s.setMap(term, grade);
                for (Course c:listCourses){
                    if (!c.getName().equals(courseName)){
                        listCourses.add(new Course(courseName));
                        break;
                    }
                }
                for (Course c:listCourses){
                    if (c.getName().equals(courseName)){
                        c.addGrade(grade);
                        break;
                    }
                }
                //gradesByCourse.putIfAbsent(courseName, new ArrayList<>());
                //gradesByCourse.get(courseName).add(grade);
                if (s.getYearsOfStudies() == 3) {
                    if (s.getFinishedSubjects() == 18) {
                        log.append(String.format("Student with ID %s graduated with average grade %.2f in 3 years.\n", s.getId(), s.getAvgGrade()));
                    }
                } else {
                    if (s.getFinishedSubjects() == 24) {
                        log.append(String.format("Student with ID %s graduated with average grade %.2f in 4 years.\n", s.getId(), s.getAvgGrade()));
                    }
                }
            }
        }
    }

    String getFacultyLogs() {
        return log.toString();
    }

    String getDetailedReportForStudent(String id) {
        Optional<Student> st = allStudents.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
        if (st.isPresent()){
            return st.map(Student::getDetView).orElse("Student not found");
        }
        return ("Student not found");
    }

    void printFirstNStudents(int n) {
        allStudents.stream()
                .sorted(Comparator.comparing(Student::getCourses).thenComparing(Student::getAvgGrade))
                .limit(n)
                .forEach(System.out::println);
    }
    void printCourses() {
        listCourses.stream()
                .sorted(Comparator.comparing(Course::countStudents).thenComparing(Course::getAvg))
                .forEach(System.out::println);
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase == 10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase == 11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i = 11; i < 15; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}

