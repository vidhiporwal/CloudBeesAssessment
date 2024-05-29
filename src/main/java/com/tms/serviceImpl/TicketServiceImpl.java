package com.tms.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.dto.TicketDto;
import com.tms.entity.Ticket;
import com.tms.repository.TicketRepository;
import com.tms.repository.UserRepository;
import com.tms.service.TicketService;
import com.tms.entity.User;
import com.tms.exception.InvalidEmailException;
import com.tms.exception.SeatAlreadyBookedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TicketServiceImpl implements TicketService {
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private UserRepository userRepository;
	private static final String[] SEAT_SECTIONS = {"A", "B"};
    private static final int SEAT_ID_RANGE = 100;
	private boolean isValidEmail(String email) {

		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(emailRegex);
	}

	@Transactional
	public Ticket purchaseTicket(Ticket ticket) throws Exception {

		if (!isValidEmail(ticket.getUser().getEmail())) {

			throw new InvalidEmailException("Invalid email format: " + ticket.getUser().getEmail());
		}
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

		Optional<Ticket> existingTicket = ticketRepository.findBySeatIdAndSeatSection(ticket.getSeatId(),
				ticket.getSeatSection());
		if (existingTicket.isPresent()) {
			throw new SeatAlreadyBookedException("The seat " + ticket.getSeatId() + " in section "
					+ ticket.getSeatSection() + " is already booked.");
		}
		 String[] seatAssignment = assignRandomSeat();
		Ticket t = new Ticket();
		t.setFromLocation(ticket.getFromLocation());
		t.setToLocation(ticket.getToLocation());
		t.setUser(user);
		t.setPricePaid(ticket.getPricePaid());
		 t.setSeatSection(seatAssignment[0]);
	        t.setSeatId(Long.parseLong(seatAssignment[1]));
		ticketRepository.save(t);
		return t;
	}
	private String[] assignRandomSeat() throws Exception {
        Random random = new Random();

        while (true) {
            String seatSection = SEAT_SECTIONS[random.nextInt(SEAT_SECTIONS.length)];
            Long seatId = (long) (random.nextInt(SEAT_ID_RANGE) + 1);

            Optional<Ticket> existingTicket = ticketRepository.findBySeatIdAndSeatSection(seatId, seatSection);
            if (!existingTicket.isPresent()) {
                return new String[]{seatSection, seatId.toString()};
            }
        }
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
	public List<Ticket> getTicketsBySectionAndUser(String section, Long userId) {
		List<Ticket> ticketsInSectionAndUser = ticketRepository.findBySeatSectionAndUserId(section, userId);
		return ticketsInSectionAndUser;
	}

	@Transactional
	@Override
	public void removeUserAndTickets(Long userId) {

		ticketRepository.deleteByUserId(userId);

		userRepository.deleteById(userId);
	}

	@Override
	public Ticket modifyTicket(Long ticketId, TicketDto ticketRequest) {

		Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
		if (ticket != null) {

			Optional<Ticket> existingTicket = ticketRepository.findBySeatIdAndSeatSection(ticketRequest.getSeatId(),
					ticketRequest.getSeatSection());
			if (existingTicket.isPresent()) {

				return null;
			}

			// Update ticket details
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
