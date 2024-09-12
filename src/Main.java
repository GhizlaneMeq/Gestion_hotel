import Menus.ModeratorMenu;
import Menus.UserMenu;
import Repositories.*;
import Services.*;
import Menus.LoginMenu;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        ModeratorRepository moderatorRepository = new ModeratorRepository();
        RoomRepository roomRepository = new RoomRepository();
        SpecialEventRepository specialEventRepository = new SpecialEventRepository();
        ReservationRepository reservationRepository = new ReservationRepository();

        UserService userService = new UserService(userRepository);
        RoomService roomService = new RoomService(roomRepository);
        SpecialEventService specialEventService = new SpecialEventService(specialEventRepository);
        ModeratorService moderatorService = new ModeratorService(moderatorRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);

        ModeratorMenu moderatorMenu = new ModeratorMenu(roomService, specialEventService, moderatorService);

        UserMenu userMenu = new UserMenu(userService, reservationService, null);

        LoginMenu loginMenu = new LoginMenu(userService, moderatorService, moderatorMenu, userMenu);
        loginMenu.show();
    }
}
