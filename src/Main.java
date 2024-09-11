import Entities.User;
import Services.UserService;

import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();

        try {
            User newUser = new User(1L,"John Doe", "john.doe@example.com", "123-456-7890", "password123");
            userService.createUser(newUser);

            // Retrieve all users
            /*List<User> users = userService.getAllUsers();
            for (User user : users) {
                System.out.println(user);
            }*/
/*
            // Update a user
            User existingUser = userService.getUserById(1); // Example ID
            existingUser.setPhone("987-654-3210");
            userService.updateUser(existingUser, 1);

            // Delete a user
            userService.deleteUser(1); // Example ID
*/
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception appropriately
        }
    }
}
