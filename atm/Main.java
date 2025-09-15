import java.util.*;
class Transaction {
    private String type;
    private double amount;
    private Date date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return date + " | " + type + " | " + amount;
    }
}

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<Transaction> history;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.history = new ArrayList<>();
    }

    public String getUserId() { return userId; }
    public boolean checkPin(String pin) { return this.pin.equals(pin); }
    public double getBalance() { return balance; }

    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("Deposit", amount));
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        history.add(new Transaction("Withdraw", amount));
        return true;
    }

    public void addTransaction(String type, double amount) {
        history.add(new Transaction(type, amount));
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public void transferTo(User recipient, double amount) {
        if (amount <= balance) {
            withdraw(amount);
            recipient.deposit(amount);
            addTransaction("Transfer to " + recipient.getUserId(), amount);
        }
    }
}

class Bank {
    private List<User> users;

    public Bank() {
        users = new ArrayList<>();
        users.add(new User("user1", "1234", 1000));
        users.add(new User("user2", "5678", 2000));
    }

    public User login(String userId, String pin) {
        for (User u : users) {
            if (u.getUserId().equals(userId) && u.checkPin(pin)) {
                return u;
            }
        }
        return null;
    }

    public User getUserById(String userId) {
        for (User u : users) {
            if (u.getUserId().equals(userId)) return u;
        }
        return null;
    }
}

class ATMOperations {
    private Scanner sc = new Scanner(System.in);

    public void start(Bank bank) {
        System.out.print("Enter User ID: ");
        String id = sc.next();
        System.out.print("Enter PIN: ");
        String pin = sc.next();

        User user = bank.login(id, pin);
        if (user == null) {
            System.out.println("Invalid login!");
            return;
        }
        System.out.println("Login successful!");

        while (true) {
            System.out.println("\n1. Transaction History\n2. Withdraw\n3. Deposit\n4. Transfer\n5. Quit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    showHistory(user);
                    break;
                case 2:
                    withdraw(user);
                    break;
                case 3:
                    deposit(user);
                    break;
                case 4:
                    transfer(user, bank);
                    break;
                case 5:
                    System.out.println("Thank you for using ATM!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void showHistory(User user) {
        for (Transaction t : user.getHistory()) {
            System.out.println(t);
        }
    }

    private void withdraw(User user) {
        System.out.print("Enter amount to withdraw: ");
        double amt = sc.nextDouble();
        if (user.withdraw(amt)) {
            System.out.println("Withdrawal successful! Remaining balance: " + user.getBalance());
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    private void deposit(User user) {
        System.out.print("Enter amount to deposit: ");
        double amt = sc.nextDouble();
        user.deposit(amt);
        System.out.println("Deposit successful! Balance: " + user.getBalance());
    }

    private void transfer(User user, Bank bank) {
        System.out.print("Enter recipient User ID: ");
        String recId = sc.next();
        User recipient = bank.getUserById(recId);
        if (recipient == null) {
            System.out.println("Recipient not found!");
            return;
        }
        System.out.print("Enter amount: ");
        double amt = sc.nextDouble();
        if (amt <= user.getBalance()) {
            user.transferTo(recipient, amt);
            System.out.println("Transfer successful! Balance: " + user.getBalance());
        } else {
            System.out.println("Insufficient funds!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        ATMOperations atm = new ATMOperations();
        atm.start(bank);
    }
}
