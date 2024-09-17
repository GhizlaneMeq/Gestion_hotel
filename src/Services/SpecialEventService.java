package Services;

import Entities.SpecialEvent;
import Repositories.SpecialEventRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SpecialEventService {
    private final SpecialEventRepository specialEventRepository;

    public SpecialEventService(SpecialEventRepository specialEventRepository) {
        this.specialEventRepository = specialEventRepository;
    }

    public void createEvent(SpecialEvent event) {
        try {
            specialEventRepository.save(event);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating special event", e);
        }
    }

    public List<SpecialEvent> getAllEvents() throws SQLException {
        return specialEventRepository.findAll();
    }

    public Optional<SpecialEvent> getEventById(Long eventId) throws SQLException {
        return specialEventRepository.findById(eventId);
    }

    public void updateEvent(SpecialEvent event) throws SQLException {
        specialEventRepository.update(event);

    }

    public void deleteEvent(Long eventId) throws SQLException {
        specialEventRepository.delete(eventId);

    }

}
