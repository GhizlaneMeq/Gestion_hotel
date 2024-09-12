package Services;

import Entities.Reservation;
import Repositories.ReservationRepository;

import java.util.List;

public class ReservationService {
    public ReservationService(ReservationRepository reservationRepository) {
    }

    public List<Reservation> getReservationsByUserId(Long id) {
        return null;
    }

    public void create(Reservation reservation) {
    }

    public void cancel(Long reservationId) {
    }
}
