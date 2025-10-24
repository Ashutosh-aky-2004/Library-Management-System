package service;

import model.Book;
import model.Transaction;
import java.io.*;
import java.util.*;

public class BookService {
    private static final String BOOKS_FILE = "data/books.txt";
    private final TransactionService transactionService;

    public BookService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public BookService() {
        this.transactionService = new TransactionService();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line);
                if (parts.length == 4) {
                    int bookId = Integer.parseInt(parts[0]);
                    String title = parts[1].replace("\\,", ",");
                    String author = parts[2].replace("\\,", ",");
                    boolean available = Boolean.parseBoolean(parts[3]);
                    books.add(new Book(bookId, title, author, available));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books file: " + e.getMessage());
        }
        return books;
    }

    public void addBook(Book book) {
        List<Book> books = getAllBooks();
        for (Book b : books) {
            if (b.getBookId() == book.getBookId()) {
                System.out.println("Book ID " + book.getBookId() + " already exists.");
                return;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE, true))) {
            bw.write(book.toCSV());
            bw.newLine();
            System.out.println("Book added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to books file: " + e.getMessage());
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> allBooks = getAllBooks();
        List<Book> result = new ArrayList<>();
        keyword = keyword.toLowerCase();
        for (Book b : allBooks) {
            if (b.getTitle().toLowerCase().contains(keyword) ||
                    b.getAuthor().toLowerCase().contains(keyword)) {
                result.add(b);
            }
        }
        return result;
    }

    public void updateBookAvailability(int bookId, boolean available) {
        List<Book> books = getAllBooks();
        boolean updated = false;

        for (Book b : books) {
            if (b.getBookId() == bookId) {
                b.setAvailable(available);
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
                for (Book b : books) {
                    bw.write(b.toCSV());
                    bw.newLine();
                }
                System.out.println("Book availability updated.");
            } catch (IOException e) {
                System.out.println("Error updating books file: " + e.getMessage());
            }
        } else {
            System.out.println("Book ID not found.");
        }
    }

    public void removeBook(int bookId) {
        for (Transaction t : transactionService.getAllTransactions()) {
            if (t.getBookId() == bookId && t.getReturnDate().equals("null")) {
                System.out.println("Cannot remove book: It is currently borrowed.");
                return;
            }
        }
        List<Book> books = getAllBooks();
        boolean removed = books.removeIf(b -> b.getBookId() == bookId);

        if (removed) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
                for (Book b : books) {
                    bw.write(b.toCSV());
                    bw.newLine();
                }
                System.out.println("Book removed successfully.");
            } catch (IOException e) {
                System.out.println("Error updating books file: " + e.getMessage());
            }
        } else {
            System.out.println("Book ID not found.");
        }
    }

    private String[] splitCSV(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean escaped = false;
        for (char c : line.toCharArray()) {
            if (c == ',' && !escaped) {
                parts.add(field.toString());
                field = new StringBuilder();
            } else if (c == '\\' && !escaped) {
                escaped = true;
            } else {
                field.append(c);
                escaped = false;
            }
        }
        parts.add(field.toString());
        return parts.toArray(new String[0]);
    }
}