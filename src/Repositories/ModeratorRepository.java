package Repositories;

import Entities.Moderator;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModeratorRepository {
    private final Connection connection;

    public ModeratorRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(Moderator moderator) throws SQLException {
        String query = "INSERT INTO moderators (user_id, name, email, phone, password, permission) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, moderator.getId());
            statement.setString(2, moderator.getName());
            statement.setString(3, moderator.getEmail());
            statement.setString(4, moderator.getPhone());
            statement.setString(5, moderator.getPassword());
            statement.setString(6, moderator.getPermission());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                moderator.setId(keys.getLong(1));
            }
        }
    }

    public Optional<Moderator> findById(Long id) throws SQLException {
        String query = "SELECT * FROM moderators WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Moderator moderator = new Moderator(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("permission")
                );
                return Optional.of(moderator);
            }
        }
        return Optional.empty();
    }

    public List<Moderator> findAll() throws SQLException {
        List<Moderator> moderators = new ArrayList<>();
        String query = "SELECT * FROM moderators";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Moderator moderator = new Moderator(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("permission")
                );
                moderators.add(moderator);
            }
        }
        return moderators;
    }

    public void update(Moderator moderator, Long id) throws SQLException {
        String query = "UPDATE moderators SET name = ?, email = ?, phone = ?, password = ?, permission = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, moderator.getName());
            statement.setString(2, moderator.getEmail());
            statement.setString(3, moderator.getPhone());
            statement.setString(4, moderator.getPassword());
            statement.setString(5, moderator.getPermission());
            statement.setLong(6, id);
            statement.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM moderators WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public Optional<Moderator> findByUserId(Long userId) throws SQLException {
        String query = "SELECT m.id, u.name, u.email, u.phone, u.password, m.permission " +
                "FROM moderators m " +
                "JOIN users u ON m.user_id = u.id " +
                "WHERE u.id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Moderator moderator = new Moderator(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("permission")
                );
                return Optional.of(moderator);
            }
        }
        return Optional.empty();
    }

}
