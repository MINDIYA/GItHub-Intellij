// Barista Class: Implements Runnable
class Barista implements Runnable {
    private final CoffeeShop coffeeShop;

    public Barista(CoffeeShop coffeeShop) {
        this.coffeeShop = coffeeShop;
    }

    @Override
    public void run() {
        while (true) { // Continuously prepare orders
            String order = coffeeShop.prepareOrder();
            if (order == null) {
                break; // Exit the loop when all orders are processed
            }
            // Simulate order preparation time
            try {
                Thread.sleep(2000); // Barista takes 2 seconds to prepare each order
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(Thread.currentThread().getName() + " has finished processing all orders.");
    }
}