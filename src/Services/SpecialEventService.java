package Services;

import Entities.SpecialEvent;
import Repositories.SpecialEventRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class SpecialEventService {
    private SpecialEventRepository specialEventRepository;

    public SpecialEventService(SpecialEventRepository specialEventRepository) {
        this.specialEventRepository = specialEventRepository;
    }

    public void createEvent(SpecialEvent event) throws SQLException {
        specialEventRepository.save(event);
    }

    public List<SpecialEvent> getAllEvents() throws SQLException {
        return specialEventRepository.findAll();
    }

    public Optional<SpecialEvent> getEventById(Long eventId) {
        return null;
    }

    public void update(SpecialEvent event) {
    }

    public void delete(Long eventId) {
    }
}
