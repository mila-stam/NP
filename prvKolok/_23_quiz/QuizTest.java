package np.zadachi_za_vezhbanje.prv_kolokvium._23_quiz;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

enum TYPE {
    MC, TF
}

class InvalidOperationException extends Exception {
    public InvalidOperationException(String message) {
        super(message);
    }
}

class Question implements Comparable<Question> {
    private TYPE type;
    private String text;
    private Integer points;
    private String answer;

    public Question(String line) throws InvalidOperationException {
        String[] parts = line.split(";");
        if (!parts[3].equals("A")
                && !parts[3].equals("B")
                && !parts[3].equals("C")
                && !parts[3].equals("D")
                && !parts[3].equals("E")
                && !parts[3].equals("true")
                && !parts[3].equals("false")
        ) {
            throw new InvalidOperationException(String.format("%s is not allowed option for this question", parts[3]));
        }
        this.type = TYPE.valueOf(parts[0]);
        this.text = parts[1];
        this.points = Integer.parseInt(parts[2]);
        this.answer = parts[3];
    }

    @Override
    public int compareTo(Question o) {
        return points.compareTo(o.points);
    }

    public TYPE getType() {
        return type;
    }

    public Integer getPoints() {
        return points;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        if (type.equals(TYPE.MC)) {
            return String.format("Multiple Choice Question: %s Points %d Answer: %s", text, points, answer);
        } else {
            return String.format("True/False Question: %s Points: %d Answer: %s", text, points, answer);
        }
    }
}

class Quiz {
    private List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    public void addQuestion(String questionData) throws InvalidOperationException {
        questions.add(new Question(questionData));
    }

    public void printQuiz(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        List<Question> sorted = questions.stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        questions.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(pw::println);
        pw.flush();
    }

    public void answerQuiz(List<String> answers, OutputStream os) throws InvalidOperationException {
        if (questions.size() != answers.size()) {
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        PrintWriter pw = new PrintWriter(os);
        double totalPoints=0;
        for (int i = 0; i < questions.size(); i++) {
            double points = 0;
            if (questions.get(i).getAnswer().equals(answers.get(i))) {
                points += questions.get(i).getPoints();
            } else {
                if (questions.get(i).getType().equals(TYPE.MC)) {
                    points -= (questions.get(i).getPoints() * 0.2);
                } else points += 0;
            }
            pw.println(String.format("%d. %.2f", i + 1, points));
            totalPoints+=points;
        }
        pw.println(String.format("Total points: %.2f", totalPoints));
        pw.flush();
    }
}

public class QuizTest {
    public static void main(String[] args) throws InvalidOperationException {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < questions; i++) {
            try {
                quiz.addQuestion(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }

        }

        List<String> answers = new ArrayList<>();

        int answersCount = Integer.parseInt(sc.nextLine());

        for (int i = 0; i < answersCount; i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) {
            quiz.printQuiz(System.out);
        } else if (testCase == 2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}

