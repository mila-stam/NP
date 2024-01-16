package np.zadachi_za_vezhbanje.prv_kolokvium._13_component_neProagjaNaCourses;

import java.util.*;

class Component implements Comparable<Component> {
    private String color;
    private int weigth;
    private List<Component> insideComponents;

    public Component(String color, int weigth) {
        this.color = color;
        this.weigth = weigth;
        insideComponents = new ArrayList<>();
    }

    public String getColor() {
        return color;
    }

    public int getWeigth() {
        return weigth;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addComponent(Component component){
        insideComponents.add(component);
        insideComponents.sort(Comparator.naturalOrder());
    }

    @Override
    public int compareTo(Component o) {
        if (weigth == o.getWeigth()){
            return color.compareTo(o.getColor());
        }
        return Integer.compare(weigth, o.getWeigth());
    }
}
class InvalidPositionException extends Exception{
    public InvalidPositionException(int pos){
        super(String.format("Invalid position %d, already taken!", pos));
    }
}
class Window{
    private String name;
    private Component[] components;
    private boolean [] taken;

    public Window(String name) {
        this.name = name;
    }
    public void addComponent(int position, Component component) throws InvalidPositionException {
        if (components.length == 0){
            components = new Component[position+1];
            components[position]= component;
            taken = new boolean[position+1];
            taken[position] = true;
            for (int i =0; i<position-1; i++){
                taken[i] = false;
            }
        } else{
            if (taken[position]){
                throw new InvalidPositionException(position);
            }
            if(position > components.length){
                int k = components.length;
                components = Arrays.copyOf(components, position+1);
                components[position] = component;
                taken[position] = true;
                for (int i=k; i<position-1; i++){
                    taken[i] = false;
                }
            } else{
                components[position] = component;
                taken[position] = true;
            }
        }
    }


    public void changeColor(int weight, String color){
        for (Component c:components){
            if (c.getWeigth() < weight){
                c.setColor(color);
            }
        }
    }

    public void swichComponents(int pos1, int pos2){
        Component temp = components[pos1];
        components[pos1] = components[pos2];
        components[pos2] = temp;
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде
