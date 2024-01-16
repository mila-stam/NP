package np.vtor_kolok._21_phoneBook;

import java.util.*;

class DuplicateNumberException extends Exception {
    DuplicateNumberException(String number) {
        super(String.format("Duplicate number: %s", number));
    }
}

class Contact {
    String name;
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public List<String> getSubs() {
        List<String> substrings = new ArrayList<>();
        for (int i = 3; i <= number.length(); i++) {
            for (int j = 0; j <= number.length()-i; j++) {
                substrings.add(number.substring(j, j + i));
            }
        }
        return substrings;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return name + " " + number;
    }
}

class PhoneBook {
    Set<String> allNumbers;
    Map<String, Set<Contact>> contactsBySubstring;
    Map<String, Set<Contact>> contactsByName;

    public PhoneBook() {
        contactsByName = new HashMap<>();
        allNumbers = new HashSet<>();
        contactsBySubstring = new HashMap<>();
    }

    public void addContact(String name, String number) throws DuplicateNumberException {
        if (allNumbers.contains(number)) {
            throw new DuplicateNumberException(number);
        }
        allNumbers.add(number);
        Contact c = new Contact(name, number);
        contactsByName.putIfAbsent(name, new TreeSet<>(Comparator.comparing(Contact::getNumber)));
        contactsByName.get(name).add(c);
        List<String> subs = c.getSubs();
        for (String s : subs) {
            contactsBySubstring.putIfAbsent(s, new TreeSet<>(Comparator.comparing(Contact::getName).thenComparing(Contact::getNumber)));
            contactsBySubstring.get(s).add(c);
        }
    }

    public void contactsByNumber(String number) {
        Set<Contact> contacts = contactsBySubstring.get(number);
        if (contacts == null){
            System.out.println("NOT FOUND");
            return;
        }
                contacts.forEach(System.out::println);
    }

    public void contactsByName(String name) {
        Set<Contact> contacts = contactsByName.get(name);
        if (contacts == null) {
            System.out.println("NOT FOUND");
            return;
        }
        contacts.forEach(System.out::println);
    }
}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде


