package com.tms.service;

import java.util.List;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;

public interface TicketService {
	Ticket purchaseTicket(Ticket ticket) throws Exception;

	Ticket getTicketDetails(Long ticketId);

	List<Ticket> getUsersBySection(String section);

	Ticket modifyTicket(Long ticketId, TicketDto ticketRequest);

	List<Ticket> getTicketsBySectionAndUser(String section, Long userId);

	void removeUserAndTickets(Long userId);
}
