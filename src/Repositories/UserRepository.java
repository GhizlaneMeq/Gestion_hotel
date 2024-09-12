package Repositories;

import Entities.User;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository /*implements GenericRepository<User, Long> */{
    private final Connection connection;

    public UserRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    //@Override
    public void save(User user) throws SQLException {
        String query = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getLong(1));
            }
        }
    }


    //@Override
    public Optional<User> findById(Long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                );
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    //@Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        /*String query = "SELECT * FROM users";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(mapResultSetToUser(resultSet));
            }
        }*/
        return users;
    }

    //@Override
    public void update(User user, Long id) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getPassword());
            statement.setLong(5, id);
            statement.executeUpdate();
        }
    }

    //@Override
    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<User> findByEmail(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password")
                );
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
