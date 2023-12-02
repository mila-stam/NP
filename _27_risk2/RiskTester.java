package np.zadachi_za_vezhbanje.prv_kolokvium._27_risk2;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Move{
    List<Integer> attack;
    List<Integer> defense;

    public Move(String line) {
        String []parts = line.split("[\\s+;]");
        attack = new ArrayList<>();
        defense = new ArrayList<>();
        attack.add(Integer.parseInt(parts[0]));
        attack.add(Integer.parseInt(parts[1]));
        attack.add(Integer.parseInt(parts[2]));
        defense.add(Integer.parseInt(parts[3]));
        defense.add(Integer.parseInt(parts[4]));
        defense.add(Integer.parseInt(parts[5]));
    }
    public void sort(){
        attack.sort(Comparator.reverseOrder());
        defense.sort(Comparator.reverseOrder());
    }
    public String result(){
        sort();
        int rAtt=0, rDef=0;
        for (int i=0; i<3; i++){
            if (attack.get(i) > defense.get(i)){
                rAtt++;
            } else rDef++;
        }
        return rAtt + " " + rDef;
    }
}
class Risk{
    List<Move> moves;
    public void processAttacksData (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        moves = br.lines()
                .map(Move::new)
                .collect(Collectors.toList());
        for (Move m : moves){
            System.out.println(m.result());
        }
    }
}
public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}