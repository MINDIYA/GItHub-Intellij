import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


// ideally, should add a transaction class
public class BankAccount {
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);   // This is to insure the fairness
    // When threads are trying to access it will be letting on First come first serve basis

    private Lock readLock = rwLock.readLock();
    private Lock writeLock = rwLock.writeLock();
    private List<String> transactions = new ArrayList<>();


    private String accountNumber;
    private BigDecimal balance;


    public BankAccount(String accountNumber, BigDecimal initialBalance) {
        this.balance = initialBalance;
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        readLock.lock();
        try {
            return balance;
        } finally {
            readLock.unlock();
        }
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public List<String> getTransactionsHistory() {
        readLock.lock();
        try {
            return this.transactions;
        } finally {
            readLock.unlock();
        }
    }

    public boolean transfer(BankAccount toAccount, BigDecimal amount, int transactionId)
            throws InsufficientBalanceException, InvalidAmountException {
        BankAccount firstLock, secondLock;

        // Determine locking order to prevent deadlocks
        if (this.getAccountNumber().hashCode() > toAccount.getAccountNumber().hashCode()) {
            firstLock = this;
            secondLock = toAccount;
        } else {
            firstLock = toAccount;
            secondLock = this;
        }

        firstLock.writeLock.lock();
        try {
            secondLock.writeLock.lock();
            try {
                // Perform transfer
                this.withdraw(amount, transactionId);
                toAccount.deposit(amount, transactionId);
                return true;
            } finally {
                secondLock.writeLock.unlock();
            }
        } finally {
            firstLock.writeLock.unlock();
        }
    }

//    public boolean transfer(BankAccount toAccount, BigDecimal amount, int transactionId )
//            throws InsufficientBalanceException, InvalidAmountException {
//        BankAccount firstLock, secondLock;
//        boolean success = false;
//        BigDecimal beforeAmountFromAccount = this.getBalance();
//        BigDecimal beforeAmountToAccount = toAccount.getBalance();
//        BigDecimal afterAmountFromAccount = null;
//        BigDecimal afterAmountToAccount = null;
//
//        //"this" is the current account object from which we transfer the amount to toAccount
//
//        // Purpose of this logic is to acquire the firstLock on the account with the largest accountNo
//        // And the secondLock is to the account with the smallest accountNo
//        // By doing so we can avoid circular deadlock
//        // Check for the largest accountNo to avoid the deadlock
//        if (this.getAccountNumber().hashCode() > toAccount.getAccountNumber().hashCode()) {
//            firstLock = this;
//            secondLock = toAccount;
//        }
//        else {
//            firstLock = toAccount;
//            secondLock = this;
//        }
//
//        firstLock.writeLock.lock();
//        try {
//            secondLock.writeLock.lock();
//            try {
//                this.withdraw(amount, transactionId);   // Get the money from "this" account to transfer to the toAccount
//                toAccount.deposit(amount,transactionId);    // Deposit the "this" account money to the toAccount
//                success = true; // if no error transaction is success
//            }
//            catch (Exception e) {
//                success = false;
//                throw new RuntimeException(e); // if any error occur
//            }
//            finally {
//
//                // get the after a
//                afterAmountFromAccount = this.getBalance();
//                afterAmountToAccount = toAccount.getBalance();
//
//
//                secondLock.writeLock.unlock();
//            }
//        } finally {
//            firstLock.writeLock.unlock();
//        }
//
//        if (!success) {
//            firstLock.writeLock.lock();
//            try {
//                secondLock.writeLock.lock();
//                try {
//                    // Reversing if any error occur
//                    if (beforeAmountFromAccount.doubleValue() != afterAmountFromAccount.doubleValue() + amount.doubleValue()) {
//                        this.deposit(amount,transactionId);
//                        // this.transactions TODO: remove the transaction with a proper message
//                    }
//                    if (beforeAmountToAccount.doubleValue() != afterAmountToAccount.doubleValue() - amount.doubleValue())  {
//                        this.withdraw(amount,transactionId);
//                        // this.transactions TODO: remove the transaction with a proper message
//                    }
//                } finally {
//                    secondLock.writeLock.unlock();
//                }
//            } finally {
//                firstLock.writeLock.unlock();
//            }
//
//        }
//        return success;
//    }

    public  void withdraw(BigDecimal amount, int transactionId) throws InsufficientBalanceException {
        // whoever calls this method should handle the insufficient balance

        // in the future we will be adding the Transaction(id, amount, account)
        writeLock.lock();
        try {
            if (amount.doubleValue() > 0 && getBalance().doubleValue() >= amount.doubleValue()) {
                this.balance = getBalance().subtract(amount);
                transactions.add(Thread.currentThread().getName() + " withdraw " + transactionId + " Amount " + amount);
                // seperate method for add transaction history
            } else {
                throw new InsufficientBalanceException("Insufficient funds in the account");
            }
        } finally {
            writeLock.unlock();
        }
    }

    public void deposit(BigDecimal amount, int transactionId) throws InvalidAmountException {
        writeLock.lock();
        try {
            if(amount.doubleValue() > 0) {
                this.balance = this.balance.add(amount);
                transactions.add(Thread.currentThread().getName() + " deposit " + transactionId + " Amount " + amount);
            }
            else {
                throw new InvalidAmountException("The amount cannot be less than 0");
            }
        } finally {
            writeLock.unlock();
        }
    }


}
