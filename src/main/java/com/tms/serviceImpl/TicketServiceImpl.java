package com.tms.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;
import com.tms.repository.TicketRepository;
import com.tms.repository.UserRepository;
import com.tms.service.TicketService;
import com.tms.entity.User;
import com.tms.exception.SeatAlreadyBookedException;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public Ticket purchaseTicket(Ticket ticket) {
        
        Optional<User> existingUser = userRepository.findByEmail(ticket.getUser().getEmail());
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            user = new User();
            user.setFirstName(ticket.getUser().getFirstName());
            user.setLastName(ticket.getUser().getLastName());
            user.setEmail(ticket.getUser().getEmail());
            userRepository.save(user);
        }

        
        Optional<Ticket> existingTicket = ticketRepository.findBySeatIdAndSeatSection(ticket.getSeatId(), ticket.getSeatSection());
        if (existingTicket.isPresent()) {
            throw new SeatAlreadyBookedException("The seat " + ticket.getSeatId() + " in section " + ticket.getSeatSection() + " is already booked.");
        }

        
        Ticket t = new Ticket();
        t.setFromLocation(ticket.getFromLocation());
        t.setToLocation(ticket.getToLocation());
        t.setUser(user);
        t.setPricePaid(ticket.getPricePaid());
        t.setSeatSection(ticket.getSeatSection());
        t.setSeatId(ticket.getSeatId());
        ticketRepository.save(t);
        return t;
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
