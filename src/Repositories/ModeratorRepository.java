package Repositories;

import Entities.Moderator;
import Entities.User;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModeratorRepository implements BaseRepository<Moderator> {
    private final Connection connection;
    private final UserRepository userRepository = new UserRepository();

    public ModeratorRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Moderator> findById(Long userId) throws SQLException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            String sql = "SELECT permission FROM moderators WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setLong(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(new Moderator(user.get(), rs.getString("permission")));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Moderator> findAll() {
        List<Moderator> moderators = new ArrayList<>();
        try {
            String sql = "SELECT * FROM moderators";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                moderators.add(new Moderator(
                        rs.getLong("id"),
                        rs.getString("permission")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return moderators;
    }

   @Override
    public Moderator save(Moderator moderator) throws SQLException {

        String sql = "INSERT INTO moderators (id, permission) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, moderator.getId());
            stmt.setString(2, moderator.getPermission());
            stmt.executeUpdate();
        }
        return moderator;
    }

    @Override
    public boolean update(Moderator moderator) {
        try {
            String sql = "UPDATE moderators SET permission = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, moderator.getPermission());
            statement.setLong(2, moderator.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM moderators WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
        userRepository.delete(id);
    }
}
