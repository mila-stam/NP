package np.vtor_kolok._24_movieList;

import java.util.*;
import java.util.stream.Collectors;

class Movie /*implements Comparable<Movie>*/{
    String title;
    List<Integer> ratings;

    public Movie(String title, int [] ratings) {
        this.title = title;
        this.ratings = Arrays.stream(ratings)
                .boxed()
                .collect(Collectors.toList());
    }

    public String getTitle() {
        return title;
    }

    public List<Integer> getRatings() {
        return ratings;
    }

    public double getAvgRate(){
        return ratings.stream()
                .mapToDouble(i -> i)
                .average()
                .orElse(0);
    }
    public double getCoefRate(){
        return (getAvgRate()*ratings.size()/MoviesList.getCR());
    }
    /*@Override
    public int compareTo(Movie o) {
        int res = Double.compare(this.getAvgRate(), o.getAvgRate());
        if (res == 0) {
            return title.compareTo(o.title);
        }
        return res;
    }*/

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, getAvgRate(), ratings.size());
    }
}
class MoviesList{
    static List<Movie> movies;
    MoviesList(){
        movies = new ArrayList<>();
    }
    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, ratings));
    }
    public List<Movie> top10ByAvgRating(){
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getAvgRate).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }
    public static int getCR(){
        return movies.stream()
                .mapToInt(m -> m.getRatings().size())
                .max()
                .orElse(0);
    }
    public List<Movie> top10ByRatingCoef(){
        return movies.stream()
                .sorted(Comparator.comparing(Movie::getCoefRate).reversed().thenComparing(Movie::getTitle))
                .limit(10)
                .collect(Collectors.toList());
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde
