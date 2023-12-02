package np.zadachi_za_vezhbanje.prv_kolokvium._9_triple_generic;

import java.util.Scanner;

import static java.util.Collections.min;

class Triple<T extends Number> {
    T a;
    T b;
    T c;
    double num1;
    double num2;
    double num3;

    public Triple(T a, T b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
        num1 = Double.parseDouble(a + "");
        num2 = Double.parseDouble(b + "");
        num3 = Double.parseDouble(c + "");

    }

    public double max() {
        if (num1 >= num2 && num1 >= num3) {
            return num1;
        } else if (num2 >= num1 && num2 >= num3) {
            return num2;
        } else {
            return num3;
        }
    }

    ;

    public double avarage() {
        return (num1 + num2 + num3) / 3.0;
    }

    public void sort() {
        double t1 = num1;
        double t2 = num2;
        double t3 = num3;
        if (t1 >= t2 && t1 >= t3) {
            num3 = t1;
            if (t2 >= t3) {
                num2 = t2;
                num1 = t3;
            } else {
                num2 = t3;
                num1 = t2;
            }
        }

        if (t2 >= t3 && t2 >= t1) {
            num3 = t2;
            if (t1 >= t3) {
                num2 = t1;
                num1 = t3;
            } else {
                num2 = t3;
                num1 = t1;
            }
        }

        if (t3 >= t1 && t3 >= t2) {
            num3 = t3;
            if (t1 >= t2) {
                num2 = t1;
                num1 = t2;
            } else {
                num2 = t2;
                num1 = t1;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", num1, num2, num3);
    }
}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple



