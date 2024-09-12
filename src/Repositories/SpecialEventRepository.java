package Repositories;

import Entities.SpecialEvent;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpecialEventRepository implements GenericRepository<SpecialEvent, Long> {
    private final Connection connection;

    public SpecialEventRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(SpecialEvent event) throws SQLException {
        String query = "INSERT INTO special_events (event_name, start_date, end_date, extra_charge) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, event.getEventName());
            statement.setDate(2, Date.valueOf(event.getStartDate()));
            statement.setDate(3, Date.valueOf(event.getEndDate()));
            statement.setBigDecimal(4, event.getExtraCharge());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                event.setId(keys.getLong(1));
            }
        }
    }

    @Override
    public SpecialEvent findById(Long id) throws SQLException {
        String query = "SELECT id, event_name, start_date, end_date, extra_charge FROM special_event WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new SpecialEvent(
                        resultSet.getLong("id"),
                        resultSet.getString("event_name"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        resultSet.getBigDecimal("extra_charge")
                );
            } else {
                return null;
            }
        }

    }

    @Override
    public List<SpecialEvent> findAll() throws SQLException {
        String query = "SELECT * FROM special_events";
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
    @Override
    public void update(SpecialEvent specialEvent, Long id) throws SQLException {
        String query = "UPDATE special_event SET event_name = ?, start_date = ?, end_date = ?, extra_charge = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, specialEvent.getEventName());
            statement.setDate(2, Date.valueOf(specialEvent.getStartDate()));
            statement.setDate(3, Date.valueOf(specialEvent.getEndDate()));
            statement.setBigDecimal(4, specialEvent.getExtraCharge());
            statement.setLong(5, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM special_event WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }
}
