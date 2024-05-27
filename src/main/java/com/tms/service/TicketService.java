package com.tms.service;

import java.util.List;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;

public interface TicketService {
	Ticket purchaseTicket(Ticket ticket);

	Ticket getTicketDetails(Long ticketId);

	List<Ticket> getUsersBySection(String section);

	void removeTicket(Long ticketId);

	Ticket modifyTicket(Long ticketId, TicketDto ticketRequest);
}
