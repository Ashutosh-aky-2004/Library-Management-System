package menu;

import model.Book;
import model.Transaction;
import service.BookService;
import service.TransactionService;

import java.util.List;
import java.util.Scanner;

public class MemberMenu {
    private final Scanner scanner;
    private final BookService bookService;
    private final TransactionService transactionService;
    private final int memberId;

    public MemberMenu(int memberId, Scanner scanner, BookService bookService, TransactionService transactionService) {
        this.memberId = memberId;
        this.scanner = scanner;
        this.bookService = bookService;
        this.transactionService = transactionService;
    }

    public void showMenu() {
        int choice = 0;
        do {
            System.out.println("\n==== Member Menu ====");
            System.out.println("1. View All Books");
            System.out.println("2. Borrow Book");
            System.out.println("3. Return Book");
            System.out.println("4. View My Transactions");
            System.out.println("5. Search Books");
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
                case 1 -> viewAllBooks();
                case 2 -> borrowBook();
                case 3 -> returnBook();
                case 4 -> viewMyTransactions();
                case 5 -> searchBooks();
                case 0 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
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

    private void borrowBook() {
        System.out.print("Enter book ID to borrow: ");
        int bookId;
        try {
            bookId = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid book ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        transactionService.borrowBook(memberId, bookId);
    }

    private void returnBook() {
        System.out.print("Enter book ID to return: ");
        int bookId;
        try {
            bookId = scanner.nextInt();
            scanner.nextLine();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Invalid book ID! Must be a number.");
            scanner.nextLine();
            return;
        }
        transactionService.returnBook(memberId, bookId);
    }

    private void viewMyTransactions() {
        List<Transaction> transactions = transactionService.getMemberTransactions(memberId);
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.println("\n--- My Transactions ---");
            for (Transaction t : transactions) {
                System.out.println("Book: " + t.getBookTitle() +
                        ", Borrow Date: " + t.getBorrowDate() +
                        ", Return Date: " + (t.getReturnDate() != null ? t.getReturnDate() : "Not Returned"));
            }
        }
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