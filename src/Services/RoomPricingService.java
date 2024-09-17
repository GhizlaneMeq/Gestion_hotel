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

    public List<RoomPricing> findByRoomType(RoomType roomType) throws SQLException {
        return roomPricingRepository.findByRoomType(roomType);
    }
}
