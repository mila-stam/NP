package np.vtor_kolok._9_Stadion;

import java.util.*;

class SeatTakenException extends Exception {
    SeatTakenException() {
        super("SeatTakenException");
    }
}

class SeatNotAllowedException extends Exception {
    SeatNotAllowedException() {
        super("SeatNotAllowedException");
    }
}

class Sector {
    String code;
    int numSeats;
    int type;
    Map<Integer, Boolean> isTaken;
    int takenSeats;

    public Sector(String code, int numSeats) {
        this.code = code;
        this.numSeats = numSeats;
        this.type = 0;
        isTaken = new HashMap<>();
        for (int i = 1; i <= numSeats; i++) {
            isTaken.put(i, false);
        }
        takenSeats = 0;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public boolean checkAvail(int seat) {
        return isTaken.get(seat);
    }

    public void changeAvail(int seat) {
        isTaken.replace(seat, true);
        takenSeats++;
    }

    public int freeSeats() {
        return numSeats - takenSeats;
    }

    public double getPercent() {
        /*return (numSeats-takenSeats)/100.0;*/
        return (takenSeats * 100.0) / numSeats;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%", code, freeSeats(), numSeats, getPercent());
    }
}

class Stadium {
    String name;
    Map<String, Sector> nameToSector;

    Stadium(String name) {
        this.name = name;
        nameToSector = new HashMap<>();
    }

    public void createSectors(String[] sectorNames, int[] sizes) {
        for (int i = 0; i < sectorNames.length; i++) {
            nameToSector.put(sectorNames[i], new Sector(sectorNames[i], sizes[i]));
        }
    }

    public void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        if (nameToSector.get(sectorName).checkAvail(seat)) {
            throw new SeatTakenException();
        }
        if (nameToSector.get(sectorName).getType() == 0) {
            nameToSector.get(sectorName).setType(type);
        } else if (nameToSector.get(sectorName).getType() == 1 && type == 2) {
            throw new SeatNotAllowedException();
        } else if (nameToSector.get(sectorName).getType() == 2 && type == 1) {
            throw new SeatNotAllowedException();
        }

        nameToSector.get(sectorName).changeAvail(seat);
    }

    public void showSectors() {
        nameToSector.values().stream()
                .sorted(Comparator.comparing(Sector::freeSeats).reversed())
                .forEach(System.out::println);
    }

}

public class StaduimTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

