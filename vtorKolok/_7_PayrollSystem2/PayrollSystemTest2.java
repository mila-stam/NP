/*
package np.vtor_kolok._7_PayrollSystem2;

import java.util.*;
import java.util.stream.Collectors;
class BonusNotAllowedException extends Exception{
    BonusNotAllowedException (String msg){

    }
}
class Employee{
    String id;
    String level;
    int bonus;
    double rate;

    public Employee(String id, String level, int bonus, double rate) {
        this.id = id;
        this.level = level;
        this.bonus = bonus;
        this.rate=rate;
    }
}
class HourlyEmployee extends Employee{
    double hours;
    double regularHours, overtimeHours;
    public HourlyEmployee(String id, String level, int bonus, double rate, double hours) {
        super(id, level, bonus, rate);
        this.hours=hours;
        if (hours<=40){
            regularHours = hours;
            overtimeHours = 0;
        }else {
            regularHours = 40;
            overtimeHours = hours-40;
        }
    }
}
class FreelanceEmployee extends Employee{
    List<Integer> points;

    public FreelanceEmployee(String id, String level, int bonus, double rate, List<Integer> points) {
        super(id, level, bonus, rate);
        this.points = points;
    }
}
class PayrollSystem{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    Map<String, Set<Employee>> byLevel;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }
    public Employee createEmployee(String line) throws BonusNotAllowedException {
        String []d = line.split("\\s+");
        String b = d[1];
        String [] parts = d[0].split(";");
        String id = parts[1];
        String level = parts[2];
        if (parts[0].equals("H")) {
            double hours = Double.parseDouble(parts[3]);
            int bonus = Integer.parseInt(b);
            if (bonus > 1000){
                throw new BonusNotAllowedException(b);
            }
            HourlyEmployee h = new HourlyEmployee(id, level, bonus, hourlyRateByLevel.get(level), hours);
            byLevel.putIfAbsent(level, new TreeSet<>()); //TODO da se napishe komparator
            byLevel.get(level).add(h);
            return h;
        }else {
            List<Integer> points = Arrays.stream(parts)
                    .skip(3)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            int bonus = Integer.parseInt(b.substring(0, b.length()-1));
            if (bonus > 20){
                throw new BonusNotAllowedException(b);
            }
            FreelanceEmployee f = new FreelanceEmployee(id, level, bonus, ticketRateByLevel.get(level), points);
            byLevel.putIfAbsent(level, new TreeSet<>()); //TODO da se napishe komparator
            byLevel.get(level).add(f);
            return f;
        }
    }
    Map<String, Double> getOvertimeSalaryForLevels (){

    }
}
public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}
*/
