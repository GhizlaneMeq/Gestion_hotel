package Repositories;

import Entities.Moderator;
import Utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeratorRepository implements GenericRepository<Moderator, Integer> {
    private final Connection connection;

    public ModeratorRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Moderator moderator) throws SQLException {
        String query = "INSERT INTO moderators (user_id, permission) VALUES (?, ?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, moderator.getId().intValue());
            statement.setString(2, moderator.getPermission());

            ResultSet resultSet = statement.executeQuery();
            /*if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Failed to retrieve generated ID.");
            }*/
        }
    }

    @Override
    public Moderator findById(Integer id) throws SQLException {
        String query = "SELECT m.id as moderator_id, u.id as user_id, u.name, u.email, u.phone, u.password, m.permission " +
                "FROM moderators m " +
                "JOIN users u ON m.user_id = u.id " +
                "WHERE m.id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Moderator(
                        resultSet.getLong("moderator_id"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("permission")
                );
            } else {
                return null;
            }
        }
    }

    @Override
    public List<Moderator> findAll() throws SQLException {
        String query = "SELECT m.id as moderator_id, u.id as user_id, u.name, u.email, u.phone, u.password, m.permission " +
                "FROM moderators m " +
                "JOIN users u ON m.user_id = u.id";
        List<Moderator> moderators = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                moderators.add(new Moderator(
                        resultSet.getLong("moderator_id"),
                        resultSet.getLong("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getString("permission")
                ));
            }
        }
        return moderators;
    }

    @Override
    public void update(Moderator moderator, Integer id) throws SQLException {
        String query = "UPDATE moderators SET permission = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, moderator.getPermission());
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String query = "DELETE FROM moderators WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
