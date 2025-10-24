package service;

import model.Member;
import java.util.Scanner;

public class AuthenticationService {
    private final Scanner scanner = new Scanner(System.in);
    private final MemberService memberService;

    public AuthenticationService(MemberService memberService) {
        this.memberService = memberService;
    }

    public Member login(String username, String password) {
        for (Member member : memberService.getAllMembers()) {
            if (member.getUsername().equals(username) && member.getPassword().equals(password)) {
                return member;
            }
        }
        System.out.println("Invalid username or password.");
        return null;
    }

    public boolean loginAsAdmin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();
        Member member = login(username, password);
        return member != null && member.getRole().equalsIgnoreCase("ADMIN");
    }

    public int loginAsMember() {
        System.out.print("Enter member ID: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid member ID!");
            return -1;
        }
    }
}