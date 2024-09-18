package Repositories;

import Entities.Reservation;
import Entities.ReservationStatus;
import Entities.Room;
import Entities.User;
import Utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationRepository implements BaseRepository<Reservation> {
    private final Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public Reservation save(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservations (user_id, room_id, reservation_status, check_in_date, check_out_date, total_price) " +
                "VALUES (?, ?, CAST(? AS reservation_status), ?, ?, ?) RETURNING id";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, reservation.getUser().getId());
            stmt.setLong(2, reservation.getRoom().getId());
            stmt.setString(3, reservation.getReservationStatus().name());
            stmt.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            stmt.setBigDecimal(6, reservation.getTotalPrice());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                reservation.setId(rs.getLong("id"));
            }
        }
        return reservation;
    }

    @Override
    public Optional<Reservation> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToReservation(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        }
        return reservations;
    }

    @Override
    public boolean update(Reservation reservation) throws SQLException {
        String sql = "UPDATE reservations SET check_in_date = ?, check_out_date = ?, room_id = ?, total_price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(2, Date.valueOf(reservation.getCheckOutDate()));
            statement.setLong(3, reservation.getRoom().getId());
            statement.setBigDecimal(4, reservation.getTotalPrice());
            statement.setLong(5, reservation.getId());
            return statement.executeUpdate() > 0;
        }
    }


    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Reservation> findByRoomId(Long roomId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE room_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        }
        return reservations;
    }

    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getLong("id"),
                new User(rs.getLong("user_id"), null, null, null, null),
                new Room(rs.getLong("room_id"), null, null),
                ReservationStatus.valueOf(rs.getString("reservation_status")),
                rs.getDate("check_in_date").toLocalDate(),
                rs.getDate("check_out_date").toLocalDate(),
                rs.getBigDecimal("total_price")
        );
    }

    public List<Reservation> findByUserId(Long userId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
        }
        return reservations;
    }

}
