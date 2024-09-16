package Repositories;

import Entities.Room;
import Entities.RoomType;
import Utils.DatabaseConnection;

import java.math.BigDecimal;
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
            stmt.setString(2, room.getRoomType().name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                room.setId(rs.getLong("id"));
            }
        }
        return room;
    }

    @Override
    public void update(Room room) {
        try {
            String sql = "UPDATE rooms SET room_number = ?, room_type = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, room.getRoomNumber());
            statement.setString(2, room.getRoomType().name());
            statement.setLong(3, room.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public BigDecimal calculateRoomPrice(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        Room room = findById(roomId).orElseThrow(() -> new IllegalArgumentException("Room not found"));

        BigDecimal basePrice = getBasePrice(room.getRoomType(), checkInDate, checkOutDate);
        BigDecimal extraCharge = getEventExtraCharge(checkInDate, checkOutDate);

        return basePrice.add(extraCharge);
    }

    private BigDecimal getBasePrice(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        String sql = "SELECT base_price FROM room_pricing WHERE room_type = ? AND start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.name());
            stmt.setDate(2, Date.valueOf(checkInDate));
            stmt.setDate(3, Date.valueOf(checkOutDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("base_price");
            }
        }
        return BigDecimal.ZERO;
    }
    private BigDecimal getEventExtraCharge(LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        String sql = "SELECT SUM(extra_charge) AS total_extra FROM special_events WHERE start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(checkInDate));
            stmt.setDate(2, Date.valueOf(checkOutDate));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("total_extra");
            }
        }
        return BigDecimal.ZERO;
    }

    public List<Room> findByRoomType(RoomType roomType) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
        }
        return rooms;
    }
}
