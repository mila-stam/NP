package np.vtor_kolok._38_quiz;

import java.io.InputStream;
import java.util.*;
class IncorrectNumOfAnswersException extends Exception{
    IncorrectNumOfAnswersException(){
        super("A quiz must have same number of correct and selected answers");
    }
}
class Quiz{
    public static double processPoints(String [] correct, String[] given) throws IncorrectNumOfAnswersException {
        if (correct.length != given.length){
            throw new IncorrectNumOfAnswersException();
        }
        double totalPoints=0;
        for (int i=0; i<correct.length; i++){
            if (correct[i].equals(given[i])){
                totalPoints+=1;
            }else {
                totalPoints-=0.25;
            }
        }
        return totalPoints;
    }
}
class QuizProcessor{
    public static Map<String, Double> processAnswers(InputStream is) {
        Scanner sc = new Scanner(is);
        Map<String, Double> pointsByStudents = new TreeMap<>();
        while (sc.hasNextLine()){
                String line = sc.nextLine();
                String [] parts = line.split(";");
                String index = parts[0];
                String [] correctAnswers = parts[1].split(", ");
                String [] studentAnswers = parts[2].split(", ");
            try{
                double totalPoints = Quiz.processPoints(correctAnswers, studentAnswers);
                pointsByStudents.put(index, totalPoints);
            } catch (IncorrectNumOfAnswersException e) {
                System.out.println(e.getMessage());
            }

        }
        return pointsByStudents;
    }
}
public class QuizProcessorTest {
    public static void main(String[] args){
            QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}
