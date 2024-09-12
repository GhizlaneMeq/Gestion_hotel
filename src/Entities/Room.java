package Entities;

public class Room {
    private Long id;
    private Integer roomNumber;
    private RoomType roomType;
    private Boolean available;

    public Room() {
    }

    public Room(Long id, Integer roomNumber, RoomType roomType, Boolean available) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
