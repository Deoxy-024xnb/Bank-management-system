import java.sql.*;
import java.util.Scanner;

public class AccountService {
    Scanner sc = new Scanner(System.in);

    public void createAccount() throws Exception {
        System.out.print("Enter account holder name: ");
        String name = sc.nextLine();

        String sql = "INSERT INTO accounts (name) VALUES (?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Account created successfully!");
        }
    }

    public void deposit() throws Exception {
        System.out.print("Enter account ID: ");
        int id = sc.nextInt();
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            String updateBalance = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            String insertTxn = "INSERT INTO transactions(account_id, amount, type) VALUES (?, ?, 'DEPOSIT')";

            try (PreparedStatement stmt1 = con.prepareStatement(updateBalance);
                 PreparedStatement stmt2 = con.prepareStatement(insertTxn)) {

                stmt1.setDouble(1, amount);
                stmt1.setInt(2, id);
                stmt1.executeUpdate();

                stmt2.setInt(1, id);
                stmt2.setDouble(2, amount);
                stmt2.executeUpdate();

                con.commit();
                System.out.println("Deposit successful.");
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void withdraw() throws Exception {
        System.out.print("Enter account ID: ");
        int id = sc.nextInt();
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);

            String checkBalance = "SELECT balance FROM accounts WHERE account_id = ?";
            String updateBalance = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            String insertTxn = "INSERT INTO transactions(account_id, amount, type) VALUES (?, ?, 'WITHDRAW')";

            try (PreparedStatement checkStmt = con.prepareStatement(checkBalance)) {
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getDouble("balance") >= amount) {
                    try (PreparedStatement stmt1 = con.prepareStatement(updateBalance);
                         PreparedStatement stmt2 = con.prepareStatement(insertTxn)) {

                        stmt1.setDouble(1, amount);
                        stmt1.setInt(2, id);
                        stmt1.executeUpdate();

                        stmt2.setInt(1, id);
                        stmt2.setDouble(2, amount);
                        stmt2.executeUpdate();

                        con.commit();
                        System.out.println("Withdrawal successful.");
                    }
                } else {
                    System.out.println("Insufficient balance.");
                }
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    public void checkBalance() throws Exception {
        System.out.print("Enter account ID: ");
        int id = sc.nextInt();

        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Balance: ₹" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found.");
            }
        }
    }

    public void viewTransactions() throws Exception {
        System.out.print("Enter account ID: ");
        int id = sc.nextInt();

        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.printf("%s | ₹%.2f | %s\n", rs.getTimestamp("timestamp"), rs.getDouble("amount"), rs.getString("type"));
            }
        }
    }
}