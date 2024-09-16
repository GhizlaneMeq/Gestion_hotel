package Repositories;

import Entities.SpecialEvent;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpecialEventRepository {

    private final Connection connection;

    public SpecialEventRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void save(SpecialEvent event) throws SQLException {
        String query = "INSERT INTO specialEvents (event_name, start_date, end_date, extra_charge) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, event.getEventName());
            statement.setDate(2, Date.valueOf(event.getStartDate()));
            statement.setDate(3, Date.valueOf(event.getEndDate()));
            statement.setBigDecimal(4, event.getExtraCharge());
            statement.executeUpdate();
        }
    }

    public List<SpecialEvent> findAll() throws SQLException {
        String query = "SELECT * FROM specialEvents";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<SpecialEvent> events = new ArrayList<>();
            while (resultSet.next()) {
                SpecialEvent event = new SpecialEvent(
                        resultSet.getLong("id"),
                        resultSet.getString("event_name"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getBigDecimal("extra_charge")
                );
                events.add(event);
            }
            return events;
        }
    }

    public Optional<SpecialEvent> findById(Long id) throws SQLException {
        String query = "SELECT * FROM specialEvents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    SpecialEvent event = new SpecialEvent(
                            resultSet.getLong("id"),
                            resultSet.getString("event_name"),
                            resultSet.getDate("start_date").toLocalDate(),
                            resultSet.getDate("end_date").toLocalDate(),
                            resultSet.getBigDecimal("extra_charge")
                    );
                    return Optional.of(event);
                }
                return Optional.empty();
            }
        }
    }

    public void update(SpecialEvent event) throws SQLException {
        String query = "UPDATE specialEvents SET event_name = ?, start_date = ?, end_date = ?, extra_charge = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, event.getEventName());
            statement.setDate(2, Date.valueOf(event.getStartDate()));
            statement.setDate(3, Date.valueOf(event.getEndDate()));
            statement.setBigDecimal(4, event.getExtraCharge());
            statement.setLong(5, event.getId());
            statement.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM specialEvents WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
