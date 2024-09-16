package Entities;

import java.math.BigDecimal;

public class RoomPricing {
    private Long id;
    private RoomType roomType;
    private BigDecimal basePrice;

    public RoomPricing() {}

    public RoomPricing(Long id, RoomType roomType, BigDecimal basePrice) {
        this.id = id;
        this.roomType = roomType;
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        if (basePrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Base price must be positive");
        }
        this.basePrice = basePrice;
    }
}
