package np.zadachi_za_vezhbanje.prv_kolokvium._8_ArchiveStore;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception{
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}
abstract class Archive{
    protected int id;
    protected LocalDate dateArchived;

    public void archive(LocalDate date){
        dateArchived = date;
    }

    public int getId() {
        return id;
    }

    abstract public boolean canOpen(LocalDate date);

    @Override
    public boolean equals(Object obj) {
        Archive archive = (Archive) obj;
        return this.id == archive.id;
    }
}
class LockedArchive extends Archive{
    private LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        this.id = id;
        this.dateToOpen = dateToOpen;
    }

    public LocalDate getDateToOpen() {
        return dateToOpen;
    }

    @Override
    public boolean canOpen(LocalDate date){
        return dateToOpen.isBefore(date);
    }
}
class SpecialArchive extends Archive{
    private int maxOpen;
    public int countOpened;

    public SpecialArchive(int id, int maxOpen) {
        this.id=id;
        this.maxOpen = maxOpen;
        this.countOpened = 0;
    }

    public int getMaxOpen() {
        return maxOpen;
    }

    @Override
    public boolean canOpen(LocalDate date){
        if (countOpened < maxOpen){
            ++countOpened;
            return true;
        }
        return false;
    }
}
class ArchiveStore{
    List<Archive> archives;
    private StringBuilder text;

    public ArchiveStore() {
        this.archives = new ArrayList<>();
        this.text = new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date){
        item.archive(date);
        archives.add(item);
        text.append(String.format("Item %d was archived at %s\n", item.getId(), date.toString()));
    }

    public void openItem(int id, LocalDate date) throws NonExistingItemException {
        int flag=0;
        for (Archive item:archives){
            if(item.getId() == id){
                flag=1;
                if (item.canOpen(date)){
                    text.append(String.format("Item %d opened at %s\n", id, date.toString()));
                } else{
                    if (item instanceof LockedArchive){
                        text.append(String.format("Item %d cannot be opened before %s\n", id, ((LockedArchive) item).getDateToOpen().toString()));
                    }
                    else if(item instanceof SpecialArchive){
                        text.append(String.format("Item %d cannot be opened more than %d times\n", id, ((SpecialArchive) item).getMaxOpen()));
                    }
                }
            }
        }
        if (flag==0){
            throw new NonExistingItemException(id);
        }

    }

    public StringBuilder getLog() {
        return text;
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
