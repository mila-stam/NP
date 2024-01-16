package np.zadachi_za_vezhbanje.prv_kolokvium._24_risk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Move{
    List<Integer> attack;
    List<Integer> defence;

    public Move(String line) {
        String []parts = line.split("[\\s+;]");
        attack = new ArrayList<>();
        defence = new ArrayList<>();
        attack.add(Integer.parseInt(parts[0]));
        attack.add(Integer.parseInt(parts[1]));
        attack.add(Integer.parseInt(parts[2]));
        defence.add(Integer.parseInt(parts[3]));
        defence.add(Integer.parseInt(parts[4]));
        defence.add(Integer.parseInt(parts[5]));
    }
    public void sort(){
        attack = attack.stream().sorted().collect(Collectors.toList());
        defence = defence.stream().sorted().collect(Collectors.toList());
    }
    public boolean isSucc(){
        sort();
        if(attack.get(0) > defence.get(0)
                && attack.get(1) > defence.get(1)
                && attack.get(2) > defence.get(2)){
            return true;
        } else return false;
    }
}
class Risk{
    private List<Move> moves;

    public Risk() {
        moves = new ArrayList<>();
    }

    public int processAttacksData(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        moves = br.lines()
                .map(Move::new)
                .collect(Collectors.toList());
        return (int) moves.stream().filter(Move::isSucc).count();
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}