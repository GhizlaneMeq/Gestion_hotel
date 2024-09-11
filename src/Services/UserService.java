package Services;

import Repositories.UserRepository;
import Entities.User;

import java.sql.SQLException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(){
        this.userRepository = new UserRepository();
    }

    public void createUser(User user) throws SQLException {
        userRepository.save(user);
    }


}
