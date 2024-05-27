package com.tms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;
import com.tms.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @Autowired
    private ObjectMapper objectMapper;

    private TicketDto ticketDto;
    private Ticket ticket;

    @BeforeEach
    void setUp() {
        ticketDto = new TicketDto();
        ticketDto.setSeatSection("A");
        ticketDto.setSeatId(123L);

        ticket = new Ticket();
        ticket.setId(1L);
        ticket.setSeatSection("A");
        ticket.setSeatId(123L);
    }

    @Test
    public void testPurchaseTicket() throws Exception {
        when(ticketService.purchaseTicket(any(Ticket.class))).thenReturn(ticket);

        mockMvc.perform(post("/tickets/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.seatSection").value("A"))
                .andExpect(jsonPath("$.seatId").value(123));
    }

    @Test
    public void testGetTicketDetails() throws Exception {
        Long ticketId = 1L;
        when(ticketService.getTicketDetails(ticketId)).thenReturn(ticket);

        mockMvc.perform(get("/tickets/details")
                .param("ticketId", String.valueOf(ticketId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.seatSection").value("A"))
                .andExpect(jsonPath("$.seatId").value(123));
    }

    @Test
    public void testGetUsersBySection() throws Exception {
        String section = "A";
        when(ticketService.getUsersBySection(section)).thenReturn(Collections.singletonList(ticket));

        mockMvc.perform(get("/tickets/section")
                .param("section", section))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].seatSection").value("A"))
                .andExpect(jsonPath("$[0].seatId").value(123));
    }

    @Test
    public void testRemoveTicket() throws Exception {
        Long ticketId = 1L;
        mockMvc.perform(delete("/tickets/remove")
                .param("ticketId", String.valueOf(ticketId)))
                .andExpect(status().isNoContent());

        verify(ticketService, times(1)).removeTicket(ticketId);
    }

    @Test
    public void testModifyTicket() throws Exception {
        Long ticketId = 1L;
        when(ticketService.modifyTicket(anyLong(), any(TicketDto.class))).thenReturn(ticket);

        mockMvc.perform(put("/tickets/modifySeat")
                .param("ticketId", String.valueOf(ticketId))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.seatSection").value("A"))
                .andExpect(jsonPath("$.seatId").value(123));
    }
    
}
