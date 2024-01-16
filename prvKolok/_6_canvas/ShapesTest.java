package np.zadachi_za_vezhbanje.prv_kolokvium._6_canvas;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

enum Color {
    RED, GREEN, BLUE
}

interface Scalable {
    public void scale(float scaleFactor);
}

interface Stackable {
    public float weight();
}

abstract class Form implements Scalable, Stackable, Comparable<Form> {
    private String id;
    private Color color;

    public Form(String id, Color color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    abstract public void scale(float scaleFactor);

    abstract public float weight();

    @Override
    public int compareTo(Form o) {
        return Double.compare(weight(), o.weight());
    }
}

class Circle extends Form{
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius*=scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (radius * radius * Math.PI);
    }
    @Override
    public String toString() {
        return String.format("C: %-5s %-10s %10.2f\n", getId(), getColor(), weight());
    }
}

class Square extends Form{
    private float width;
    private float height;

    public Square(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width*=scaleFactor;
        height*=scaleFactor;
    }

    @Override
    public float weight() {
        return width * height;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s %-10s %10.2f\n", getId(), getColor(), weight());
    }
}

class Canvas {
    List<Form> forms;

    public Canvas() {
        forms = new ArrayList<>();
    }

    public void add(String id, Color color, float radius) {
        Circle c = new Circle(id, color, radius);
        forms.add(c);
        forms = forms.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public void add(String id, Color color, float width, float height) {
        Square s = new Square(id, color, width, height);
        forms.add(s);
        forms = forms.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
    public void scale(String id, float scaleFactor){
        for (Form f:forms){
            if (f.getId().equals(id)){
                f.scale(scaleFactor);
                break;
            }
        }
        forms = forms.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        forms.forEach(sb::append);
        return sb.toString();
    }
}
