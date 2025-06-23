import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        AccountService service = new AccountService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bank Account Management ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. View Transactions");
            System.out.println("6. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> service.createAccount();
                case 2 -> service.deposit();
                case 3 -> service.withdraw();
                case 4 -> service.checkBalance();
                case 5 -> service.viewTransactions();
                case 6 -> System.exit(0);
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}