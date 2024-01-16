package np.zadachi_za_vezhbanje.prv_kolokvium._17_vesnik;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String name) {
        super(String.format("Category %s was not found", name));
    }
}

class Category implements Comparable<Category> {
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(@NotNull Category o) {
        return name.compareTo(o.name);
    }
}

abstract class NewsItem {
    private String title;
    private Date date;
    private Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }
    abstract public String getTeaser();
    public int when(){
        Date now = new Date();
        long ms = now.getTime()-date.getTime();
        return (int) (ms/1000)/60;
    }
}

class TextNewsItem extends NewsItem {
    private String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }

    @Override
    public String getTeaser() {
        String teaser = text;
       if (text.length() > 80){
           teaser = text.substring(0, 80);
       }
        return String.format("%s\n%d\n%s\n", getTitle(), when(), teaser);
    }
}

class MediaNewsItem extends NewsItem {
    private String url;
    private int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", getTitle(), when(), url, views);
    }
}

class FrontPage {
    private List<NewsItem> news;
    Category[] categories;

    public FrontPage(Category[] categories) {
        this.categories = categories;
        news = new ArrayList<>();
    }

    void addNewsItem(NewsItem newsItem) {
        if (newsItem instanceof TextNewsItem) {
            TextNewsItem tnew = (TextNewsItem) newsItem;
            news.add(tnew);
        } else {
            MediaNewsItem mnew = (MediaNewsItem) newsItem;
            news.add(mnew);
        }
    }
    public List<NewsItem> listByCategory(Category category){
        List<NewsItem> filteredList = new ArrayList<>(news);

        return filteredList.stream()
                .filter(l -> l.getCategory().getName().equals(category.getName()))
                .collect(Collectors.toList());
    }
    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        int flag = 0;
        for (Category c : categories) {
            if (c.getName().equals(category)) {
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            throw new CategoryNotFoundException(category);
        }

        List<NewsItem> filteredList = new ArrayList<>(news);

        return filteredList.stream()
                .filter(l -> l.getCategory().getName().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (NewsItem newsItem : news) {
            sb.append(newsItem.getTeaser());
        }
        return sb.toString();
    }
}

public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for (Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch (CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde