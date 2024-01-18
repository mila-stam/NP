package np.vtor_kolok._33_logs;

import javax.management.remote.rmi.RMIConnectorServer;
import java.util.*;
import java.util.stream.Collectors;

class Log {
    String type;
    String msg;
    int timestamp;
    String nameMicro;

    public Log(String type, String msg, int timestamp, String nameMicro) {
        this.type = type;
        this.msg = msg;
        this.timestamp = timestamp;
        this.nameMicro = nameMicro;
    }

    public int getSeverity() {
        int total = 0;
        if (type.equals("INFO")) {
            return total;
        }
        if (type.equals("WARN")) {
            total += 1;
            if (msg.contains("might cause error")) {
                total += 1;
            }
        }
        if (type.equals("ERROR")) {
            total += 3;
            if (msg.contains("fatal")) {
                total += 2;
            }
            if (msg.contains("exception")) {
                total += 3;
            }
        }
        return total;
    }

    public String getType() {
        return type;
    }

    public String getMicro() {
        return nameMicro;
    }

    public int getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(" [%s] %s%d T:%d", type, msg, timestamp, timestamp);
    }
}

class Microservice {
    String name;
    List<Log> logs;
    Map<Integer, List<Log>> logsBySeverity;

    public Microservice(String name) {
        this.name = name;
        logs = new ArrayList<>();
        logsBySeverity = new TreeMap<>();
    }

    public void addLog(Log l) {
        logs.add(l);
        logsBySeverity.putIfAbsent(l.getSeverity(), new ArrayList<>());
        logsBySeverity.get(l.getSeverity()).add(l);
    }

    public int getSumSeverity() {
        return logs.stream()
                .mapToInt(Log::getSeverity)
                .sum();
    }

    public int getNumOfLogs() {
        return logs.size();
    }

    public String getName() {
        return name;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public Map<Integer, List<Log>> getLogsBySeverity() {
        return logsBySeverity;
    }
}
class ComparatorBuilder{
    public static Comparator<Log> makeComp(String order){
        switch (order){
            case "NEWEST_FIRST":
                return Comparator.comparing(Log::getTimestamp).reversed();
            case "OLDEST_FIRST":
                return Comparator.comparing(Log::getTimestamp);
            case "MOST_SEVERE_FIRST":
                return Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp).reversed();
            case "LEAST_SEVERE_FIRST":
                return Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp);
            default: return null;
        }
    }
}
class Service{
    String name;
    Map<Integer, List<Log>> allLogsBySeverity;
    Service(String name){
        this.name=name;
        allLogsBySeverity = new TreeMap<>();
    }
    public void addLog(Log l){
        allLogsBySeverity.putIfAbsent(l.getSeverity(), new ArrayList<>());
        allLogsBySeverity.get(l.getSeverity()).add(l);
    }
    public String getName(){
        return name;
    }

    public Map<Integer, List<Log>> getAllLogsBySeverity() {
        return allLogsBySeverity;
    }
}
class LogCollector {
    Map<String, List<Microservice>> services;
    Map<String, Service> servicesWithLogs;

    LogCollector() {
        services = new TreeMap<>();
        servicesWithLogs = new TreeMap<>();
    }

    public void addLog(String log) {
        String[] parts = log.split("\\s+");
        String serviceName = parts[0];
        String microserviceName = parts[1];
        String type = parts[2];
        StringBuilder msg = new StringBuilder();
        for (int i = 3; i < parts.length - 1; i++) {
            msg.append(parts[i]).append(" ");
        }
        int timestamp = Integer.parseInt(parts[parts.length - 1]);

        Log l = new Log(type, msg.toString(), timestamp, microserviceName);
        Microservice micro = new Microservice(microserviceName);

        services.putIfAbsent(serviceName, new ArrayList<>());
        boolean flag = true;
        for (Microservice m : services.get(serviceName)) {
            if (m.getName().equals(microserviceName)) {
                m.addLog(l);
                flag = false;
            }
        }
        if (flag) {
            micro.addLog(l);
            services.get(serviceName).add(micro);

        }
        Service newServ = new Service(serviceName);
        servicesWithLogs.put(serviceName, newServ);
        servicesWithLogs.get(serviceName).addLog(l);
    }

    private int totalLogs(List<Microservice> list) {
        return list.stream()
                .mapToInt(Microservice::getNumOfLogs)
                .sum();
    }

    private double avgSeverity(String service) {
        return services.get(service).stream()
                .mapToDouble(Microservice::getSumSeverity)
                .sum() / totalLogs(services.get(service));
    }

    public void printServicesBySeverity() {
        services.entrySet().stream()
                .sorted(Comparator.comparing((Map.Entry<String, List<Microservice>> entry) -> avgSeverity(entry.getKey())).reversed())
                .forEach((Map.Entry<String, List<Microservice>> entry) -> {
                    System.out.print("Service name: " + entry.getKey());
                    System.out.print(" Count of microservices: " + entry.getValue().size());
                    System.out.print(" Total logs in service: " + totalLogs(entry.getValue()));
                    System.out.printf(" Average severity for all logs: %.2f",avgSeverity(entry.getKey()));
                    System.out.printf(" Average number of logs per microservice: %.2f\n", (double)totalLogs(entry.getValue())/entry.getValue().size());
                });
    }

    public Map<Integer, Integer> getSeverityDistribution (String service, String microservice){
        if(microservice == null){
            /*return servicesWithLogs.get(service).getAllLogsBySeverity().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue().size()
                    ));*/
            return Collections.emptyMap();
        } else{
            for (Microservice m:services.get(service)){
                if (m.getName().equals(microservice)){
                    return m.getLogsBySeverity().entrySet().stream()
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    entry -> entry.getValue().size()
                            ));
                }
            }
        }
        return Collections.emptyMap();
    }
    public void displayLogs(String service, String microservice, String order){
        Comparator<Log> comparator = ComparatorBuilder.makeComp(order);
        Microservice micro = services.get(service).stream()
                .filter(m -> m.getName().equals(microservice))
                .findFirst()
                .orElse(null);
        if (micro != null){
            micro.getLogs().stream().sorted(comparator).forEach(x -> {
                System.out.printf("%s|%s", service, microservice);
                System.out.println(x);
            });
        } else{
            services.get(service).stream()
                    .map(Microservice::getLogs)
                    .flatMap(Collection::stream)
                    .sorted(comparator)
                    .forEach(x -> {
                        System.out.printf("%s|%s", service, x.getMicro());
                        System.out.println(x);
                    });
        }
    }
}

public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
            } else if (line.startsWith("displayLogs")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);

                collector.displayLogs(service, microservice, order);
            }
        }
    }
}
