package Services;

import Entities.RoomPricing;
import Entities.RoomType;
import Repositories.RoomPricingRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomPricingService {

    private final RoomPricingRepository roomPricingRepository;
    private final SpecialEventService specialEventService;

    public RoomPricingService(RoomPricingRepository roomPricingRepository, SpecialEventService specialEventService) {
        this.roomPricingRepository = roomPricingRepository;
        this.specialEventService = specialEventService;
    }

    public void addRoomPricing(RoomPricing roomPricing) throws SQLException {
        roomPricingRepository.save(roomPricing);
    }

    public List<RoomPricing> getAllRoomPricing() throws SQLException {
        return roomPricingRepository.findAll();
    }

    public void removeRoomPricing(Long id) throws SQLException {
        roomPricingRepository.delete(id);
    }

    public RoomPricing getRoomPricing(Long id) throws SQLException {
        Optional<RoomPricing> roomPricing = roomPricingRepository.findById(id);
        return roomPricing.orElse(null);
    }

   /* public BigDecimal calculatePrice(RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        List<RoomPricing> pricings = roomPricingRepository.findByRoomType(roomType);

        BigDecimal totalPrice = BigDecimal.ZERO;
        LocalDate currentDate = checkInDate;

        while (currentDate.isBefore(checkOutDate)) {
            final LocalDate finalCurrentDate = currentDate;
            RoomPricing pricing = pricings.stream()
                    .filter(p -> !finalCurrentDate.isBefore(p.getStartDate()) && !finalCurrentDate.isAfter(p.getEndDate()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No pricing found for date: " + finalCurrentDate));

            totalPrice = totalPrice.add(pricing.getBasePrice());

            currentDate = currentDate.plusDays(1);
        }

        BigDecimal eventCharge = specialEventService.calculateEventCharge(checkInDate, checkOutDate);
        return totalPrice.add(eventCharge);
    }*/

    public List<RoomPricing> findByRoomType(RoomType roomType) throws SQLException {
        return roomPricingRepository.findByRoomType(roomType);
    }
}
