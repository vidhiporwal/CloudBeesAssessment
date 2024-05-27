package com.tms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.tms.service.TicketService;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    
    @PostMapping("/purchase")
    public ResponseEntity<Ticket> purchaseTicket(@RequestBody Ticket ticket) {
        Ticket purchasedTicket = ticketService.purchaseTicket(ticket);
        return ResponseEntity.ok(purchasedTicket);
    }
    
    @GetMapping("/details")
    public ResponseEntity<Ticket> getTicketDetails(@RequestParam("ticketId") Long ticketId) {
        Ticket ticket = ticketService.getTicketDetails(ticketId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    
    @GetMapping("/section")
    public ResponseEntity<List<Ticket>> getUsersBySection(@RequestParam("section") String section) {
        List<Ticket> tickets = ticketService.getUsersBySection(section);
        return ResponseEntity.ok(tickets);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeTicket(@RequestParam("ticketId") Long ticketId) {
        ticketService.removeTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modifySeat")
    public ResponseEntity<Ticket> modifyTicket(@RequestParam("ticketId") Long ticketId, 
                                                @RequestBody TicketDto ticketRequest) {
        Ticket ticket = ticketService.modifyTicket(ticketId, ticketRequest);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
   

}
