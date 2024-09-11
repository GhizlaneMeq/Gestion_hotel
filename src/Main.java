import Entities.User;
import Entities.Room;
import Exceptions.RoomAlreadyExistException;
import Exceptions.UserAlreadyExistsException;
import Menus.LoginMenu;
import Menus.ModeratorMenu;
import Menus.UserMenu;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (LoginMenu.getCurrentUser() == null) {
                LoginMenu.showLoginMenu(scanner);
            } else {
                User currentUser = LoginMenu.getCurrentUser();
                ModeratorMenu.showUserMenu(scanner, currentUser);
            }
        }
    }


}
