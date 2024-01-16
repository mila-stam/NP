package np.vtor_kolok._11_TaskMenager.withDecoratorPattern;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
class DeadlineNotValidException extends Exception{
    DeadlineNotValidException(String msg){
        super(msg);
    }
}

interface ITask{
    LocalDateTime getDeadline();
    int getPriority();
    Duration getDiff();
}
class Task implements ITask{
    String name;
    String description;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Duration getDiff() {
        return Duration.between(LocalDateTime.now(), getDeadline());
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'';
    }
}
abstract class TaskDecorator implements ITask{
    ITask wrappedTask;
    public TaskDecorator(ITask wrappedTask){
        this.wrappedTask=wrappedTask;
    }
}
class PriorityDecorator extends TaskDecorator{
    int priority;
    public PriorityDecorator(ITask wrappedTask, int priority) {
        super(wrappedTask);
        this.priority=priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return wrappedTask.getDeadline();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Duration getDiff() {
        return Duration.between(LocalDateTime.now(),getDeadline());
    }

    @Override
    public String toString() {
        return wrappedTask +
                ", priority=" + priority;
    }
}
class DeadlineDecorator extends TaskDecorator{
    LocalDateTime deadline;

    public DeadlineDecorator(ITask wrappedTask, LocalDateTime deadline) throws DeadlineNotValidException {
        super(wrappedTask);
        if (deadline.isBefore(LocalDateTime.of(2020,6,2,23,59,59))){
            throw new DeadlineNotValidException("The deadline 2020-06-01T23:59:59 has already passed");
        }
        this.deadline=deadline;
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }

    @Override
    public int getPriority() {
        return wrappedTask.getPriority();
    }

    @Override
    public Duration getDiff() {
        return Duration.between(LocalDateTime.now(),deadline);
    }
    @Override
    public String toString() {
        return wrappedTask +
                ", deadline=" + deadline;
    }
}
class TaskManager{
    Map<String, List<ITask>> tasksByCategory;

    public TaskManager() {
        tasksByCategory = new HashMap<>();
    }
    public void readTasks(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> lines = br.lines().collect(Collectors.toList());
        for (String line:lines){
            try{
                String [] parts = line.split(",");
                ITask iTask = new Task(parts[1], parts[2]);
                if (parts.length == 4 || parts.length == 5){
                    if (parts[3].length() > 10){
                        iTask = new DeadlineDecorator(iTask, LocalDateTime.parse(parts[3]));
                    }else{
                        iTask = new PriorityDecorator(iTask, Integer.parseInt(parts[3]));
                    }
                }
                if (parts.length == 5){
                    iTask = new PriorityDecorator(iTask, Integer.parseInt(parts[4]));
                }
                tasksByCategory.putIfAbsent(parts[0], new ArrayList<>());
                tasksByCategory.get(parts[0]).add(iTask);
            }catch (DeadlineNotValidException e){
                System.out.println(e.getMessage());
            }
        }

    }
    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter pw = new PrintWriter(os);
        Comparator<ITask> compWithPr = Comparator.comparing(ITask::getPriority).thenComparing(ITask::getDiff);
        Comparator<ITask> defComp = Comparator.comparing(ITask::getDiff);
        if (includeCategory){
            tasksByCategory.forEach((category, task) -> {
                pw.println(category.toUpperCase());
                task.stream().sorted(includePriority ? compWithPr : defComp)
                        .forEach(t -> pw.println(t+"}"));
            });
        } else{
            tasksByCategory.values().stream()
                    .flatMap(Collection::stream)
                    .sorted(includePriority ? compWithPr : defComp)
                    .forEach(t -> pw.println(t+"}"));
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

