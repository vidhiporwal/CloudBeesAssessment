package com.tms.dto;

public class TicketDto {
    private String seatSection;
    private Long seatId; 
    
    public TicketDto() {
       
    }

    public String getSeatSection() {
        return seatSection;
    }

    public void setSeatSection(String seatSection) {
        this.seatSection = seatSection;
    }
    
    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

   
}
