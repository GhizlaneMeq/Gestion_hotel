package Menus;
import Entities.*;
import Services.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class ModeratorMenu {

    private final ModeratorService moderatorService;
    private final SpecialEventService specialEventService;
    private final RoomPricingService roomPricingService;
    private final RoomService roomService;
    private final StatistiqueService statistiqueService;
    private final UserService userService;
    public ModeratorMenu(ModeratorService moderatorService, SpecialEventService specialEventService,
                         RoomPricingService roomPricingService, RoomService roomService, UserService userService,StatistiqueService statistiqueService) {
        this.moderatorService = moderatorService;
        this.specialEventService = specialEventService;
        this.roomPricingService = roomPricingService;
        this.roomService = roomService;
        this.userService = userService;
        this.statistiqueService = statistiqueService;
    }

    public void displayMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Moderator Menu ---");
            System.out.println("1. Manage Moderators");
            System.out.println("2. Manage Events");
            System.out.println("3. Manage Room Pricing");
            System.out.println("4. Manage Rooms");
            System.out.println("5. View Statistics");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> manageModerators(scanner);
                case 2 -> manageEvents(scanner);
                case 3 -> manageRoomPricing(scanner);
                case 4 -> manageRooms(scanner);
                case 5 -> viewStatistics(scanner);
                case 6 -> System.out.println("Exiting Moderator Menu...");
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void manageModerators(Scanner scanner) throws SQLException {
        System.out.println("\n--- Manage Moderators ---");
        System.out.println("1. Add Moderator");
        System.out.println("2. View Moderators");
        System.out.println("3. Remove Moderator");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addModerator(scanner);
            case 2 -> viewModerators();
            case 3 -> removeModerator(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void addModerator(Scanner scanner) {
        System.out.println("\n--- Add Moderator ---");
        System.out.println("1. Promote Existing User to Moderator");
        System.out.println("2. Create New User and Make Them a Moderator");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                promoteExistingUser(scanner);
                break;
            case 2:
                createNewUserAndMakeModerator(scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void promoteExistingUser(Scanner scanner) {
        System.out.print("Enter User ID to promote: ");
        Long userId = scanner.nextLong();
        scanner.nextLine();
        System.out.print("Enter Permission (e.g., FULL_ACCESS / LIMITED_ACCESS): ");
        String permission = scanner.nextLine();

        try {
            Optional<User> userOptional = userService.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                Moderator moderator = new Moderator(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getPassword(), permission);
                moderatorService.addModerator(moderator);
                System.out.println("User promoted to moderator successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while promoting the user: " + e.getMessage());
        }
    }

    private void createNewUserAndMakeModerator(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Permission (e.g., FULL_ACCESS / LIMITED_ACCESS): ");
        String permission = scanner.nextLine();

        try {
            User newUser = new User(null, name, email, phone, password);
            userService.registerUser(newUser);
            Optional<User> createdUserOptional = userService.findUserByEmail(email);
            if (createdUserOptional.isPresent()) {
                Moderator newModerator = new Moderator(createdUserOptional.get(), permission);
                moderatorService.addModerator(newModerator);
                System.out.println("New user created and promoted to moderator successfully.");
            } else {
                System.out.println("Failed to retrieve the newly created user.");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while creating the new user and making them a moderator: " + e.getMessage());
        }
    }


    private void viewModerators() throws SQLException {
        System.out.println("\n--- List of Moderators ---");
        moderatorService.getAllModerators().forEach(System.out::println);
    }

    private void removeModerator(Scanner scanner) throws SQLException {
        System.out.print("\nEnter Moderator ID to remove: ");
        Long id = scanner.nextLong();
        moderatorService.delete(id);
        System.out.println("Moderator removed successfully.");
    }

    private void manageEvents(Scanner scanner) throws SQLException {
        System.out.println("\n--- Manage Events ---");

        System.out.println("1. Add Event");
        System.out.println("2. View Events");
        System.out.println("3. Remove Event");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addEvent(scanner);
            case 2 -> viewEvents();
            case 3 -> removeEvent(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void addEvent(Scanner scanner) {
        System.out.println("\n--- Add Event ---");
        System.out.print("Enter Event Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Extra Charge: ");
        BigDecimal extraCharge = scanner.nextBigDecimal();
        specialEventService.createEvent(new SpecialEvent(null, name, startDate, endDate, extraCharge));
        System.out.println("Event added successfully.");
    }

    private void viewEvents() throws SQLException {
        System.out.println("\n--- List of Events ---");
        specialEventService.getAllEvents().forEach(System.out::println);
    }

    private void removeEvent(Scanner scanner) throws SQLException {
        System.out.print("\nEnter Event ID to remove: ");
        Long id = scanner.nextLong();
        specialEventService.deleteEvent(id);
        System.out.println("Event removed successfully.");
    }

    private void manageRoomPricing(Scanner scanner) throws SQLException {
        System.out.println("\n--- Manage Room Pricing ---");
        System.out.println("1. Add Room Pricing");
        System.out.println("2. View Room Pricing");
        System.out.println("3. Remove Room Pricing");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addRoomPricing(scanner);
            case 2 -> viewRoomPricing();
            case 3 -> removeRoomPricing(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void addRoomPricing(Scanner scanner) throws SQLException {
        System.out.println("\n--- Add Room Pricing ---");
        System.out.print("Enter Room Type (SINGLE/DOUBLE/SUITE): ");
        String roomTypeInput = scanner.nextLine();

        RoomType roomType;
        try {
            roomType = RoomType.valueOf(roomTypeInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type. Please enter SINGLE, DOUBLE, or SUITE.");
            return;
        }

        System.out.print("Enter Base Price: ");
        BigDecimal basePrice = scanner.nextBigDecimal();

        RoomPricing roomPricing = new RoomPricing(null, roomType, basePrice);
        roomPricingService.addRoomPricing(roomPricing);

        System.out.println("Room pricing added successfully.");
    }


    private void viewRoomPricing() throws SQLException {
        System.out.println("\n--- List of Room Pricing ---");
        roomPricingService.getAllRoomPricing().forEach(System.out::println);
    }

    private void removeRoomPricing(Scanner scanner) throws SQLException {
        System.out.print("\nEnter Room Pricing ID to remove: ");
        Long id = scanner.nextLong();
        roomPricingService.removeRoomPricing(id);
        System.out.println("Room pricing removed successfully.");
    }

    private void manageRooms(Scanner scanner) throws SQLException {
        System.out.println("\n--- Manage Rooms ---");
        System.out.println("1. Add Room");
        System.out.println("2. View Rooms");
        System.out.println("3. Remove Room");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> addRoom(scanner);
            case 2 -> viewRooms();
            case 3 -> removeRoom(scanner);
            default -> System.out.println("Invalid choice.");
        }
    }

    private void addRoom(Scanner scanner) throws SQLException {
        System.out.println("\n--- Add Room ---");
        System.out.print("Enter Room Number: ");
        int roomNumber = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Room Type (SINGLE/DOUBLE/SUITE/DELUXE): ");
        String roomType = scanner.nextLine();
        roomService.create(new Room(null, roomNumber, RoomType.valueOf(roomType)));
        System.out.println("Room added successfully.");
    }

    private void viewRooms() throws SQLException {
        System.out.println("\n--- List of Rooms ---");
        roomService.getAll().forEach(System.out::println);
    }

    private void removeRoom(Scanner scanner) throws SQLException {
        System.out.print("\nEnter Room ID to remove: ");
        Long id = scanner.nextLong();
        roomService.delete(id);
        System.out.println("Room removed successfully.");
    }

    private void viewStatistics(Scanner scanner) throws SQLException {
        System.out.println("\n--- View Statistics ---");

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        Double occupancyRate = statistiqueService.calculateOccupancyRate(startDate, endDate);
        if (occupancyRate == null) {
            System.out.println("Occupancy Rate: No data available.");
        } else {
            System.out.printf("Occupancy Rate: %.2f%%\n", occupancyRate);
        }

        BigDecimal totalRevenue = statistiqueService.calculateTotalRevenue(startDate, endDate);
        if (totalRevenue == null) {
            System.out.println("Total Revenue: No data available.");
        } else {
            System.out.println("Total Revenue: $" + totalRevenue);
        }

        Long canceledReservations = statistiqueService.countCanceledReservations(startDate, endDate);
        if (canceledReservations == null) {
            System.out.println("Canceled Reservations: No data available.");
        } else {
            System.out.println("Canceled Reservations: " + canceledReservations);
        }

        List<Room> mostReservedRooms = statistiqueService.getMostReservedRooms(2);
        if (mostReservedRooms == null || mostReservedRooms.isEmpty()) {
            System.out.println("\nTop 3 Most Reserved Rooms: No data available.");
        } else {
            System.out.println("\nTop 3 Most Reserved Rooms:");
            mostReservedRooms.forEach(System.out::println);
        }

        Map<RoomType, BigDecimal> revenuePerRoomType = statistiqueService.calculateRevenuePerRoomType(startDate, endDate);
        if (revenuePerRoomType == null || revenuePerRoomType.isEmpty()) {
            System.out.println("\nRevenue Per Room Type: No data available.");
        } else {
            System.out.println("\nRevenue Per Room Type:");
            revenuePerRoomType.forEach((roomType, revenue) -> System.out.println(roomType + ": $" + revenue));
        }

        Map<RoomType, Long> reservedRoomsPerType = statistiqueService.countCurrentlyReservedRoomsPerType();
        if (reservedRoomsPerType == null || reservedRoomsPerType.isEmpty()) {
            System.out.println("\nCurrently Reserved Rooms Per Room Type: No data available.");
        } else {
            System.out.println("\nCurrently Reserved Rooms Per Room Type:");
            ((Map<?, ?>) reservedRoomsPerType).forEach((roomType, count) -> System.out.println(roomType + ": " + count));
        }
    }


}
