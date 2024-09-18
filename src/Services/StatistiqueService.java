package Services;

import Entities.Reservation;
import Entities.Room;
import Entities.RoomType;
import Entities.ReservationStatus;
import Repositories.ReservationRepository;
import Repositories.RoomRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class StatistiqueService {

    private ReservationRepository reservationRepository;
    private RoomRepository roomRepository;


    public StatistiqueService(ReservationRepository reservationRepository, RoomRepository roomRepository) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    public double calculateOccupancyRate(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Reservation> reservations = getReservationsWithinPeriod(startDate, endDate);
        long totalReservedNights = reservations.stream()
                .mapToLong(res -> res.getCheckOutDate().toEpochDay() - res.getCheckInDate().toEpochDay())
                .sum();

        long totalAvailableRoomNights = roomRepository.findAll().size() * (endDate.toEpochDay() - startDate.toEpochDay());

        return totalAvailableRoomNights == 0 ? 0 : (double) totalReservedNights / totalAvailableRoomNights * 100;
    }

    public BigDecimal calculateTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Reservation> reservations = getReservationsWithinPeriod(startDate, endDate);
        return reservations.stream()
                .filter(res -> res.getReservationStatus() == ReservationStatus.Confirmed)
                .map(Reservation::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countCanceledReservations(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Reservation> reservations = getReservationsWithinPeriod(startDate, endDate);
        return reservations.stream()
                .filter(res -> res.getReservationStatus() == ReservationStatus.Canceled)
                .count();
    }

    public List<Room> getMostReservedRooms(int topN) throws SQLException {
        List<Reservation> reservations = reservationRepository.findAll();

        Map<Room, Long> roomReservationCount = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getRoom, Collectors.counting()));

        return roomReservationCount.entrySet().stream()
                .sorted(Map.Entry.<Room, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public Map<RoomType, BigDecimal> calculateRevenuePerRoomType(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Reservation> reservations = getReservationsWithinPeriod(startDate, endDate);
        return reservations.stream()
                .filter(res -> res.getReservationStatus() == ReservationStatus.Confirmed)
                .filter(res -> res.getRoom().getRoomType() != null)
                .collect(Collectors.groupingBy(
                        res -> res.getRoom().getRoomType(),
                        Collectors.reducing(BigDecimal.ZERO, Reservation::getTotalPrice, BigDecimal::add)
                ));
    }



    public Map<RoomType, Long> countCurrentlyReservedRoomsPerType() throws SQLException {
        LocalDate today = LocalDate.now();
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .filter(res -> res.getReservationStatus() == ReservationStatus.Confirmed &&
                        !res.getCheckOutDate().isBefore(today))
                .collect(Collectors.groupingBy(
                        res -> res.getRoom().getRoomType(),
                        Collectors.counting()
                ));
    }

    private List<Reservation> getReservationsWithinPeriod(LocalDate startDate, LocalDate endDate) throws SQLException {
        return reservationRepository.findAll().stream()
                .filter(res -> !res.getCheckInDate().isAfter(endDate) &&
                        !res.getCheckOutDate().isBefore(startDate))
                .collect(Collectors.toList());
    }
}
