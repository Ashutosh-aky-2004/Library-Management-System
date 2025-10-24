package menu;

import model.Book;
import model.Member;
import service.BookService;
import service.MemberService;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final Scanner scanner;
    private final BookService bookService;
    private final MemberService memberService;

    public AdminMenu(Scanner scanner, BookService bookService, MemberService memberService) {
        this.scanner = scanner;
        this.bookService = bookService;
        this.memberService = memberService;
    }

    public void showMenu() {
        int choice = 0; // Initialize to fix uninitialized variable error
        do {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. Add Book");
            System.out.println("2. View All Books");
            System.out.println("3. Remove Book");
            System.out.println("4. Add Member");
            System.out.println("5. View All Members");
            System.out.println("6. Remove Member");
            System.out.println("7. Search Books");
            System.out.println("0. Logout");
            System.out.print("Enter your choice: ");

            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1 -> addBook();
                case 2 -> viewAllBooks();
                case 3 -> removeBook();
                case 4 -> addMember();
                case 5 -> viewAllMembers();
                case 6 -> removeMember();
                case 7 -> searchBooks();
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private void addBook() {
        System.out.print("Enter book ID: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid book ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        if (title.trim().isEmpty()) {
            System.out.println("Title cannot be empty.");
            return;
        }
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        if (author.trim().isEmpty()) {
            System.out.println("Author cannot be empty.");
            return;
        }

        Book book = new Book(id, title, author);
        bookService.addBook(book);
    }

    private void viewAllBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            System.out.println("\n--- Book List ---");
            for (Book b : books) {
                System.out.println("ID: " + b.getBookId() +
                        ", Title: " + b.getTitle() +
                        ", Author: " + b.getAuthor() +
                        ", Available: " + b.isAvailable());
            }
        }
    }

    private void removeBook() {
        System.out.print("Enter book ID to remove: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid book ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        bookService.removeBook(id);
    }

    private void addMember() {
        System.out.print("Enter member ID: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid member ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        if (name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        if (!java.util.regex.Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            System.out.println("Invalid email format.");
            return;
        }
        System.out.print("Enter member username: ");
        String username = scanner.nextLine();
        if (username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return;
        }
        System.out.print("Enter member password: ");
        String password = scanner.nextLine();
        if (password.trim().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        Member member = new Member(id, name, "MEMBER", username, password, email);
        memberService.addMember(member);
    }

    private void viewAllMembers() {
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            System.out.println("\n--- Member List ---");
            for (Member m : members) {
                System.out.println("ID: " + m.getMemberId() +
                        ", Name: " + m.getName() +
                        ", Email: " + m.getEmail() +
                        ", Role: " + m.getRole());
            }
        }
    }

    private void removeMember() {
        System.out.print("Enter member ID to remove: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid member ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        memberService.removeMember(id);
    }

    private void searchBooks() {
        System.out.print("Enter search keyword (title or author): ");
        String keyword = scanner.nextLine();
        List<Book> books = bookService.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("No books found matching '" + keyword + "'.");
        } else {
            System.out.println("\n--- Search Results ---");
            for (Book b : books) {
                System.out.println("ID: " + b.getBookId() +
                        ", Title: " + b.getTitle() +
                        ", Author: " + b.getAuthor() +
                        ", Available: " + b.isAvailable());
            }
        }
    }
}