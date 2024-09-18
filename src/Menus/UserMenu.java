package Menus;

import Entities.*;
import Services.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UserMenu {
    private final ReservationService reservationService;
    private final RoomPricingService roomPricingService;
    private final SpecialEventService specialEventService;
    private final RoomService roomService;
    private final UserService userService;
    private User loggedInUser;
    private final Scanner scanner;

    public UserMenu(ReservationService reservationService, RoomPricingService roomPricingService, SpecialEventService specialEventService, RoomService roomService, UserService userService) {
        this.reservationService = reservationService;
        this.roomPricingService = roomPricingService;
        this.specialEventService = specialEventService;
        this.roomService = roomService;
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void showMenu(Long userId) {
        if (loggedInUser == null) {
            System.out.println("No user is logged in.");
            return;
        }

        while (true) {
            System.out.println("User Menu:");
            System.out.println("1. View Profile");
            System.out.println("2. View Reservations");
            System.out.println("3. Make a Reservation");
            System.out.println("4. Modify a Reservation");
            System.out.println("5. Exit");

            int choice = getIntInput();
            switch (choice) {
                case 1:
                    viewProfile(userId);
                    break;
                case 2:
                    viewReservations(userId);
                    break;
                case 3:
                    makeReservation(userId);
                    break;
                case 4:
                    modifyReservation(userId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void viewProfile(Long userId) {
        try {
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("=== Profile Information ===");
                System.out.println("Name: " + user.getName());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Phone: " + user.getPhone());
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user profile: " + e.getMessage());
        }
    }

    private void viewReservations(Long userId) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByUserId(userId);
            if (reservations.isEmpty()) {
                System.out.println("You have no reservations.");
            } else {
                for (Reservation reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getId());
                    System.out.println("Room Number: " + reservation.getRoom().getRoomNumber());
                    System.out.println("Check-In Date: " + reservation.getCheckInDate());
                    System.out.println("Check-Out Date: " + reservation.getCheckOutDate());
                    System.out.println("Status: " + reservation.getReservationStatus());
                    BigDecimal totalPrice = calculateTotalPrice(reservation.getRoom(), reservation.getCheckInDate(), reservation.getCheckOutDate());
                    System.out.println("-----------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
    }

    private void makeReservation(Long userId) {
        try {
            System.out.println("Enter check-in date (YYYY-MM-DD):");
            LocalDate checkInDate = getValidDate();
            if (checkInDate == null) return;

            System.out.println("Enter check-out date (YYYY-MM-DD):");
            LocalDate checkOutDate = getValidDate();
            if (checkOutDate == null || !validateDates(checkInDate, checkOutDate)) return;

            System.out.println("Enter room type (SINGLE, DOUBLE, SUITE):");
            String roomTypeInput = scanner.nextLine().toUpperCase();
            RoomType roomType;
            try {
                roomType = RoomType.valueOf(roomTypeInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid room type. Please enter SINGLE, DOUBLE, or SUITE.");
                return;
            }
            Optional<Room> availableRoom = findAvailableRoom(roomType, checkInDate, checkOutDate);

           if (availableRoom.isPresent()) {
                Room room = availableRoom.get();
                BigDecimal totalPrice = calculateTotalPrice(room, checkInDate, checkOutDate);
                System.out.println("Available Room Found!");
                System.out.println("Room Number: " + room.getRoomNumber());
                System.out.println("Total Price: " + totalPrice);
                System.out.println("Do you want to proceed with the reservation? (yes/no)");

                if (scanner.nextLine().equalsIgnoreCase("yes")) {
                    Optional<User> userOptional = userService.findById(userId);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        ReservationStatus reservationStatus;
                        reservationStatus = ReservationStatus.valueOf("Confirmed");
                        Reservation reservation = new Reservation(null, user, room,reservationStatus, checkInDate, checkOutDate, totalPrice);
                        boolean success = reservationService.create(reservation);
                        if (success) {
                            System.out.println("Reservation created successfully.");
                        } else {
                            System.out.println("Failed to create reservation. Please try again.");
                        }
                    } else {
                        System.out.println("User not found.");
                    }
                } else {
                    System.out.println("Reservation canceled.");
                }
            } else {
                System.out.println("No available rooms of the selected type for the given dates.");
            }


        } catch (SQLException e) {
            System.out.println("Error making reservation: " + e.getMessage());
        }
    }
    private void modifyReservation(Long userId) {
        System.out.println("Enter reservation ID to modify:");
    }




    private Optional<Room> findAvailableRoom(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        List<Room> rooms = roomService.getRoomsByType(roomType);
        for (Room room : rooms) {
            List<Reservation> reservations = reservationService.getReservationsByRoomId(room.getId());
            boolean isAvailable = true;
            System.out.println(room);
            for (Reservation reservation : reservations) {
                if (!(checkOutDate.isBefore(reservation.getCheckInDate()) || checkInDate.isAfter(reservation.getCheckOutDate()))) {
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                return Optional.of(room);
            }
        }

        return Optional.empty();
    }

    private BigDecimal calculateTotalPrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        BigDecimal basePrice = getBasePrice(room, checkInDate, checkOutDate);
        BigDecimal specialEventCharge = getSpecialEventCharge(checkInDate, checkOutDate);
       return basePrice.add(specialEventCharge);

    }

    private BigDecimal getBasePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        List<RoomPricing> pricings = roomPricingService.findByRoomType(room.getRoomType());
        BigDecimal totalBasePrice = BigDecimal.ZERO;
        long numberOfNights = java.time.Duration.between(checkInDate.atStartOfDay(), checkOutDate.atStartOfDay()).toDays();
        if (numberOfNights < 0) {
            throw new IllegalArgumentException("Check-out date cannot be before check-in date.");
        }
        for (RoomPricing pricing : pricings) {
            if (pricing.getRoomType() == room.getRoomType()) {
                totalBasePrice = totalBasePrice.add(pricing.getBasePrice().multiply(BigDecimal.valueOf(numberOfNights)));
            }
        }
        System.out.println("price"+totalBasePrice);
        return totalBasePrice;
    }



    private BigDecimal getSpecialEventCharge(LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        List<SpecialEvent> events = specialEventService.getAllEvents();
        BigDecimal totalCharge = BigDecimal.ZERO;

        for (SpecialEvent event : events) {
            if ((checkInDate.isBefore(event.getEndDate()) && checkOutDate.isAfter(event.getStartDate()))) {
                LocalDate start = checkInDate.isBefore(event.getStartDate()) ? event.getStartDate() : checkInDate;
                LocalDate end = checkOutDate.isAfter(event.getEndDate()) ? event.getEndDate() : checkOutDate;
                long days = java.time.Duration.between(start.atStartOfDay(), end.atStartOfDay()).toDays();
                totalCharge = totalCharge.add(event.getExtraCharge().multiply(BigDecimal.valueOf(days)));
            }
        }
        return totalCharge;
    }

    private LocalDate getValidDate() {
        try {
            return LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }

    private boolean validateDates(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkOutDate.isBefore(checkInDate)) {
            System.out.println("Check-out date cannot be before check-in date.");
            return false;
        }
        return true;
    }

    private int getIntInput() {
        try {
            return scanner.nextInt();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return -1;
        } finally {
            scanner.nextLine();
        }
    }
}
