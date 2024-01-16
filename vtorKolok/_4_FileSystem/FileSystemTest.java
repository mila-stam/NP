package np.vtor_kolok._4_FileSystem;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

class File implements Comparable<File> {
    String name;
    Integer size;
    LocalDateTime createdAt;

    public File(String name, Integer size, LocalDateTime createdAt) {
        this.name = name;
        this.size = size;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }

    public int getYear() {
        return createdAt.getYear();
    }
    public String getMonth(){
        return createdAt.getMonth().name();
    }
    public int getDay(){
        return createdAt.getDayOfMonth();
    }
    @Override
    public int compareTo(File o) {
        if (this.createdAt.isEqual(o.createdAt)) {
            int res = this.name.compareTo(o.name);
            if (res == 0) {
                return Integer.compare(this.size, o.size);
            }
            return res;
        }
        return this.createdAt.compareTo(o.createdAt);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %tFT00:00", name, size, createdAt);
    }
}

class FileSystem {
    Map<Character, Set<File>> folders;

    public FileSystem() {
        folders = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt) {
        File f = new File(name, size, createdAt);
        folders.computeIfAbsent(folder, x -> new TreeSet<>());
        folders.get(folder).add(f);
    }

    public List<File> findAllHiddenFilesWithSizeLessThen(int size) {
        return folders.values().stream() //se pravi stream od Set<File> odnosno, Collection<File>
                .flatMap(Collection::stream)//se koristi flatMap za da se dobie stream od FILE a ne stream od kolekcija na files
                .filter(file -> file.getName().startsWith(".") && file.getSize() < size)
                .collect(Collectors.toList());
    }

    public int totalSize(Character c) {
        return folders.get(c).stream()
                .mapToInt(File::getSize)
                .sum();
    }

    public int totalSizeOfFilesFromFolders(List<Character> folders1) {
        return folders.keySet().stream()
                .filter(folders1::contains)
                .mapToInt(this::totalSize)
                .sum();
    }

    public Map<Integer, Set<File>> byYear() {
        /*Map<Integer, Set<File>> sortedByYear = new HashMap<>();
        List<File> files = folders.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (File f : files) {
            sortedByYear.computeIfAbsent(f.getYear(), x->new TreeSet<>());
            sortedByYear.get(f.getYear()).add(f);
        }
        return sortedByYear;*/
        return folders.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        File::getYear,
                        HashMap::new,
                        Collectors.toCollection(TreeSet::new)
                ));
    }
    public Map<String, Long> sizeByMonthAndDay(){
        /*Map<String, Set<File>> sortedByMonthAndDay = new HashMap<>();
        List<File> files = folders.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (File f:files){
            String monthAndDay = f.getMonth() + "-" + f.getDay();
            sortedByMonthAndDay.computeIfAbsent(monthAndDay, x->new TreeSet<>());
            sortedByMonthAndDay.get(monthAndDay).add(f);
        }
        Map<String, Long> sizeByMandD = new HashMap<>();
        for (String key : sortedByMonthAndDay.keySet()){
            long totalSum = sortedByMonthAndDay.get(key).stream()
                    .mapToInt(File::getSize)
                    .sum();
            sizeByMandD.put(key, totalSum);
        }
        return sizeByMandD;*/
        return folders.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(
                        f -> f.getMonth() + "-" + f.getDay(),
                        Collectors.summingLong((File::getSize))
                ));
    }
}



public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here


