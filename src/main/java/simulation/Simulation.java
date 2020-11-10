package simulation;

public class Simulation {
    public static void main(String[] args) {
        new Thread(new Seller1()).start();
        new Thread(new Buyer1()).start();
        new Thread(new Buyer2()).start();
        new Thread(new Seller2()).start();
    }
}
