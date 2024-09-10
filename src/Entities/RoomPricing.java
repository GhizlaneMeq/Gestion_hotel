package Entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RoomPricing {
    private Long id;
    private RoomType roomType;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal basePrice;

    public RoomPricing() {
    }

    public RoomPricing(Long id, RoomType roomType, LocalDate startDate, LocalDate endDate, BigDecimal basePrice) {
        this.id = id;
        this.roomType = roomType;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
