package Services;

import Entities.Moderator;
import Repositories.ModeratorRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ModeratorService {
    private ModeratorRepository moderatorRepository;

    public ModeratorService(ModeratorRepository moderatorRepository) {
        this.moderatorRepository = moderatorRepository;
    }

    public void addModerator(Moderator moderator) throws SQLException {
        moderatorRepository.save(moderator);
    }

    public Optional<Moderator> getModeratorByUserId(Long userId) throws SQLException {
        return moderatorRepository.findByUserId(userId);
    }

    public Optional<Moderator> getModeratorById(Long id) throws SQLException {
        return moderatorRepository.findById(id);
    }

    public void delete(Long moderatorId) throws SQLException {
        moderatorRepository.delete(moderatorId);
    }

    public void update(Moderator moderator) throws SQLException {
        moderatorRepository.update(moderator, moderator.getId());
    }

    public List<Moderator> getAllModerators() throws SQLException {
        return moderatorRepository.findAll();
    }
}
