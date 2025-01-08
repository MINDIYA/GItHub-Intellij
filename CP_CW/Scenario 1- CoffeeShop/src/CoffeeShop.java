import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// CoffeeShop Class: Shared Resource
class CoffeeShop {
    private final int capacity; // Maximum orders the queue can hold
    private final Queue<String> queue = new LinkedList<>(); // Order queue
    private final Lock lock = new ReentrantLock(); // Lock for mutual exclusion
    private final Condition full = lock.newCondition(); // Condition for full queue
    private final Condition empty = lock.newCondition(); // Condition for empty queue
    private int totalOrders; // Total number of orders to process
    private int processedOrders = 0; // Counter for processed orders

    public CoffeeShop(int capacity, int totalOrders) {
        this.capacity = capacity;
        this.totalOrders = totalOrders;
    }

    // Method for customers to place orders
    public void placeOrder(String order) {
        lock.lock();
        try {
            while (queue.size() >= capacity) {
                System.out.println(Thread.currentThread().getName() + " is waiting to place order: " + order);
                full.await(); // Wait if the queue is full
            }
            queue.add(order);
            System.out.println(Thread.currentThread().getName() + " placed order: " + order);
            empty.signalAll(); // Notify baristas that there are orders to prepare
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    // Method for baristas to prepare orders
    public String prepareOrder() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                if (processedOrders >= totalOrders) {
                    return null; // Stop if all orders have been processed
                }
                System.out.println(Thread.currentThread().getName() + " is waiting for an order...");
                empty.await(); // Wait if the queue is empty
            }
            String order = queue.poll();
            processedOrders++;
            System.out.println(Thread.currentThread().getName() + " prepared order: " + order);
            full.signalAll(); // Notify customers that space is available in the queue
            return order;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }


}