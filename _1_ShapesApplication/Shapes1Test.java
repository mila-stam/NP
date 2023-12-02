package np.zadachi_za_vezhbanje.prv_kolokvium._1_ShapesApplication;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Square implements Comparable<Square> {
    String canvas_id;
    int [] sizes;

    public int[] getSizes() {
        return sizes;
    }

    public String getCanvas_id() {
        return canvas_id;
    }

    public Square(String line) {
        String []array = line.split("\\s+");
        this.canvas_id = array[0];
        int j=0;
        this.sizes = new int[array.length-1];
        for (int i = 1; i<array.length; i++){
            this.sizes[j] = Integer.parseInt(array[i]);
            j++;
        }
    }
    public int totalPerimeter(){
        int perimeter = 0;
        for (int size : sizes) {
            perimeter = perimeter + (size * 4);
        }
        return perimeter;
    }
    @Override
    public int compareTo(@NotNull Square other) {
        return Integer.compare(this.totalPerimeter(), other.totalPerimeter());
    }

    @Override
    public String toString() {
        return canvas_id + " " + sizes.length + " " + totalPerimeter();
    }
}
class ShapesApplication{

    List<Square> canvases = new ArrayList<>();
    public ShapesApplication() {
    }
    public int readCanvases(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        canvases = br.lines().map(Square::new).collect(Collectors.toList());
        return canvases.stream()
                .mapToInt(square -> square.getSizes().length)
                .sum();
    }

    public void printLargestCanvasTo (OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        Square max = canvases.stream()
                .max(Square::compareTo)
                .orElse(null);
        pw.println(max);
        pw.flush();
        pw.close();
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
