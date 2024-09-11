package Repositories;

import Entities.Reservation;
import Entities.ReservationStatus;
import Entities.Room;
import Entities.User;
import Utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository implements GenericRepository<Reservation, Long> {
    private final Connection connection;

    public ReservationRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservations (user_id, room_id, reservation_status, check_in_date, check_out_date, total_price, special_requests) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, reservation.getUser().getId());
            statement.setLong(2, reservation.getRoom().getId());
            statement.setString(3, reservation.getReservationStatus().toString());
            statement.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            statement.setBigDecimal(6, reservation.getTotalPrice());
            statement.setString(7, reservation.getSpecialRequests());

            statement.executeUpdate();
        }
    }

    @Override
    public Reservation findById(Long id) throws SQLException {
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToReservation(resultSet);
            }
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() throws SQLException {
        String query = "SELECT * FROM reservations";
        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                reservations.add(mapResultSetToReservation(resultSet));
            }
        }
        return reservations;
    }

    @Override
    public void update(Reservation reservation, Long id) throws SQLException {
        String query = "UPDATE reservations SET user_id = ?, room_id = ?, reservation_status = ?, check_in_date = ?, check_out_date = ?, " +
                "total_price = ?, special_requests = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, reservation.getUser().getId());
            statement.setLong(2, reservation.getRoom().getId());
            statement.setString(3, reservation.getReservationStatus().toString());
            statement.setDate(4, Date.valueOf(reservation.getCheckInDate()));
            statement.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
            statement.setBigDecimal(6, reservation.getTotalPrice());
            statement.setString(7, reservation.getSpecialRequests());
            statement.setLong(8, id);

            statement.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }

    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {
        Long reservationId = resultSet.getLong("id");
        Long userId = resultSet.getLong("user_id");
        Long roomId = resultSet.getLong("room_id");
        String reservationStatusStr = resultSet.getString("reservation_status");
        LocalDate checkInDate = resultSet.getDate("check_in_date").toLocalDate();
        LocalDate checkOutDate = resultSet.getDate("check_out_date").toLocalDate();
        BigDecimal totalPrice = resultSet.getBigDecimal("total_price");
        String specialRequests = resultSet.getString("special_requests");

        User user = new UserRepository().findById(Math.toIntExact(userId));
        Room room = new RoomRepository().findById(roomId);
        ReservationStatus reservationStatus = ReservationStatus.valueOf(reservationStatusStr);

        return new Reservation(reservationId, user, room, reservationStatus, checkInDate, checkOutDate, totalPrice, specialRequests);
    }
}
