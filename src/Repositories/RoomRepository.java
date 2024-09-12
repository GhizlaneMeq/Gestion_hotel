package Repositories;

import Entities.Room;
import Entities.RoomType;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository implements GenericRepository<Room, Long> {

    private final Connection connection;

    public RoomRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Room room) throws SQLException {
        String query = "INSERT INTO rooms (room_number, room_type, is_available) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setObject(2, room.getRoomType().name(), java.sql.Types.OTHER);
            statement.setBoolean(3, room.getAvailable());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                room.setId(keys.getLong(1));
            }
        }
    }

    @Override
    public Room findById(Long id) throws SQLException {
        String query = "SELECT id, room_number, room_type, is_available FROM rooms WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Room(
                        resultSet.getLong("id"),
                        resultSet.getInt("room_number"),
                        RoomType.valueOf(resultSet.getString("room_type")),
                        resultSet.getBoolean("is_available")
                );
            } else {
                return null;
            }
        }
    }

    @Override
    public List<Room> findAll() throws SQLException {
        String query = "SELECT * FROM rooms";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<Room> rooms = new ArrayList<>();
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getLong("id"),
                        resultSet.getInt("room_number"),
                        RoomType.valueOf(resultSet.getString("room_type")),
                        resultSet.getBoolean("is_available")
                );
                rooms.add(room);
            }
            return rooms;
        }
    }

    @Override
    public void update(Room room, Long id) throws SQLException {
        String query = "UPDATE rooms SET room_number = ?, room_type = ?, is_available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, room.getRoomNumber());
            statement.setObject(2, room.getRoomType().name(), java.sql.Types.OTHER);
            statement.setBoolean(3, room.getAvailable());
            statement.setLong(4, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    public void updateAvailability(Long roomId, boolean isAvailable) throws SQLException {
        String query = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, isAvailable);
            statement.setLong(2, roomId);
            statement.executeUpdate();
        }
    }
}
