import java.util.concurrent.Semaphore;

public class App {
    public static void main(String[] args)throws InterruptedException {
        Semaphore semaphore = new Semaphore(6);
        semaphore.acquire();
        System.out.println("Semaphore available:"+semaphore.availablePermits());
        semaphore.acquire(3);
        System.out.println("Semaphore available:"+semaphore.availablePermits());
        //the below statement does not execute succesfully as the available permits are 2
        //semaphore.acquire(3);
       //System.out.println("Semaphore available:"+semaphore.availablePermits());

        semaphore.release();
        System.out.println("Semaphore available:"+semaphore.availablePermits());
        semaphore.acquire(4);
        System.out.println("Semaphore available:"+semaphore.availablePermits());
    }
}