package model;

public class Member {
    private int memberId;
    private String name;
    private String role;
    private String username;
    private String password;
    private String email;
    private String bookTitle; // Added for transaction display

    public Member(int memberId, String name, String role, String username, String password, String email) {
        this.memberId = memberId;
        this.name = name;
        this.role = role;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Member(int memberId, String name, String email) {
        this(memberId, name, "MEMBER", "", "", email);
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getBookTitle() { return bookTitle; }

    public void setMemberId(int memberId) { this.memberId = memberId; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String toCSV() {
        return memberId + "," +
               name.replace(",", "\\,") + "," +
               role + "," +
               username.replace(",", "\\,") + "," +
               password.replace(",", "\\,") + "," +
               email.replace(",", "\\,");
    }
}