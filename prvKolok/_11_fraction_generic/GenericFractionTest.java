package np.zadachi_za_vezhbanje.prv_kolokvium._11_fraction_generic;

import java.util.Scanner;
class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException() {
        super("Denominator cannot be zero");
    }
}
class GenericFraction <T extends Number, U extends Number>{
    T numerator;
    U denominator;

    public double toDouble(){
        return numerator.doubleValue()/denominator.doubleValue();
    }

    public double greatestCommonDenominator(double a, double b){
        if (b == 0)
            return a;
        if (a < b)
            return greatestCommonDenominator(a, b - a);
        else
            return greatestCommonDenominator(b, a - b);
    }
    public double gcd() {
        return greatestCommonDenominator(this.numerator.doubleValue(), this.denominator.doubleValue());
    }
    @Override
    public String toString() {
        double gdc = this.gcd();
        return String.format("%.2f / %.2f", numerator.doubleValue()/gdc, denominator.doubleValue()/gdc);
    }

    public GenericFraction (T numerator, U denominator) throws ZeroDenominatorException {
        if(denominator.doubleValue() == 0){
            throw new ZeroDenominatorException();
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }
    GenericFraction <Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        double resultNumerator = this.numerator.doubleValue() * gf.denominator.doubleValue() + gf.numerator.doubleValue() * this.denominator.doubleValue();
        double resultDenominator = this.denominator.doubleValue() * gf.denominator.doubleValue();

        return new GenericFraction<>(resultNumerator, resultDenominator);
    }


}
public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}



