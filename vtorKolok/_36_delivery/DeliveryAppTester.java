package np.vtor_kolok._36_delivery;

import java.util.*;

class DeliveryPerson {
    String id;
    String name;
    Location currentLocation;
    int numOfOrders;
    float earnedMoney;

    public DeliveryPerson() {
    }

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.numOfOrders = 0;
        this.earnedMoney = 0;
    }

    public void setLocation(Location loc) {
        this.currentLocation = loc;
    }

    public void addMoney(int distance) {
        int total = 90;
        total += (distance / 10) * 10;
        earnedMoney += total;
    }

    public void addOrder() {
        numOfOrders++;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public float getEarnedMoney() {
        return earnedMoney;
    }

    public int getNumOfOrders() {
        return numOfOrders;
    }

    public String getId() {
        return id;
    }

    public double getAvg() {
        if (numOfOrders == 0) {
            return 0;
        } else {
            return earnedMoney / numOfOrders;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", id, name, numOfOrders, earnedMoney, getAvg());
    }
}

class Restaurant {
    String id;
    String name;
    Location location;
    List<Float> orders;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        orders = new ArrayList<>();
    }

    public void addOrder(float order) {
        orders.add(order);
    }

    public double getAvgOrder() {
        return orders.stream()
                .mapToDouble(x -> x)
                .average()
                .orElse(0);
    }

    public double getSumOrders() {
        return orders.stream()
                .mapToDouble(x -> x)
                .sum();
    }

    public Location getLocation() {
        return location;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", id, name, orders.size(), getSumOrders(), getAvgOrder());
    }
}

class User {
    String id;
    String name;
    Map<String, Location> locationByAddress;
    float spentMoney;
    int totalOrders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        locationByAddress = new HashMap<>();
        spentMoney = 0;
        totalOrders = 0;
    }

    public void addAddress(String name, Location loc) {
        locationByAddress.put(name, loc);
    }

    public void payOrder(float price) {
        spentMoney += price;
        totalOrders++;
    }

    public float getSpentMoney() {
        return spentMoney;
    }

    public String getId() {
        return id;
    }

    public float getAvg() {
        if (totalOrders == 0) {
            return 0;
        } else {
            return spentMoney / totalOrders;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", id, name, totalOrders, spentMoney, getAvg());
    }
}

class DeliveryApp {
    String name;
    Map<String, DeliveryPerson> deliveryPeople;
    Map<String, Restaurant> restaurants;
    Map<String, User> users;

    public DeliveryApp(String name) {
        this.name = name;
        this.deliveryPeople = new HashMap<>();
        this.restaurants = new HashMap<>();
        this.users = new HashMap<>();
    }

    public void registerDeliveryPerson(String id, String name, Location currentLocation) {
        deliveryPeople.put(id, new DeliveryPerson(id, name, currentLocation));
    }

    public void addRestaurant(String id, String name, Location location) {
        restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addUser(String id, String name) {
        users.put(id, new User(id, name));
    }

    public void addAddress(String id, String addressName, Location location) {
        users.get(id).addAddress(addressName, location);
    }

    public String getClosestPerson(Location restLoc) {
        int min = Integer.MAX_VALUE;
        DeliveryPerson closest = new DeliveryPerson();
        for (DeliveryPerson dp : deliveryPeople.values()) {
            if (dp.getCurrentLocation().distance(restLoc) < min) {
                closest = dp;
                min = dp.getCurrentLocation().distance(restLoc);
            } else if (dp.getCurrentLocation().distance(restLoc) == min) {
                if (dp.getNumOfOrders() < closest.getNumOfOrders()) {
                    closest = dp;
                    min = dp.getCurrentLocation().distance(restLoc);
                }
            }
        }
        return closest.getId();
    }

    public void orderFood(String userId, String userAddressName, String restaurantId, float cost) {
        Location restLoc = restaurants.get(restaurantId).getLocation();
        Location userLoc = users.get(userId).locationByAddress.get(userAddressName);
        String deliveryId = getClosestPerson(restLoc);
        //za deliveryPerson
        int distanceRestDelivery = restLoc.distance(deliveryPeople.get(deliveryId).getCurrentLocation());
        deliveryPeople.get(deliveryId).setLocation(userLoc);
        deliveryPeople.get(deliveryId).addMoney(distanceRestDelivery);
        deliveryPeople.get(deliveryId).addOrder();
        //za user
        users.get(userId).payOrder(cost);
        //za restoran
        restaurants.get(restaurantId).addOrder(cost);
    }

    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::getSpentMoney).thenComparing(User::getId).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants() {
        restaurants.values().stream()
                .sorted(Comparator.comparing(Restaurant::getAvgOrder).thenComparing(Restaurant::getID).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople() {
        deliveryPeople.values().stream()
                .sorted(Comparator.comparing(DeliveryPerson::getEarnedMoney).thenComparing(DeliveryPerson::getId).reversed())
                .forEach(System.out::println);
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}

