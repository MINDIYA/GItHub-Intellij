import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        BankAccount accountA = new BankAccount("1111", new BigDecimal(10000));
        BankAccount accountB = new BankAccount("2222", new BigDecimal(20000));
        BankAccount accountC = new BankAccount("3333", new BigDecimal(10000));

        Thread thread1 = new Thread(()->{
            try {
                accountA.transfer(accountB, new BigDecimal(2000), 1);
            } catch (InsufficientBalanceException e) {
                throw new RuntimeException(e);
            } catch (InvalidAmountException e) {
                throw new RuntimeException(e);
            }
        }, "Transfer 1");

        Thread thread2 = new Thread(()->{
            try {
                accountB.transfer(accountC, new BigDecimal(5000), 2);
            } catch (InsufficientBalanceException e) {
                throw new RuntimeException(e);
            } catch (InvalidAmountException e) {
                throw new RuntimeException(e);
            }
        }, "Transfer 2");

        Thread thread3 = new Thread(()->{
            try {
                accountC.transfer(accountA, new BigDecimal(5000), 3);
            } catch (InsufficientBalanceException e) {
                throw new RuntimeException(e);
            } catch (InvalidAmountException e) {
                throw new RuntimeException(e);
            }
        }, "Transfer 3");

        Thread thread4 = new Thread(()-> {
            System.out.println("After Transfer");
            System.out.println("Account A - Balance: " + accountA.getBalance());
            System.out.println("Account B - Balance: " + accountB.getBalance());
            System.out.println("Account C - Balance: " + accountC.getBalance());
        });

        thread1.start();
        thread2.start();
        thread3.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        thread4.start();

    }
}