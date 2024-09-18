package Services;

import Entities.Reservation;
import Entities.Room;
import Entities.RoomType;
import Repositories.ReservationRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getReservationsByUserId(Long userId) throws SQLException {
        return reservationRepository.findByUserId(userId);
    }

    public boolean create(Reservation reservation) throws SQLException {
        return reservationRepository.save(reservation) != null;
    }

    public boolean cancel(Long reservationId) throws SQLException {
        try {
            reservationRepository.delete(reservationId);
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to cancel reservation: " + e.getMessage());
            return false;
        }
    }

    public List<Reservation> getReservationsByRoomId(Long roomId) throws SQLException {
        return reservationRepository.findByRoomId(roomId);
    }
    public boolean update(Long reservationId, LocalDate newCheckInDate, LocalDate newCheckOutDate, RoomType newRoomType) throws SQLException {
        return false;
    }

}
