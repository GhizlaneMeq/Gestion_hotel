package Menus;

import Entities.User;
import Entities.Moderator;
import Services.UserService;
import Services.ModeratorService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class LoginMenu {
    private UserService userService;
    private ModeratorService moderatorService;
    private  UserMenu userMenu;
    private Scanner scanner;

    private ModeratorMenu moderatorMenu;

    public LoginMenu(UserService userService, ModeratorService moderatorService,ModeratorMenu moderatorMenu, UserMenu userMenu) {
        this.userService = userService;
        this.moderatorService = moderatorService;
        this.scanner = new Scanner(System.in);
        this.moderatorMenu = moderatorMenu;
        this.userMenu = userMenu;
    }

    public void show() {
        boolean running = true;
        while (running) {
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            Optional<User> userOptional = userService.loginUser(email, password);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("Login successful!");

                // Check if the user is a moderator
                Optional<Moderator> moderatorOptional = moderatorService.getModeratorByUserId(user.getId());
                if (moderatorOptional.isPresent()) {
                    System.out.println("You are logged in as a Moderator.");
                    moderatorMenu.show();
                } else {
                    System.out.println("You are logged in as a Regular User.");
                    // Redirect to user menu if needed
                }
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void signUp() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = new User(null, name, email, phone, password);

        try {
            userService.registerUser(user);
            System.out.println("Sign up successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showModeratorMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Moderator Menu:");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Events");
            System.out.println("3. Manage Moderators");
            System.out.println("4. View Statistics");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    manageRooms();
                    break;
                case 2:
                    manageEvents();
                    break;
                case 3:
                    manageModerators();
                    break;
                case 4:
                    viewStatistics();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageRooms() {
        // Implementation for managing rooms
        System.out.println("Managing rooms...");
    }

    private void manageEvents() {
        // Implementation for managing events
        System.out.println("Managing events...");
    }

    private void manageModerators() {
        // Implementation for managing moderators
        System.out.println("Managing moderators...");
    }

    private void viewStatistics() {
        // Implementation for viewing statistics
        System.out.println("Viewing statistics...");
    }
}
