package np.zadachi_za_vezhbanje.prv_kolokvium._6_min_max_generic;

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    T min;
    T max;
    int counterMax;
    int counterMin;
    int total;
    MinMax(){
        this.min = null;
        this.max = null;
        this.total = 0;
        this.counterMin = 0;
        this.counterMax = 0;
    }

    int getTotal(int max_c, int min_c){
        return max_c + min_c;
    }

    void update(T element){
        if(element == null){
            return;
        }
        total++;
        if(min == null || element.compareTo(min) < 0){
            min = element;
            counterMin = 1;
        } else {
            if(element.compareTo(min) == 0) {
                counterMin++;
            }
        }
        if(max == null || element.compareTo(max) > 0){
            max = element;
            counterMax=1;
        } else {
            if(element.compareTo(max) == 0) {
                counterMax++;
            }
        }
    }
    T max(){
        return max;
    }
    T min(){
        return min;
    }


    @Override
    public String toString() {
        return min() + " " + max() + " " + (total-(counterMin+counterMax)) + "\n";
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
