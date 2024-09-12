package Menus;

import Entities.Moderator;
import Entities.Room;
import Entities.RoomType;
import Entities.SpecialEvent;
import Services.ModeratorService;
import Services.RoomService;
import Services.SpecialEventService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ModeratorMenu {
    private RoomService roomService;
    private SpecialEventService eventService;
    private ModeratorService moderatorService;

    public ModeratorMenu(RoomService roomService, SpecialEventService eventService, ModeratorService moderatorService) {
        this.roomService = roomService;
        this.eventService = eventService;
        this.moderatorService = moderatorService;
    }

    public void show() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Moderator Menu");
            System.out.println("1. Manage Rooms");
            System.out.println("2. Manage Events");
            System.out.println("3. Manage Moderators");
            System.out.println("4. View Statistics");
            System.out.println("5. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    manageRooms(scanner);
                    break;
                case 2:
                    manageEvents(scanner);
                    break;
                case 3:
                    manageModerators(scanner);
                    break;
                case 4:
                    viewStatistics(scanner);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void manageRooms(Scanner scanner) {
        while (true) {
            System.out.println("Manage Rooms Menu");
            System.out.println("1. Create Room");
            System.out.println("2. Update Room");
            System.out.println("3. Delete Room");
            System.out.println("4. View Rooms");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createRoom(scanner);
                    break;
                case 2:
                    updateRoom(scanner);
                    break;
                case 3:
                    deleteRoom(scanner);
                    break;
                case 4:
                    viewRooms();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createRoom(Scanner scanner) {
        System.out.print("Enter room number: ");
        Integer roomNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter room type (SINGLE, DOUBLE, SUITE): ");
        String roomTypeInput = scanner.nextLine();
        RoomType roomType = parseRoomType(roomTypeInput);

        if (roomType != null) {
            System.out.print("Enter availability (true/false): ");
            Boolean isAvailable = Boolean.parseBoolean(scanner.nextLine());

            Room room = new Room(null, roomNumber, roomType, isAvailable);
            try {
                roomService.createRoom(room);
                System.out.println("Room created successfully.");
            } catch (SQLException e) {
                System.out.println("Error creating room: " + e.getMessage());
            }
        }
    }

    private void updateRoom(Scanner scanner) {
        System.out.print("Enter room ID to update: ");
        Long roomId = Long.parseLong(scanner.nextLine());
        Optional<Room> roomOptional = roomService.getRoomById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            System.out.print("Enter new room number: ");
            room.setRoomNumber(Integer.parseInt(scanner.nextLine()));
            System.out.print("Enter new room type (SINGLE, DOUBLE, SUITE): ");
            String roomTypeInput = scanner.nextLine();
            RoomType roomType = parseRoomType(roomTypeInput);
            if (roomType != null) {
                room.setRoomType(roomType);
            }
            System.out.print("Enter new availability (true/false): ");
            room.setAvailable(Boolean.parseBoolean(scanner.nextLine()));

            roomService.update(room);
            System.out.println("Room updated successfully.");
        } else {
            System.out.println("Room not found.");
        }
    }

    private void deleteRoom(Scanner scanner) {
        System.out.print("Enter room ID to delete: ");
        Long roomId = Long.parseLong(scanner.nextLine());
        roomService.delete(roomId);
        System.out.println("Room deleted successfully.");
    }

    private void viewRooms() {
        try {
            List<Room> rooms = roomService.getAllRooms();
            for (Room room : rooms) {
                System.out.println(room);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving rooms: " + e.getMessage());
        }
    }

    private void manageEvents(Scanner scanner) {
        while (true) {
            System.out.println("Manage Events Menu");
            System.out.println("1. Create Event");
            System.out.println("2. Update Event");
            System.out.println("3. Delete Event");
            System.out.println("4. View Events");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createEvent(scanner);
                    break;
                case 2:
                    updateEvent(scanner);
                    break;
                case 3:
                    deleteEvent(scanner);
                    break;
                case 4:
                    viewEvents();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createEvent(Scanner scanner) {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter extra charge: ");
        BigDecimal extraCharge = new BigDecimal(scanner.nextLine());

        SpecialEvent event = new SpecialEvent(null, eventName, startDate, endDate, extraCharge);
        try {
            eventService.createEvent(event);
            System.out.println("Event created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating event: " + e.getMessage());
        }
    }

    private void updateEvent(Scanner scanner) {
        System.out.print("Enter event ID to update: ");
        Long eventId = Long.parseLong(scanner.nextLine());
        Optional<SpecialEvent> eventOptional = eventService.getEventById(eventId);
        if (eventOptional.isPresent()) {
            SpecialEvent event = eventOptional.get();
            System.out.print("Enter new event name: ");
            event.setEventName(scanner.nextLine());
            System.out.print("Enter new start date (YYYY-MM-DD): ");
            event.setStartDate(LocalDate.parse(scanner.nextLine()));
            System.out.print("Enter new end date (YYYY-MM-DD): ");
            event.setEndDate(LocalDate.parse(scanner.nextLine()));
            System.out.print("Enter new extra charge: ");
            event.setExtraCharge(new BigDecimal(scanner.nextLine()));

            eventService.update(event);
            System.out.println("Event updated successfully.");
        } else {
            System.out.println("Event not found.");
        }
    }

    private void deleteEvent(Scanner scanner) {
        System.out.print("Enter event ID to delete: ");
        Long eventId = Long.parseLong(scanner.nextLine());
        eventService.delete(eventId);
        System.out.println("Event deleted successfully.");
    }

    private void viewEvents() {
        try {
            List<SpecialEvent> events = eventService.getAllEvents();
            for (SpecialEvent event : events) {
                System.out.println(event);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving events: " + e.getMessage());
        }
    }

    private void manageModerators(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Manage Moderators Menu");
            System.out.println("1. Create Moderator");
            System.out.println("2. Update Moderator");
            System.out.println("3. Delete Moderator");
            System.out.println("4. View Moderators");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    createModerator(scanner);
                    break;
                case 2:
                    updateModerator(scanner);
                    break;
                case 3:
                    deleteModerator(scanner);
                    break;
                case 4:
                    viewModerators();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createModerator(Scanner scanner) {
        System.out.print("Enter moderator name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter permissions: ");
        String permission = scanner.nextLine();

        Moderator moderator = new Moderator(null, name, email, phone, password, permission);
        try {
            moderatorService.addModerator(moderator);
            System.out.println("Moderator created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating moderator: " + e.getMessage());
        }
    }

    private void updateModerator(Scanner scanner) throws SQLException {
        System.out.print("Enter moderator ID to update: ");
        Long moderatorId = Long.parseLong(scanner.nextLine());
        Optional<Moderator> moderatorOptional = moderatorService.getModeratorById(moderatorId);
        if (moderatorOptional.isPresent()) {
            Moderator moderator = moderatorOptional.get();
            System.out.print("Enter new name: ");
            moderator.setName(scanner.nextLine());
            System.out.print("Enter new email: ");
            moderator.setEmail(scanner.nextLine());
            System.out.print("Enter new phone: ");
            moderator.setPhone(scanner.nextLine());
            System.out.print("Enter new password: ");
            moderator.setPassword(scanner.nextLine());
            System.out.print("Enter new permissions: ");
            moderator.setPermission(scanner.nextLine());

            try {
                moderatorService.update(moderator);
                System.out.println("Moderator updated successfully.");
            } catch (SQLException e) {
                System.out.println("Error updating moderator: " + e.getMessage());
            }
        } else {
            System.out.println("Moderator not found.");
        }
    }

    private void deleteModerator(Scanner scanner) {
        System.out.print("Enter moderator ID to delete: ");
        Long moderatorId = Long.parseLong(scanner.nextLine());
        try {
            moderatorService.delete(moderatorId);
            System.out.println("Moderator deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting moderator: " + e.getMessage());
        }
    }

    private void viewModerators() {
        try {
            List<Moderator> moderators = moderatorService.getAllModerators();
            for (Moderator moderator : moderators) {
                System.out.println(moderator);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving moderators: " + e.getMessage());
        }
    }

    private void viewStatistics(Scanner scanner) {
        System.out.println("Statistics feature is not yet implemented.");
    }

    private RoomType parseRoomType(String input) {
        try {
            return RoomType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid room type. Please enter one of the following: SINGLE, DOUBLE, SUITE.");
            return null;
        }
    }
}

