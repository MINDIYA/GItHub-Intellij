// Customer Class: Implements Runnable
class Customer implements Runnable {
    private final CoffeeShop coffeeShop;
    private final String order;

    public Customer(CoffeeShop coffeeShop, String order) {
        this.coffeeShop = coffeeShop;
        this.order = order;
    }

    @Override
    public void run() {
        coffeeShop.placeOrder(order); // Place an order in the queue
    }
}