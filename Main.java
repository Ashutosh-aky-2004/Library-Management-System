
import menu.AdminMenu;
import menu.MemberMenu;
import model.Member;
import service.AuthenticationService;
import service.BookService;
import service.MemberService;
import service.TransactionService;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookService bookService = new BookService();
        MemberService memberService = new MemberService();
        TransactionService transactionService = new TransactionService();
        AuthenticationService authService = new AuthenticationService(memberService);

        System.out.println("==== Welcome to Library Tracker ====");
        System.out.println("Note: Ensure an admin is registered in members.txt or use 'admin/admin123' for first-time setup.");

        boolean exit = false;
        while (!exit) {
            System.out.println("\n1. Login (use username from members.txt or 'admin' for first-time setup)");
            System.out.println("2. Register as new member");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    var member = authService.login(username, password);

                    if (member != null) {
                        if (member.getRole().equalsIgnoreCase("ADMIN")) {
                            new AdminMenu(scanner, bookService, memberService).showMenu();
                        } else {
                            new MemberMenu(member.getMemberId(), scanner, bookService, transactionService).showMenu();
                        }
                    } else {
                        System.out.println("Invalid credentials! Please try again.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter member ID: ");
                    int id;
                    try {
                        id = scanner.nextInt();
                        scanner.nextLine();
                    } catch (java.util.InputMismatchException e) {
                        System.out.println("Invalid member ID! Must be a number.");
                        scanner.nextLine();
                        continue;
                    }
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    if (name.trim().isEmpty()) {
                        System.out.println("Name cannot be empty.");
                        continue;
                    }
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
                        System.out.println("Invalid email format.");
                        continue;
                    }
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    if (username.trim().isEmpty()) {
                        System.out.println("Username cannot be empty.");
                        continue;
                    }
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    if (password.trim().isEmpty()) {
                        System.out.println("Password cannot be empty.");
                        continue;
                    }
                    memberService.addMember(new Member(id, name, "MEMBER", username, password, email));
                }
                case 0 -> {
                    exit = true;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }
}