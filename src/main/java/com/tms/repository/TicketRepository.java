package com.tms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tms.entity.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findBySeatSection(String section);

	Optional<Ticket> findBySeatId(Long seatId);

	Optional<Ticket> findBySeatIdAndSeatSection(Long seatId, String seatSection);
}
