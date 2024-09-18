package Repositories;

import Entities.Room;
import Entities.RoomType;
import Utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepository implements BaseRepository<Room> {
    private Connection connection;

    public RoomRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Optional<Room> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToRoom(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        try {
            String sql = "SELECT * FROM rooms";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getLong("id"),
                        rs.getInt("room_number"),
                        RoomType.valueOf(rs.getString("room_type"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public Room save(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, room_type) VALUES (?, ?::room_type) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, room.getRoomNumber());
            stmt.setString(2, room.getRoomType().name()); // Store the enum as a string
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                room.setId(rs.getLong("id"));
            }
        }
        return room;
    }

    @Override
    public boolean update(Room room) {
        try {
            String sql = "UPDATE rooms SET room_number = ?, room_type = ?::room_type WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, room.getRoomNumber());
            statement.setString(2, room.getRoomType().name()); // Store the enum as a string
            statement.setLong(3, room.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        try {
            String sql = "DELETE FROM rooms WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        String query = "SELECT COUNT(*) FROM reservations WHERE room_id = ? " +
                "AND (check_in_date < ? AND check_out_date > ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setLong(1, roomId);
            stmt.setDate(2, java.sql.Date.valueOf(checkOutDate));
            stmt.setDate(3, java.sql.Date.valueOf(checkInDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            } else {
                return false;
            }
        }
    }

    private Room mapRowToRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getLong("id"),
                rs.getInt("room_number"),
                RoomType.valueOf(rs.getString("room_type"))
        );
    }
    public List<Room> findByRoomType(RoomType roomType) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ?::room_type";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.name().toUpperCase());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
        }
        return rooms;

    }


}
