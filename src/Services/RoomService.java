package Services;

import Entities.Room;
import Repositories.RoomRepository;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void createRoom(Room room) throws SQLException {
        roomRepository.save(room);
    }

    public List<Room> getAllRooms() throws SQLException {
        return roomRepository.findAll();
    }

    public void updateRoomAvailability(Long roomId, boolean isAvailable) throws SQLException {
        roomRepository.updateAvailability(roomId, isAvailable);
    }

    public Optional<Room> getRoomById(Long roomId) {
        return null;
    }

    public void update(Room room) {
    }

    public void delete(Long roomId) {
    }
}
