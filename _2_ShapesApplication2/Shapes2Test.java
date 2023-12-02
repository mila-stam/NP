package np.zadachi_za_vezhbanje.prv_kolokvium._2_ShapesApplication2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum TYPE{
    CIRCLE,
    SQUARE
}
class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String id, double max) {
        super(String.format("Canvas %s has a shape with area larger than %.2f", id, max));
    }
}
abstract class Shape{
    int size;

    public Shape(int size) {
        this.size = size;
    }

    abstract double area();
    abstract TYPE getType();

    public static Shape createShape(int size, char type, double maxArea){
        switch (type) {
            case 'S':
                return new Square(size);
            case 'C':
                return new Circle(size);
            default:
                return null;
        }
    }
}

class Square extends Shape{

    public Square(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size;
    }

    @Override
    public TYPE getType() {
        return TYPE.SQUARE;
    }
}
class Circle extends Shape{

    public Circle(int size) {
        super(size);
    }

    @Override
    public double area() {
        return size * size * Math.PI;
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }
}
class Canvas implements Comparable<Canvas>{
    String canvas_id;
    List<Shape> shapes;

    public Canvas(String canvas_id, List<Shape> shapes) {
        this.canvas_id = canvas_id;
        this.shapes = shapes;
    }

    public static Canvas createCanvas(String line, double maxArea) throws IrregularCanvasException {
        String [] array = line.split("\\s+");
        String canvas_id = array[0];
        List<Shape> shapes = new ArrayList<>();
        for (int i=1; i<array.length; i+=2){
            //bidejkji proveruvam vo samiot konstruktor koj tip e tuka samo go kreiram soodvetiot Shape
            Shape s = Shape.createShape(Integer.parseInt(array[i+1]), array[i].charAt(0), maxArea);
            if (s.area() > maxArea){
                throw new IrregularCanvasException(canvas_id, maxArea);
            }
            shapes.add(Shape.createShape(Integer.parseInt(array[i+1]), array[i].charAt(0), maxArea));
        }
        return new Canvas(canvas_id, shapes);
    }

    int getCirclesCount(){
        return (int) shapes.stream().filter(s -> s.getType().equals(TYPE.CIRCLE)).count();
    }
    @Override
    public String toString() {
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::area).summaryStatistics();
        return String.format("%s %d %d %d %.2f %.2f %.2f",
                canvas_id,
                shapes.size(),
                getCirclesCount(),
                shapes.size() - getCirclesCount(),
                dss.getMin(),
                dss.getMax(),
                dss.getAverage());
    }

    double totalArea(){
        return shapes.stream()
                .mapToDouble(Shape::area)
                .sum();
    }
    @Override
    public int compareTo(Canvas other) {
        return Double.compare(totalArea(), other.totalArea());
    }
}
class ShapesApplication{
    double maxArea;
    List<Canvas> canvases;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }

    public void readCanvases(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        canvases = br.lines()
                .map(line -> {
                    try{
                        return Canvas.createCanvas(line, maxArea);
                    } catch (IrregularCanvasException e){
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void printCanvases(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        canvases.stream().sorted(Comparator.reverseOrder()).forEach(pw::println);
        pw.flush();
        pw.close();
    }
}
public class Shapes2Test {

    public static void main(String[] args) {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
