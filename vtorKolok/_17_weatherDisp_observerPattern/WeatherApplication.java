package np.vtor_kolok._17_weatherDisp_observerPattern;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Observer {
    void update(float temperature, float humidity, float pressure);

    //void display(); //mozhe da se napishe vo poseben interface DisplayElement, koj kje koristi samo da prikazhuvanje na elementite
}

/*interface Subject {
    void remove(Observer o);

    void register(Observer o);
    //void notifyObservers();
}*/

class WeatherDispatcher /*implements Subject*/ {
    float temperature;
    float humidity;
    float pressure;
    List<Observer> observers;

    WeatherDispatcher() {
        observers = new ArrayList<>();
        temperature=-100;
        humidity=-100;
        pressure=0;
    }
    public void setMeasurements(float temperature, float humidity, float pressure){
        this.temperature=temperature;
        this.humidity=humidity;
        this.pressure=pressure;
        observers.forEach(o -> o.update(temperature, humidity, pressure));
    }


    public void remove(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }


    public void register(Observer o) {
        observers.add(o);
    }
}
class CurrentConditionsDisplay implements Observer{
    float temperature;
    float humidity;
    WeatherDispatcher weatherDispatcher;

    public CurrentConditionsDisplay(WeatherDispatcher wd) {
        this.weatherDispatcher = wd;
        weatherDispatcher.register(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature=temperature;
        this.humidity=humidity;
        display();
    }

    public void display() {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n", temperature, humidity);
    }
}
class ForecastDisplay implements Observer{
    float temperature;
    float humidity;
    float currPressure;
    float lastPressure;
    WeatherDispatcher weatherDispatcher;
    ForecastDisplay(WeatherDispatcher wd){
        this.weatherDispatcher=wd;
        weatherDispatcher.register(this);
    }
    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature=temperature;
        this.humidity=humidity;
        this.lastPressure=currPressure;
        this.currPressure=pressure;
        display();
    }

    public void display() {
        System.out.printf("Temperature: %.1fF\nHumidity: %.1f%%\n%n", temperature, humidity);
        if(currPressure>lastPressure){
            System.out.println("Forecast: Improving");
        } else if (currPressure==lastPressure) {
            System.out.println("Forecast: Same");
        } else{
            System.out.println("Forecast: Cooler");
        }
    }
}
public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if (parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if (operation == 1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if (operation == 2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if (operation == 3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if (operation == 4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}
