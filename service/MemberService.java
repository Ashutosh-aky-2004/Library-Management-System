package service;

import model.Member;
import model.Transaction;
import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class MemberService {
    private static final String MEMBERS_FILE = "data/members.txt";
    private final TransactionService transactionService;

    public MemberService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public MemberService() {
        this.transactionService = new TransactionService();
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(MEMBERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line);
                if (parts.length == 6) {
                    int memberId = Integer.parseInt(parts[0]);
                    String name = parts[1].replace("\\,", ",");
                    String role = parts[2];
                    String username = parts[3].replace("\\,", ",");
                    String password = parts[4].replace("\\,", ",");
                    String email = parts[5].replace("\\,", ",");
                    members.add(new Member(memberId, name, role, username, password, email));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading members file: " + e.getMessage());
        }
        return members;
    }

    public void addMember(Member member) {
        List<Member> members = getAllMembers();
        for (Member m : members) {
            if (m.getMemberId() == member.getMemberId()) {
                System.out.println("Member ID " + member.getMemberId() + " already exists.");
                return;
            }
            if (m.getUsername().equals(member.getUsername())) {
                System.out.println("Username " + member.getUsername() + " is already taken.");
                return;
            }
            if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", member.getEmail())) {
                System.out.println("Invalid email format for member.");
                return;
            }
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBERS_FILE, true))) {
            bw.write(member.toCSV());
            bw.newLine();
            System.out.println("Member added successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to members file: " + e.getMessage());
        }
    }

    public Member getMemberById(int memberId) {
        for (Member m : getAllMembers()) {
            if (m.getMemberId() == memberId) {
                return m;
            }
        }
        return null;
    }

    public void removeMember(int memberId) {
        for (Transaction t : transactionService.getAllTransactions()) {
            if (t.getMemberId() == memberId && t.getReturnDate().equals("null")) {
                System.out.println("Cannot remove member: They have active borrowed books.");
                return;
            }
        }
        List<Member> members = getAllMembers();
        boolean removed = members.removeIf(m -> m.getMemberId() == memberId);

        if (removed) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
                for (Member m : members) {
                    bw.write(m.toCSV());
                    bw.newLine();
                }
                System.out.println("Member removed successfully.");
            } catch (IOException e) {
                System.out.println("Error updating members file: " + e.getMessage());
            }
        } else {
            System.out.println("Member not found.");
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