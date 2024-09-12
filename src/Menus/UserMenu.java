package Menus;

import Entities.Reservation;
import Entities.User;
import Services.ReservationService;
import Services.UserService;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;


public class UserMenu {
    private UserService userService;
    private ReservationService reservationService;
    private User currentUser;

    public UserMenu(UserService userService, ReservationService reservationService, User currentUser) {
        this.userService = userService;
        this.reservationService = reservationService;
        this.currentUser = currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("User Menu");
            System.out.println("1. View Profile");
            System.out.println("2. Manage Reservations");
            System.out.println("3. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    manageReservations(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewProfile() {
        System.out.println("Profile Information:");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Email: " + currentUser.getEmail());
        System.out.println("Phone: " + currentUser.getPhone());
        // Not displaying the password for security reasons
    }

    private void manageReservations(Scanner scanner) {
        while (true) {
            System.out.println("Manage Reservations Menu");
            System.out.println("1. View Reservations");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel a Reservation");
            System.out.println("4. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    viewReservations();
                    break;
                case 2:
                    makeReservation(scanner);
                    break;
                case 3:
                    cancelReservation(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewReservations() {
        List<Reservation> reservations = reservationService.getReservationsByUserId(currentUser.getId());
        if (reservations.isEmpty()) {
            System.out.println("You have no reservations.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private void makeReservation(Scanner scanner) {
        System.out.print("Enter room ID: ");
        Long roomId = Long.parseLong(scanner.nextLine());
        System.out.print("Enter check-in date (YYYY-MM-DD): ");
        LocalDate checkInDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter check-out date (YYYY-MM-DD): ");
        LocalDate checkOutDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter special requests (or leave blank): ");
        String specialRequests = scanner.nextLine();

        BigDecimal totalPrice = calculateTotalPrice(roomId, checkInDate, checkOutDate);

        Reservation reservation = new Reservation(null, currentUser.getId(), roomId,
                "Confirmed", checkInDate, checkOutDate, totalPrice, specialRequests);

        reservationService.create(reservation);
        System.out.println("Reservation made successfully!");
    }

    private BigDecimal calculateTotalPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        return BigDecimal.ZERO;
    }

    private void cancelReservation(Scanner scanner) {
        System.out.print("Enter reservation ID to cancel: ");
        Long reservationId = Long.parseLong(scanner.nextLine());
        reservationService.cancel(reservationId);
        System.out.println("Reservation canceled successfully.");
    }
}
