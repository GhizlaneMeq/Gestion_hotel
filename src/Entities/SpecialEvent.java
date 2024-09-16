package Entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SpecialEvent {
    private Long id;
    private String eventName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal extraCharge;

    public SpecialEvent() {}

    public SpecialEvent(Long id, String eventName, LocalDate startDate, LocalDate endDate, BigDecimal extraCharge) {
        this.id = id;
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.extraCharge = extraCharge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
        this.endDate = endDate;
    }

    public BigDecimal getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(BigDecimal extraCharge) {
        this.extraCharge = extraCharge;
    }
    @Override
    public String toString() {
        return "SpecialEvent{" +
                "id=" + id +
                ", eventName='" + eventName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", extraCharge=" + extraCharge +
                '}';
    }

}
