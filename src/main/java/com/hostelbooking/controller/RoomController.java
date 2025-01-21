package com.hostelbooking.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.Room;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.service.BookingService;
import com.hostelbooking.service.RoomService;

@Controller
@RequestMapping("/rooms")
public class RoomController {

	private RoomService roomService;

	private BookingService bookingService;

	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;
	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	public RoomController(RoomService roomService, BookingService bookingService, RoomRepository roomRepository,
			GuestRepository guestRepository) {
		this.roomService = roomService;
		this.bookingService = bookingService;
		this.roomRepository = roomRepository;
		this.guestRepository = guestRepository;
	}

	@GetMapping("/available-rooms")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'RECEPTIONIST')")
	public String showAvailableRooms(Model model) {
		List<Room> availableRooms = roomService.getAvailableRooms();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", availableRooms);
		model.addAttribute("guests", guests);
		return "rooms/available-rooms";
	}

	@GetMapping("/all")
	public List<Map<String, Object>> getAllRooms() {
		List<Room> rooms = roomRepository.findAll();
		return rooms.stream().map(room -> {
			Map<String, Object> roomDetails = new HashMap<>();
			roomDetails.put("id", room.getId());
			roomDetails.put("roomNumber", room.getRoomNumber());
			roomDetails.put("floor", room.getFloor());
			return roomDetails;
		}).collect(Collectors.toList());
	}

	@GetMapping("/change-room")
	public String showChangeRoomPage(Model model) {
		List<Room> availableRooms = roomService.getAvailableRooms();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", availableRooms);
		model.addAttribute("guests", guests);

		List<Room> rooms = roomService.getAllRooms();
		model.addAttribute("booking", new Booking());
		model.addAttribute("rooms", rooms);
		return "rooms/change-room";
	}

	@PostMapping("/change-room")
	public String changeRoom(@RequestParam(required = false) Long guestId, @RequestParam Long newRoomId,
			RedirectAttributes redirectAttributes) {
		try {
			// Retrieve the booking using the guestId
			Optional<Booking> bookingOptional = bookingRepository.findBookingByGuestId(guestId);

			if (bookingOptional.isPresent()) {
				Booking booking = bookingOptional.get();
				Long bookingId = booking.getId(); // Get the bookingId from the Booking object

				// Print bookingId and newRoomId for debugging
				System.out.println("Received Booking ID: " + bookingId + ", New Room ID: " + newRoomId);

				// Call the service to change the room
				bookingService.changeRoom(bookingId, newRoomId);

				// Add success message for the redirect
				redirectAttributes.addFlashAttribute("message", "Room successfully changed.");
			} else {
				// If booking is not found, set an error message
				redirectAttributes.addFlashAttribute("error", "No booking found for the given guest ID.");
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the full stack trace for debugging
			redirectAttributes.addFlashAttribute("error", "Error changing room: " + e.getMessage());
		}

		// Redirect to the change-room page
		return "redirect:/rooms/change-room";
	}

	// -----------------------------------

	@GetMapping("/view-rooms")
	@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'RECEPTIONIST')")
	public String viewRooms(Model model) {
		List<Integer> floors = roomRepository.findDistinctFloors();
		List<Room> allRooms = roomRepository.findAll();
		List<Guest> guests = guestRepository.findAll();
		model.addAttribute("availableRooms", allRooms);
		model.addAttribute("guests", guests);
		return "rooms/available-rooms";
	}

	// ------------------------------------

	@GetMapping("/get-booking-id-by-guest")
	@ResponseBody
	public Long getBookingIdByGuest(@RequestParam("guestId") Long guestId) {
		return bookingService.getBookingByGuestId(guestId).map(Booking::getId).orElse(null); // Return null if no
																								// booking found
	}

	// ---------------- Render Floor --------------------//

	@GetMapping("/floors")
	public ResponseEntity<List<Integer>> getFloors() {
		return ResponseEntity.ok(roomService.getUniqueFloors());
	}

	// ----------------- End ------------------------------//

}
