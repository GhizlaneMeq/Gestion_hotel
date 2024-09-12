package Services;

import Entities.User;
import Repositories.UserRepository;
import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) throws SQLException {
        userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String password) throws SQLException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Optional<User> findById(Long id) throws SQLException {
        return userRepository.findById(id);
    }
}
