package model;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int memberId;
    private String issueDate;
    private String returnDate;
    private String bookTitle; // Added for display

    public Transaction(int transactionId, int bookId, int memberId, String issueDate, String returnDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public int getTransactionId() { return transactionId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getIssueDate() { return issueDate; }
    public String getReturnDate() { return returnDate; }
    public String getBorrowDate() { return issueDate; }
    public String getBookTitle() { return bookTitle; }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setIssueDate(String issueDate) { this.issueDate = issueDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String toCSV() {
        return transactionId + "," +
               bookId + "," +
               memberId + "," +
               issueDate + "," +
               (returnDate != null ? returnDate.replace(",", "\\,") : "null");
    }
}