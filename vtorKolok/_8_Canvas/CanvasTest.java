package np.vtor_kolok._8_Canvas;

import java.awt.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
class InvalidDimensionException extends Exception{
    InvalidDimensionException (){
        super("Dimension 0 is not allowed!");
    }
}
class InvalidIDException extends Exception{
    InvalidIDException (String id){
        super(String.format("ID %s is not valid", id));
    }
}
interface Form{
    double perimeter();
    double area();
    void scale(double c);
}
class Circle implements Form{
    double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double perimeter() {
        return 2*radius*Math.PI;
    }

    @Override
    public double area() {
        return radius*radius*Math.PI;
    }

    @Override
    public void scale(double c) {
        radius*=c;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, area(), perimeter());
    }
}
class Square implements Form{
    double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public double perimeter() {
        return 4*side;
    }

    @Override
    public double area() {
        return side*side;
    }

    @Override
    public void scale(double c) {
        side*=c;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", side, area(), perimeter());
    }
}
class Rectangle implements Form{
    double side1;
    double side2;

    public Rectangle(double side1, double side2) {
        this.side1 = side1;
        this.side2 = side2;
    }

    @Override
    public double perimeter() {
        return 2*(side1+side2);
    }

    @Override
    public double area() {
        return side1*side2;
    }

    @Override
    public void scale(double c) {
        side1*=c;
        side2*=c;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", side1, side2, area(), perimeter());
    }
}
class ShapesMaker{
    private static boolean checkId(String id){
        if (id.length()!=6){
            return false;
        }
        return id.matches("[a-zA-Z0-9]+");
    }
    public static Form createShape(String line) throws InvalidDimensionException{
        String [] parts = line.split("\\s+");
        int type = Integer.parseInt(parts[0]);
        double firstDimension = Double.parseDouble(parts[2]);
        if (firstDimension ==0.0){
            throw new InvalidDimensionException();
        }
        if (type == 1){
            return new Circle(firstDimension);
        } else if (type == 2) {
            return new Square(firstDimension);
        } else {
            double secondDimension = Double.parseDouble(parts[3]);
            if (secondDimension ==0.0){
                throw new InvalidDimensionException();
            }
            return new Rectangle(firstDimension, secondDimension);
        }
    }
    public static String exctractId(String line) throws InvalidIDException {
        String [] parts = line.split("\\s+");
        String id = parts[1];
        if (!checkId(id)){
            throw new InvalidIDException(id);
        }
        return id;
    }
}
class Canvas{
    Set<Form> allShapes;
    Map<String, Set<Form>> shapesById;
    Canvas(){
        allShapes = new TreeSet<>(Comparator.comparing(Form::area));
        shapesById = new TreeMap<>();
    }
    public void readShapes(InputStream is){
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            try{
                String userId = ShapesMaker.exctractId(line);
                Form newShape = ShapesMaker.createShape(line);
                allShapes.add(newShape);
                shapesById.putIfAbsent(userId, new TreeSet<>(Comparator.comparing(Form::perimeter)));
                shapesById.get(userId).add(newShape);
            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
            } catch (InvalidDimensionException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
    public void scaleShapes(String userID, double coef){
        /*shapesById.get(userID)
                .forEach(form -> form.scale(coef));*/
        shapesById.getOrDefault(userID, new HashSet<>())
                .forEach(form -> form.scale(coef));
    }
    public void printAllShapes(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        allShapes.forEach(pw::println);
        pw.flush();
    }
    public void printByUserId(OutputStream os){
        PrintWriter pw = new PrintWriter(os);

        Comparator<Map.Entry<String, Set<Form>>> comparator = Comparator.comparing(entry -> entry.getValue().size());
        shapesById.entrySet().stream()
                .sorted(comparator.reversed().thenComparing(entry -> entry.getValue().stream().mapToDouble(Form::area).sum()))
                .forEach(entry -> {
                    pw.println("Shapes of user: " + entry.getKey());
                    entry.getValue().forEach(pw::println);
                });
        pw.flush();
    }
    public void statistics(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics dss = allShapes.stream().mapToDouble(Form::area).summaryStatistics();
        pw.println(String.format("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f", dss.getCount(), dss.getSum(), dss.getMin(), dss.getAverage(), dss.getMax()));
        pw.flush();
    }

}
public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        canvas.readShapes(System.in);

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}