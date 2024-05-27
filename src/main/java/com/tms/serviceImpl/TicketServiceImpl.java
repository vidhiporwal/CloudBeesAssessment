package com.tms.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;
import com.tms.repository.TicketRepository;
import com.tms.repository.UserRepository;
import com.tms.service.TicketService;
import com.tms.entity.User;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Ticket purchaseTicket(Ticket ticket) {
    	User user = ticket.getUser();
        userRepository.save(user); 
        return ticketRepository.save(ticket);
    }
    
    @Override
    public Ticket getTicketDetails(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }
    
    @Override
    public List<Ticket> getUsersBySection(String section) {
        return ticketRepository.findBySeatSection(section);
    }
    
    @Override
    public void removeTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }
    
    @Override
    public Ticket modifyTicket(Long ticketId, TicketDto ticketRequest) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if (ticket != null) {
            
            if (ticketRequest.getSeatSection() != null) {
                ticket.setSeatSection(ticketRequest.getSeatSection());
            }
            if (ticketRequest.getSeatId() != null) {
                ticket.setSeatId(ticketRequest.getSeatId());
            }
            
            return ticketRepository.save(ticket);
        }
        return null;
    }
}
