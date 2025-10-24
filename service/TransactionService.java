package service;

import model.Book;
import model.Transaction;
import java.io.*;
import java.util.*;
import java.nio.channels.FileLock;
import java.nio.channels.FileChannel;

public class TransactionService {
    private static final String TRANSACTIONS_FILE = "data/transactions.txt";
    private static final String ID_COUNTER_FILE = "data/transaction_id_counter.txt";
    private final BookService bookService;

    public TransactionService(BookService bookService) {
        this.bookService = bookService;
    }

    public TransactionService() {
        this.bookService = new BookService(this);
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line);
                if (parts.length == 5) {
                    int transactionId = Integer.parseInt(parts[0]);
                    int bookId = Integer.parseInt(parts[1]);
                    int memberId = Integer.parseInt(parts[2]);
                    String issueDate = parts[3];
                    String returnDate = parts[4];
                    transactions.add(new Transaction(transactionId, bookId, memberId, issueDate, returnDate));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions file: " + e.getMessage());
        }
        return transactions;
    }

    private int getNextTransactionId() {
        int id = 1;
        try (RandomAccessFile file = new RandomAccessFile(ID_COUNTER_FILE, "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {
            String line = file.readLine();
            if (line != null && !line.trim().isEmpty()) {
                id = Integer.parseInt(line.trim()) + 1;
            }
            file.seek(0);
            file.writeBytes(String.valueOf(id));
            file.setLength(String.valueOf(id).length()); // Fix: Use setLength instead of truncate
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error updating transaction ID counter: " + e.getMessage());
        }
        return id;
    }

    public void addTransaction(Transaction transaction) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            bw.write(transaction.toCSV());
            bw.newLine();
            System.out.println("Transaction recorded successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to transactions file: " + e.getMessage());
        }
    }

    public void updateReturnDate(int transactionId, String returnDate) {
        List<Transaction> transactions = getAllTransactions();
        boolean updated = false;

        for (Transaction t : transactions) {
            if (t.getTransactionId() == transactionId && t.getReturnDate().equals("null")) {
                t.setReturnDate(returnDate);
                updated = true;
                break;
            }
        }

        if (updated) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE))) {
                for (Transaction t : transactions) {
                    bw.write(t.toCSV());
                    bw.newLine();
                }
                System.out.println("Return date updated successfully.");
            } catch (IOException e) {
                System.out.println("Error updating transactions file: " + e.getMessage());
            }
        } else {
            System.out.println("Transaction not found or already returned.");
        }
    }

    public List<Transaction> getMemberTransactions(int memberId) {
        List<Transaction> result = new ArrayList<>();
        Map<Integer, String> bookTitles = new HashMap<>();
        for (Book b : bookService.getAllBooks()) {
            bookTitles.put(b.getBookId(), b.getTitle());
        }
        for (Transaction t : getAllTransactions()) {
            if (t.getMemberId() == memberId) {
                t.setBookTitle(bookTitles.getOrDefault(t.getBookId(), "Unknown Book"));
                result.add(t);
            }
        }
        return result;
    }

    public void borrowBook(int memberId, int bookId) {
        List<Book> books = bookService.getAllBooks();
        boolean bookExists = false;
        for (Book b : books) {
            if (b.getBookId() == bookId) {
                if (!b.isAvailable()) {
                    System.out.println("Book is not available.");
                    return;
                }
                bookExists = true;
                break;
            }
        }
        if (!bookExists) {
            System.out.println("Book ID not found.");
            return;
        }
        MemberService memberService = new MemberService(this);
        if (memberService.getMemberById(memberId) == null) {
            System.out.println("Member ID not found.");
            return;
        }
        int transactionId = getNextTransactionId();
        String issueDate = java.time.LocalDate.now().toString();
        String returnDate = "null";
        Transaction transaction = new Transaction(transactionId, bookId, memberId, issueDate, returnDate);
        addTransaction(transaction);
        bookService.updateBookAvailability(bookId, false);
    }

    public void returnBook(int memberId, int bookId) {
        List<Transaction> transactions = getAllTransactions();
        for (Transaction t : transactions) {
            if (t.getMemberId() == memberId && t.getBookId() == bookId && t.getReturnDate().equals("null")) {
                updateReturnDate(t.getTransactionId(), java.time.LocalDate.now().toString());
                bookService.updateBookAvailability(bookId, true);
                return;
            }
        }
        System.out.println("No active borrowing found for this book and member.");
    }

    public List<Transaction> getTransactionsByMemberId(int memberId) {
        return getMemberTransactions(memberId);
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