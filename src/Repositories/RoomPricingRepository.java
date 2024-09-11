package Repositories;

import Entities.Room;
import Entities.RoomPricing;
import Entities.RoomType;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomPricingRepository implements GenericRepository<RoomPricing, Long>{
    private final Connection connection;

    public RoomPricingRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(RoomPricing roomPricing) throws SQLException {
        String query = "INSERT INTO room_pricing (room_type, start_date, end_date, base_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomPricing.getRoomType().name());
            statement.setDate(2, Date.valueOf(roomPricing.getStartDate()));
            statement.setDate(3, Date.valueOf(roomPricing.getEndDate()));
            statement.setBigDecimal(4, roomPricing.getBasePrice());
            statement.executeUpdate();
        }
    }

    @Override
    public RoomPricing findById(Long id) throws SQLException {
        String query = "SELECT id, room_type, start_date, end_date, base_price FROM room_pricing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new RoomPricing(
                        resultSet.getLong("id"),
                        RoomType.valueOf(resultSet.getString("room_type")),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getBigDecimal("base_price")
                );
            } else {
                return null;
            }
        }
    }

    @Override
    public List<RoomPricing> findAll() throws SQLException {
        String query = "SELECT id, room_type, start_date, end_date, base_price FROM room_pricing";
        List<RoomPricing> roomPricings = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                roomPricings.add(new RoomPricing(
                        resultSet.getLong("id"),
                        RoomType.valueOf(resultSet.getString("room_type")),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getBigDecimal("base_price")
                ));
            }
        }
        return roomPricings;
    }

    @Override
    public void update(RoomPricing roomPricing, Long id) throws SQLException {
        String query = "UPDATE room_pricing SET room_type = ?, start_date = ?, end_date = ?, base_price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, roomPricing.getRoomType().name());
            statement.setDate(2, Date.valueOf(roomPricing.getStartDate()));
            statement.setDate(3, Date.valueOf(roomPricing.getEndDate()));
            statement.setBigDecimal(4, roomPricing.getBasePrice());
            statement.setLong(5, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM room_pricing WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
