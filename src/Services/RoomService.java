package Services;

import Entities.*;
import Repositories.RoomRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class RoomService {
    private RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void create(Room room) throws SQLException {
        roomRepository.save(room);
    }

    public Optional<Room> getById(Long id) throws SQLException {
        return roomRepository.findById(id);
    }

    public List<Room> getAll() throws SQLException {
        return roomRepository.findAll();
    }

    public void update(Room room) throws SQLException {
        roomRepository.update(room);
    }

    public void delete(Long id) throws SQLException {
        roomRepository.delete(id);
    }

    public boolean isAvailable(Long roomId, LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        return roomRepository.isAvailable(roomId, checkInDate, checkOutDate);
    }

    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        return null;
    }

    public List<Room> getRoomsByType(RoomType roomType) throws SQLException {
        return roomRepository.findByRoomType(roomType);
    }
}
