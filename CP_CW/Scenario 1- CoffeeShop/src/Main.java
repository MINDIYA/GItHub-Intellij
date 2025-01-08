// Main Class: Entry Point
public class Main {
    public static void main(String[] args) {
        int totalOrders = 5; // Total number of orders to process
        CoffeeShop coffeeShop = new CoffeeShop(3, totalOrders); // Queue can hold 3 orders at a time

        // Create Barista threads
        Thread barista1 = new Thread(new Barista(coffeeShop), "Barista 1");
        Thread barista2 = new Thread(new Barista(coffeeShop), "Barista 2");
        Thread barista3 = new Thread(new Barista(coffeeShop), "Barista 3");
        Thread barista4 = new Thread(new Barista(coffeeShop), "Barista 4");
        Thread barista5 = new Thread(new Barista(coffeeShop), "Barista 5");

        // Create Customer threads
        Thread customer1 = new Thread(new Customer(coffeeShop, "Order 1"), "Customer 1");
        Thread customer2 = new Thread(new Customer(coffeeShop, "Order 2"), "Customer 2");
        Thread customer3 = new Thread(new Customer(coffeeShop, "Order 3"), "Customer 3");
        Thread customer4 = new Thread(new Customer(coffeeShop, "Order 4"), "Customer 4");
        Thread customer5 = new Thread(new Customer(coffeeShop, "Order 5"), "Customer 5");

        // Start all threads
        barista1.start();
        barista2.start();
        barista3.start();
        barista4.start();
        barista5.start();
        customer1.start();
        customer2.start();
        customer3.start();
        customer4.start();
        customer5.start();

        // Wait for all threads to complete
        try {
            barista1.join();
            barista2.join();
            barista3.join();
            barista4.join();
            barista5.join();
            customer1.join();
            customer2.join();
            customer3.join();
            customer4.join();
            customer5.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All orders have been processed. Coffee shop is closing.");
    }
}