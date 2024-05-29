package com.tms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;
import com.tms.exception.InvalidEmailException;
import com.tms.exception.SeatAlreadyBookedException;
import com.tms.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody Ticket ticket) throws Exception {
        if (ticket.getFromLocation() == null || ticket.getToLocation() == null ||
            ticket.getUser() == null || ticket.getUser().getEmail() == null ||
            ticket.getPricePaid() == 0.0) {
            return ResponseEntity.badRequest().body("Please enter required parameters");
        }

        try {
            Ticket purchasedTicket = ticketService.purchaseTicket(ticket);
            return ResponseEntity.ok(purchasedTicket);
        } catch (SeatAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (InvalidEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    
    @GetMapping("/details")
    public ResponseEntity<?> getTicketDetails(@RequestParam("ticketId") Long ticketId) {
        if (ticketId == null) {
            return ResponseEntity.badRequest().body("The 'ticketId' is missing or empty.");
        }

        Ticket ticket = ticketService.getTicketDetails(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/section")
    public ResponseEntity<?> getUsersBySection(@RequestParam("section") String section,
                                                @RequestParam(value = "userId") Long userId) {
        if (section == null || section.isEmpty()) {
            return ResponseEntity.badRequest().body("The 'section' is missing or empty.");
        }

        List<Ticket> ticketsInSection;
        
            ticketsInSection = ticketService.getTicketsBySectionAndUser(section, userId);
        
        
        return ResponseEntity.ok(ticketsInSection);
    }



    @DeleteMapping("/remove")
    public ResponseEntity<?> removeUserAndTickets(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return ResponseEntity.badRequest().body("The 'userId' is missing or empty.");
        }

        ticketService.removeUserAndTickets(userId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/modifySeat")
    public ResponseEntity<?> modifyTicket(@RequestParam("ticketId") Long ticketId, 
                                                @RequestBody TicketDto ticketRequest) {
        if (ticketId == null) {
            return ResponseEntity.badRequest().body("The 'ticketId' is missing or empty.");
        }

        Ticket ticket = ticketService.modifyTicket(ticketId, ticketRequest);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

   

}
