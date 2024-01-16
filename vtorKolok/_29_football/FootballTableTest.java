package np.vtor_kolok._29_football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Team {
    String name;
    int playedMatches;
    int wins;
    int ties;
    int loses;
    int scored;
    int gotten;

    public Team(String name) {
        this.name = name;
        this.playedMatches = 0;
        this.wins = 0;
        this.ties = 0;
        this.loses=0;
        this.scored=0;
        this.gotten=0;
    }

    public void setStats(boolean wins, boolean ties, boolean loses, int scored, int gotten) {
        this.playedMatches++;
        if (wins) {
            this.wins++;
        }
        if (ties) {
            this.ties++;
        }
        if (loses){
            this.loses++;
        }
        this.scored+=scored;
        this.gotten+=gotten;
    }

    public String getName() {
        return name;
    }
    public int getDiff(){
        return scored-gotten;
    }

    public int getPoints() {
        return (wins * 3) + ties;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, playedMatches, wins, ties, loses, getPoints());
    }
}

class FootballTable {
    Map<String, Team> teams;
    FootballTable(){
        teams = new TreeMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals) {
        Team home = new Team(homeTeam);
        Team guest = new Team(awayTeam);
        teams.putIfAbsent(homeTeam, home);
        teams.putIfAbsent(awayTeam, guest);
        if (homeGoals == awayGoals){
            teams.get(homeTeam).setStats(false, true, false, homeGoals, awayGoals);
            teams.get(awayTeam).setStats(false, true, false, awayGoals, homeGoals);
        } else if (homeGoals > awayGoals) {
            teams.get(homeTeam).setStats(true, false, false, homeGoals, awayGoals);
            teams.get(awayTeam).setStats(false, false, true, awayGoals, homeGoals);
        } else {
            teams.get(homeTeam).setStats(false, false, true, homeGoals, awayGoals);
            teams.get(awayTeam).setStats(true, false, false, awayGoals, homeGoals);
        }
    }
    public void printTable(){
        List<Team> teamList = teams.values().stream()
                .sorted(Comparator.comparing(Team::getPoints).thenComparing(Team::getDiff).reversed().thenComparing(Team::getName))
                .collect(Collectors.toList());
        int i=1;
        for (Team t: teamList){
            System.out.printf("%2d.", i);
            System.out.println(t);
            i++;
        }
    }
}

public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

