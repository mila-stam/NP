package np.vtor_kolok._11_TaskMenager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DeadlineNotValidException extends Exception {
    DeadlineNotValidException(LocalDateTime date) {
        super("The deadline " + date + " has already passed");
    }
}

class Task {
    String name;
    String desc;
    boolean hasDeadline;
    LocalDateTime deadLine;
    boolean isPriorityTask;
    int priority;

    public Task(String name, String desc) {
        this.name = name;
        this.desc = desc;
        this.hasDeadline = false;
        this.deadLine = LocalDateTime.now();
        this.isPriorityTask = false;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isPriorityTask() {
        return isPriorityTask;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) throws DeadlineNotValidException {
        LocalDateTime valid = LocalDateTime.parse("2020-06-02T00:00:00");
        if (deadLine.isBefore(valid)) {
            throw new DeadlineNotValidException(deadLine);
        }
        this.deadLine = deadLine;
        this.hasDeadline = true;
    }

    public void setPriority(int pr) {
        this.priority = pr;
        this.isPriorityTask = true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task{name='").append(name).append("', description='")
                .append(desc).append("'");
        if (hasDeadline) sb.append(", deadline=").append(deadLine);
        if (isPriorityTask) sb.append(", priority=").append(priority);
        sb.append("}");
        return sb.toString();
    }
}

class TaskBuilder {
    private boolean isValid(String date) {
        try {
            LocalDateTime.parse(date);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Task createTask(String line) throws DeadlineNotValidException {
        String[] parts = line.split(",");
        String name = parts[1];
        String desc = parts[2];
        Task task = new Task(name, desc);
        if (parts.length > 3) {
            if (isValid(parts[3])) {
                task.setDeadLine(LocalDateTime.parse(parts[3]));
                if (parts.length == 5) {
                    task.setPriority(Integer.parseInt(parts[4]));
                }
            } else {
                task.setPriority(Integer.parseInt(parts[3]));
            }
        }
        return task;
    }
}

class TaskManager {
    Map<String, List<Task>> tasksByCategory;

    TaskManager() {
        tasksByCategory = new HashMap<>();
    }

    public void readTasks(InputStream in) {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String category = line.split(",")[0];
            TaskBuilder tb = new TaskBuilder();
            try{
                Task t = tb.createTask(line);
                tasksByCategory.putIfAbsent(category, new ArrayList<>());
                tasksByCategory.get(category).add(t);
            } catch(Exception e){
                System.out.println(e.getMessage());
                continue;
            }
        }
    }

    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw = new PrintWriter(os);
        Comparator<Task> priorityComp = Comparator
                .comparing(Task::isPriorityTask).reversed()
                .thenComparing(Task::getPriority)
                .thenComparing(task -> Duration.between(LocalDateTime.now(), task.getDeadLine()));
        Comparator<Task> defaultComp = Comparator
                .comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadLine()));
        if (includeCategory) {
            tasksByCategory.forEach((category, task) -> {
                pw.println(category.toUpperCase());
                task.stream().sorted(includePriority ? priorityComp : defaultComp).forEach(pw::println);
            });
        } else {
            tasksByCategory.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(includePriority ? priorityComp : defaultComp)
                    .forEach(pw::println);
        }
        pw.flush();
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}

