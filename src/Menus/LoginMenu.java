package Menus;
import Entities.User;
import Entities.Moderator;
import Services.UserService;
import Services.ModeratorService;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
public class LoginMenu {

    private final UserService userService;
    private final ModeratorService moderatorService;
    private final UserMenu userMenu;
    private final Scanner scanner;
    private final ModeratorMenu moderatorMenu;
    private User loggedInUser = null;

    public LoginMenu(UserService userService, ModeratorService moderatorService, ModeratorMenu moderatorMenu, UserMenu userMenu) {
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
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int option = getIntInput(scanner);
            if (option == -1) continue;

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    signUp();
                    break;
                case 3:
                    logout();
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void login() {
        if (loggedInUser != null) {
            System.out.println("A user is already logged in. Please log out first.");
            return;
        }
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            Optional<User> userOptional = userService.loginUser(email, password);
            if (userOptional.isPresent()) {
                loggedInUser = userOptional.get();
                userMenu.setLoggedInUser(loggedInUser);
                System.out.println("Login successful!");

                Optional<Moderator> moderatorOptional = moderatorService.getModeratorByUserId(loggedInUser.getId());
                if (moderatorOptional.isPresent()) {
                    System.out.println("You are logged in as a Moderator.");
                    moderatorMenu.displayMenu();
                } else {
                    System.out.println("You are logged in as a Regular User.");
                    userMenu.showMenu(loggedInUser.getId());
                }
            } else {
                System.out.println("Invalid email or password.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred during login: " + e.getMessage());
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
            System.out.println("An error occurred during sign up: " + e.getMessage());
        }
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("Logging out " + loggedInUser.getName() + "...");
            loggedInUser = null;
            userMenu.setLoggedInUser(null);
            System.out.println("Logout successful.");
        } else {
            System.out.println("No user is logged in.");
        }
    }

    private int getIntInput(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return -1;
        }
    }
}
