package np.vtor_kolok._6_PayrollSystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

abstract class Employee{
    String ID;
    String level;
    public static final Comparator<Employee> EMPLOYEE_COMP = Comparator.comparing(Employee::paycheck)
            .reversed()
            .thenComparing(Employee::getLevel);

    public Employee(String ID, String level) {
        this.ID = ID;
        this.level = level;
    }

    public String getID() {
        return ID;
    }

    public String getLevel() {
        return level;
    }

    abstract double paycheck();

    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f ", ID, level, paycheck());
    }
}

class HourlyEmployee extends Employee {
    double hours;
    double hourlyRateByLevel;
    double regularHours, overtimeHours;

    public HourlyEmployee(String ID, String level, double hours, double hourlyRateByLevel) {
        super(ID, level);
        this.hours = hours;
        this.hourlyRateByLevel = hourlyRateByLevel;
        if (hours<=40){
            regularHours = hours;
            overtimeHours = 0;
        }else {
            regularHours = 40;
            overtimeHours = hours-40;
        }
    }

    @Override
    double paycheck() {
        return (regularHours * hourlyRateByLevel) + (overtimeHours * hourlyRateByLevel * 1.500);
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Regular hours: %.2f Overtime hours: %.2f", regularHours, overtimeHours);
    }
}

class FreelanceEmployee extends Employee {
    List<Integer> points;
    double ticketRateByLevel;

    public FreelanceEmployee(String ID, String level, List<Integer> points, double ticketRateByLevel) {
        super(ID, level);
        this.points = points;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    @Override
    double paycheck() {
        return points.stream().mapToInt(x -> x).sum() * ticketRateByLevel;
    }

    @Override
    public String toString() {
        return super.toString() + String.format("Tickets count: %d Tickets points: %d", points.size(), points.stream().mapToInt(x ->x).sum());
    }
}

class PayrollSystem {
    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;
    Map<String, Set<Employee>> sortedByLevel;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        sortedByLevel = new TreeMap<>();
    }

    public void readEmployees(InputStream is) {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String id = parts[1];
            String level = parts[2];
            if (parts[0].equalsIgnoreCase("H")) {
                double h = Double.parseDouble(parts[3]);
                HourlyEmployee hourlyEmployee = new HourlyEmployee(id, level, h, hourlyRateByLevel.get(level));
                sortedByLevel.putIfAbsent(level, new TreeSet<>(Employee.EMPLOYEE_COMP));
                sortedByLevel.get(level).add(hourlyEmployee);
            } else {
                List<Integer> list = Arrays.stream(parts)
                        .skip(3)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                FreelanceEmployee freelanceEmployee = new FreelanceEmployee(id, level, list, ticketRateByLevel.get(level));
                sortedByLevel.putIfAbsent(level, new TreeSet<>(Employee.EMPLOYEE_COMP));
                sortedByLevel.get(level).add(freelanceEmployee);
            }
        }
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        return sortedByLevel.entrySet().stream()
                .filter(entry -> levels.contains(entry.getKey()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));
    }
}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });


    }
}
