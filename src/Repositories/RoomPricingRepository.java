package Repositories;

import Entities.RoomPricing;
import Entities.RoomType;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomPricingRepository implements BaseRepository<RoomPricing> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public RoomPricing save(RoomPricing pricing) throws SQLException {
        String sql = "INSERT INTO room_pricing (room_type, base_price) VALUES (?::room_type, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pricing.getRoomType().name());
            stmt.setBigDecimal(2, pricing.getBasePrice());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pricing.setId(rs.getLong("id"));
            }
        }
        return pricing;
    }

    @Override
    public Optional<RoomPricing> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM room_pricing WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToRoomPricing(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<RoomPricing> findAll() throws SQLException {
        String sql = "SELECT * FROM room_pricing";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            List<RoomPricing> pricings = new ArrayList<>();
            while (rs.next()) {
                pricings.add(mapRowToRoomPricing(rs));
            }
            return pricings;
        }
    }

    @Override
    public boolean update(RoomPricing pricing) throws SQLException {
        String sql = "UPDATE room_pricing SET room_type = ?,base_price = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pricing.getRoomType().name());
            stmt.setBigDecimal(4, pricing.getBasePrice());
            stmt.setLong(5, pricing.getId());
            stmt.executeUpdate();
        }
        return false;
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM room_pricing WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private RoomPricing mapRowToRoomPricing(ResultSet rs) throws SQLException {
        return new RoomPricing(
                rs.getLong("id"),
                RoomType.valueOf(rs.getString("room_type")),
                rs.getBigDecimal("base_price")
        );
    }

    public List<RoomPricing> findByRoomType(RoomType roomType) throws SQLException {
        String sql = "SELECT * FROM room_pricing WHERE room_type = ?::room_type";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, roomType.name());
            ResultSet rs = stmt.executeQuery();
            List<RoomPricing> pricings = new ArrayList<>();
            while (rs.next()) {
                pricings.add(mapRowToRoomPricing(rs));
            }
            return pricings;
        }
    }
}
