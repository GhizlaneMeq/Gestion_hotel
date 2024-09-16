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
        RoomPricingRepository roomPricingRepository = new RoomPricingRepository();

        UserService userService = new UserService(userRepository);
        RoomService roomService = new RoomService(roomRepository);
        SpecialEventService specialEventService = new SpecialEventService(specialEventRepository);
        ModeratorService moderatorService = new ModeratorService(moderatorRepository);
        ReservationService reservationService = new ReservationService(reservationRepository);
        RoomPricingService roomPricingService = new RoomPricingService(roomPricingRepository, specialEventService);

        ModeratorMenu moderatorMenu = new ModeratorMenu(moderatorService, specialEventService, roomPricingService, roomService, userService);

        UserMenu userMenu = new UserMenu(reservationService,roomPricingService, specialEventService,roomService, userService);

        LoginMenu loginMenu = new LoginMenu(userService, moderatorService, moderatorMenu, userMenu);
        loginMenu.show();
    }
}
