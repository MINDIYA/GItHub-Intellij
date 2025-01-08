import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class BathroomStall {
    public static final int MAX_OCCUPANCY = 6;
    public static final int MAX_USERS = 100;
    private static final Semaphore bathroomStalls = new Semaphore(MAX_OCCUPANCY);
    private static final Object stallLock = new Object();
    private static Queue<Integer> availableStalls = new LinkedList<>();

    public static void main(String[] args) {
        try {
            if (MAX_OCCUPANCY <= 0 || MAX_USERS <= 0) {
                throw new IllegalArgumentException("Number of stalls or users cannot be zero or negative.");
            }

            // Initialize the available stalls
            for (int i = 1; i <= MAX_OCCUPANCY; i++) {
                availableStalls.add(i);
            }

            // Create threads for each user
            for (int i = 1; i <= MAX_USERS; i++) {
                String userType = (i % 2 == 0) ? "Student" : "Employee";
                Thread user = new Thread(new BathroomUser(i, userType), userType + "-" + i);
                user.start();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Initialization Error: " + e.getMessage());
        }
    }

    public static class BathroomUser implements Runnable {
        private int id;
        private String userType;
        private int stallNumber;

        public BathroomUser(int id, String userType) {
            this.id = id;
            this.userType = userType;
        }

        @Override
        public void run() {
            try {
                // Wait for a stall to become available
                bathroomStalls.acquire();

                synchronized (stallLock) {
                    // Assign a stall number from the available stalls
                    stallNumber = availableStalls.poll();
                }

                if (stallNumber == 0) {
                    System.out.println(userType + " " + id + " could not find a stall.");
                    return;
                }

                System.out.println(userType + " " + id + " is using stall " + stallNumber);

                // Simulate stall usage
                useBathroomStall();

                System.out.println(userType + " " + id + " has finished using stall " + stallNumber);

                // Release the stall back to the pool
                synchronized (stallLock) {
                    availableStalls.add(stallNumber);
                }

            } catch (InterruptedException e) {
                System.out.println(userType + " " + id + " was interrupted: " + e.getMessage());
            } finally {
                bathroomStalls.release();
            }
        }

        public void useBathroomStall() {
            try {
                int usageTime = (int) (Math.random() * 2000) + 3000;
                Thread.sleep(usageTime); // Simulate time spent in the stall
            } catch (InterruptedException e) {
                System.out.println("Error during stall usage for " + userType + " " + id + ": " + e.getMessage());
            }
        }
    }
}
