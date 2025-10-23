import java.util.*;

class Customer {
    private int customerID;
    private String name;
    private String address;
    private String phone;
    private ArrayList<BankAccount> accounts;

    public Customer(int customerID, String name, String address, String phone) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.accounts = new ArrayList<>();
    }

    public int getCustomerID() { return customerID; }
    public String getName() { return name; }
    public ArrayList<BankAccount> getAccounts() { return accounts; }
    public void addAccount(BankAccount account) { accounts.add(account); }

    public void displayCustomerDetails() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone);
    }
}

abstract class BankAccount {
    protected String accountNumber;
    protected Customer customer;
    protected double balance;

    public BankAccount(String accountNumber, Customer customer, double balance) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
    }

    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public Customer getCustomer() { return customer; }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: " + balance);
    }

    public abstract void withdraw(double amount);

    public void displayAccountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Balance: " + balance);
    }
}

class SavingsAccount extends BankAccount {
    private double interestRate;
    private double minimumBalance;

    public SavingsAccount(String accountNumber, Customer customer, double balance, double interestRate, double minimumBalance) {
        super(accountNumber, customer, balance);
        this.interestRate = interestRate;
        this.minimumBalance = minimumBalance;
    }

    public void addInterest() {
        balance += balance * interestRate / 100;
        System.out.println("Interest added. New balance: " + balance);
    }

    public void withdraw(double amount) {
        if (balance - amount < minimumBalance) {
            System.out.println("Withdrawal denied. Minimum balance required.");
        } else {
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: " + balance);
        }
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, Customer customer, double balance, double overdraftLimit) {
        super(accountNumber, customer, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public void withdraw(double amount) {
        if (balance + overdraftLimit >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: " + balance);
        } else {
            System.out.println("Withdrawal denied. Overdraft limit exceeded.");
        }
    }
}

abstract class Transaction {
    protected String transactionID;
    protected BankAccount account;
    protected double amount;
    protected Date date;

    public Transaction(String transactionID, BankAccount account, double amount) {
        this.transactionID = transactionID;
        this.account = account;
        this.amount = amount;
        this.date = new Date();
    }

    public abstract void execute();
}

class DepositTransaction extends Transaction {
    public DepositTransaction(String transactionID, BankAccount account, double amount) {
        super(transactionID, account, amount);
    }

    public void execute() {
        account.deposit(amount);
        System.out.println("Deposit Transaction ID: " + transactionID + " completed on " + date);
    }
}

class WithdrawalTransaction extends Transaction {
    public WithdrawalTransaction(String transactionID, BankAccount account, double amount) {
        super(transactionID, account, amount);
    }

    public void execute() {
        account.withdraw(amount);
        System.out.println("Withdrawal Transaction ID: " + transactionID + " completed on " + date);
    }
}

class Loan {
    private String loanID;
    private Customer customer;
    private double loanAmount;
    private double interestRate;
    private boolean approved;

    public Loan(String loanID, Customer customer, double loanAmount, double interestRate) {
        this.loanID = loanID;
        this.customer = customer;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.approved = false;
    }

    public void checkEligibility() {
        double totalBalance = 0;
        for (BankAccount acc : customer.getAccounts()) {
            totalBalance += acc.getBalance();
        }
        if (totalBalance >= 5000) {
            approved = true;
            System.out.println("Loan approved for " + customer.getName());
        } else {
            approved = false;
            System.out.println("Loan rejected for " + customer.getName());
        }
    }

    public void displayLoanStatus() {
        System.out.println("Loan ID: " + loanID + " | Customer: " + customer.getName() + " | Approved: " + approved);
    }
}

public class BankingManagementSystem {
    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Customer> customers = new ArrayList<>();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Banking Management System ---");
            System.out.println("1. Add Customer");
            System.out.println("2. Open Account");
            System.out.println("3. Deposit Money");
            System.out.println("4. Withdraw Money");
            System.out.println("5. Apply Loan");
            System.out.println("6. Display Account Details");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> addCustomer();
                case 2 -> openAccount();
                case 3 -> depositMoney();
                case 4 -> withdrawMoney();
                case 5 -> applyLoan();
                case 6 -> displayAccountDetails();
                case 7 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice!");
            }
        } while (choice != 7);
    }

    private static void addCustomer() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Address: ");
        String address = sc.nextLine();
        System.out.print("Enter Phone: ");
        String phone = sc.nextLine();
        customers.add(new Customer(id, name, address, phone));
        System.out.println("Customer added successfully!");
    }

    private static Customer findCustomer(int id) {
        for (Customer c : customers) {
            if (c.getCustomerID() == id) return c;
        }
        return null;
    }

    private static void openAccount() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        Customer customer = findCustomer(id);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        System.out.println("1. Savings Account\n2. Current Account");
        int type = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter Account Number: ");
        String accNum = sc.nextLine();
        System.out.print("Enter Initial Balance: ");
        double balance = sc.nextDouble();

        BankAccount account;
        if (type == 1) {
            account = new SavingsAccount(accNum, customer, balance, 3.5, 1000);
        } else {
            account = new CurrentAccount(accNum, customer, balance, 5000);
        }
        customer.addAccount(account);
        System.out.println("Account opened successfully!");
    }

    private static void depositMoney() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        Customer customer = findCustomer(id);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        System.out.print("Enter Account Number: ");
        String accNum = sc.next();
        BankAccount account = null;
        for (BankAccount a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(accNum)) account = a;
        }
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }
        System.out.print("Enter Deposit Amount: ");
        double amount = sc.nextDouble();
        new DepositTransaction(UUID.randomUUID().toString(), account, amount).execute();
    }

    private static void withdrawMoney() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        Customer customer = findCustomer(id);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        System.out.print("Enter Account Number: ");
        String accNum = sc.next();
        BankAccount account = null;
        for (BankAccount a : customer.getAccounts()) {
            if (a.getAccountNumber().equals(accNum)) account = a;
        }
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }
        System.out.print("Enter Withdrawal Amount: ");
        double amount = sc.nextDouble();
        new WithdrawalTransaction(UUID.randomUUID().toString(), account, amount).execute();
    }

    private static void applyLoan() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        Customer customer = findCustomer(id);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        System.out.print("Enter Loan Amount: ");
        double amount = sc.nextDouble();
        Loan loan = new Loan(UUID.randomUUID().toString(), customer, amount, 7.5);
        loan.checkEligibility();
        loan.displayLoanStatus();
    }

    private static void displayAccountDetails() {
        System.out.print("Enter Customer ID: ");
        int id = sc.nextInt();
        Customer customer = findCustomer(id);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }
        customer.displayCustomerDetails();
        for (BankAccount acc : customer.getAccounts()) {
            acc.displayAccountDetails();
        }
    }
}
