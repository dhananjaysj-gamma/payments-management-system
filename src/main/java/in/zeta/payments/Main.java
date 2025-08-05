package in.zeta.payments;

import in.zeta.payments.dao.impl.AuditLogDAOImpl;
import in.zeta.payments.dao.impl.PaymentDAOImpl;
import in.zeta.payments.dao.impl.UserDAOImpl;
import in.zeta.payments.enums.PaymentStatus;
import in.zeta.payments.enums.PaymentType;
import in.zeta.payments.models.Payment;
import in.zeta.payments.models.User;
import in.zeta.payments.service.UserService;
import in.zeta.payments.service.impl.AuditLogServiceImpl;
import in.zeta.payments.service.impl.PaymentServiceImpl;
import in.zeta.payments.service.impl.ReportServiceImpl;
import in.zeta.payments.service.impl.UserServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl(new UserDAOImpl());
    private static final PaymentServiceImpl paymentService = new PaymentServiceImpl(new PaymentDAOImpl());
    private static final ReportServiceImpl reportService = new ReportServiceImpl(new PaymentDAOImpl());
    private static final AuditLogServiceImpl auditLogService = new AuditLogServiceImpl(new AuditLogDAOImpl());

    private static Integer loggedInUser;

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> addPayment();
                case 4 -> updatePaymentStatus();
                case 5 -> viewPayments();
                case 6 -> generateMonthlyReport();
                case 7 -> generateQuarterlyReport();
                case 8 -> viewAllAuditLogs();              // NEW
                case 9 -> viewAuditLogsByPaymentId();      // NEW
                case 10 -> exit = true;
                default -> System.out.println("Invalid option. Try again.");
            }
        }
        System.out.println("Exiting...");
    }

    private static void showMenu() {
        System.out.println("\n=== Payments Management System ===");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Add Payment");
        System.out.println("4. Update payment Status");
        System.out.println("5. View My Payments");
        System.out.println("6. Generate Monthly Report");
        System.out.println("7. Generate Quarterly Report");
        System.out.println("8. View All Audit Logs");  // NEW
        System.out.println("9. View Logs by Payment ID");  // NEW
        System.out.println("10. Exit");
        System.out.print("Choose option: ");
    }

    private static void registerUser() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        userService.registerUser(user);
        System.out.println("Registration successful.");
    }

    private static void loginUser() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            loggedInUser = userService.login(email, password);
            System.out.println("Login successful. Welcome!");
        } catch (RuntimeException e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private static void addPayment() {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter payment type (INCOMING / OUTGOING): ");
        String type = scanner.nextLine();

        System.out.print("Enter payment status (PENDING / COMPLETED / FAILED): ");
        String status = scanner.nextLine();

        System.out.println("Enter payment date (yyyy-mm-dd): ");
        String date = scanner.nextLine();

        System.out.println("Available Categories:");
        System.out.println("1. Salary Disbursements");
        System.out.println("2. Vendor AP Payments");
        System.out.println("3. Client Invoice");

        System.out.print("Enter category name (e.g., Salary Disbursements): ");
        String categoryName = scanner.nextLine().trim();

        int categoryId = getCategoryIdByName(categoryName);
        if (categoryId == -1) {
            System.out.println("Invalid category name. Aborting.");
            return;
        }

        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setPaymentType(PaymentType.valueOf(type.toUpperCase()));
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        payment.setPaymentDate(LocalDate.parse(date));
        payment.setCategoryID(categoryId);
        payment.setCreatedBy(loggedInUser);
        payment.setDescription(description);

        paymentService.addPayment(payment);
        System.out.println("Payment added successfully.");
    }

    private static void updatePaymentStatus(){
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }
        System.out.print("Enter Payment ID: ");
        int paymentID = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Payment status: ");
        String status = scanner.nextLine();

        paymentService.updatePaymentStatus(paymentID,status);
        System.out.println("Status updated successfully");
    }

    private static void viewPayments() {
        if (loggedInUser == null) {
            System.out.println("Please login first.");
            return;
        }

        System.out.print("Enter User ID: ");
        int userID = Integer.parseInt(scanner.nextLine());

        List<Payment> payments = paymentService.getAllPaymentsByUser(userID);
        if (payments.isEmpty()) {
            System.out.println("No payments found.");
        } else {
            System.out.println("Your Payments:");
            for (Payment p : payments) {
                System.out.println("- " + p.getPaymentId() + " | ₹" + p.getAmount() + " | " + p.getPaymentType() + " | " + p.getStatus() + " | " + p.getDescription());
            }
        }
    }

    private static void generateMonthlyReport() {
        System.out.print("Enter year (e.g. 2025): ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        reportService.generateMonthlyReport(year, month);
    }

    private static void generateQuarterlyReport() {
        System.out.print("Enter year (e.g. 2025): ");
        int year = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter quarter (1-4): ");
        int quarter = Integer.parseInt(scanner.nextLine());

        reportService.generateQuarterlyReport(year, quarter);
    }

    private static void viewAllAuditLogs() {
        auditLogService.getAllLogs();
    }

    private static void viewAuditLogsByPaymentId() {
        System.out.print("Enter Payment ID to view logs: ");
        int paymentId = Integer.parseInt(scanner.nextLine());
        auditLogService.getLogsByPaymentID(paymentId);
    }
    private static int getCategoryIdByName(String name) {
        switch (name.toLowerCase()) {
            case "salary disbursements":
                return 1;
            case "vendor payments":
                return 2;
            case "client invoice":
                return 3;
            default:
                return -1;
        }
    }
}
