package com.hostelbooking.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Program;
import com.hostelbooking.entity.Room;

import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.User;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.repository.UserRepository;
import com.hostelbooking.service.BookingService;
import com.hostelbooking.service.GuestService;
import com.hostelbooking.service.ProgramService;
import com.hostelbooking.service.RoomService;
import com.hostelbooking.service.TemporaryPreBookingService;

@Controller
@RequestMapping("/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private GuestService guestService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private GuestRepository guestRepository;

	@Autowired
	private ProgramService programService;

	@Autowired
	private TemporaryPreBookingService temporaryPreBookingService;

	// Show Check In Form
	@GetMapping("/checkin")
	public String showCheckInPage(Model model) {

		// Fetch all programs
		List<Program> allPrograms = programService.findAllPrograms();

		List<Integer> floors = roomRepository.findDistinctFloors();
		model.addAttribute("floors", floors);

		List<Program> starredPrograms = allPrograms.stream().filter(Program::getIsStarred).collect(Collectors.toList());

		List<Program> otherPrograms = allPrograms.stream().filter(program -> !program.getIsStarred())
				.collect(Collectors.toList());

		// Add to model
		model.addAttribute("starredPrograms", starredPrograms);
		model.addAttribute("otherPrograms", otherPrograms);

		model.addAttribute("guest", new Guest());
		model.addAttribute("availableRooms", roomService.getAvailableRooms());
		return "bookings/checkin";
	}

	@PostMapping("/checkin")
	public String checkIn(@RequestParam String name, @RequestParam String idType, @RequestParam String idNumber,
			@RequestParam String programName, @RequestParam String mobileNumber, @RequestParam String kid,
			@RequestParam String foodType, @RequestParam String coffeeTime, @RequestParam Long roomId,

			RedirectAttributes redirectAttributes, Principal principal) {

		try {

			// Get logged-in user's ID
			User user = userRepository.findByEmail(principal.getName());

			Long userId = user.getId();

			System.out.println("User Id : **********************************" + userId);
			System.out.println("User Id : **********************************" + userId);

			// Create a new guest instance
			Guest guest = new Guest();
			guest.setName(name);
			guest.setIdType(idType);
			guest.setIdNumber(idNumber);
			guest.setProgramName(programName);
			guest.setMobileNumber(mobileNumber);
			guest.setMobileNumber(mobileNumber);
			guest.setKid(kid);
			guest.setFoodType(foodType);
			guest.setCoffeeTime(coffeeTime);

			// Call the service to check in the guest
			bookingService.checkIn(guest, roomId, userId);

			// Remove the guest from the temporary table
			temporaryPreBookingService.deleteByKid(kid);
			// Add a success message
			redirectAttributes.addFlashAttribute("message", "Guest successfully checked in.");
		} catch (Exception e) {
			// Handle errors and add an error message
			redirectAttributes.addFlashAttribute("error", "Error during check-in: " + e.getMessage());
		}

		// Redirect back to the check-in page
		return "redirect:/bookings/checkin";
	}

	@GetMapping("/checkout")
	public String showCheckoutForm(Model model) {

		// Fetch all distinct floors
		List<Integer> floors = roomRepository.findDistinctFloors();
		// Fetch all rooms
		List<Room> rooms = roomRepository.findAll();

		List<Room> bookedRooms = roomService.getAllRooms();
		List<Guest> guests = guestRepository.findAll();

		model.addAttribute("floors", floors);
		model.addAttribute("rooms", rooms);

//	        model.addAttribute("rooms", bookedRooms);
		model.addAttribute("guests", guests);
		model.addAttribute("floors", floors);

		System.out.println(floors);
		return "bookings/checkout";
	}

	@PostMapping("/checkout")
	public RedirectView checkOut(@RequestParam Long guestId, RedirectAttributes attributes, Principal principal) {
		try {

			User user = userRepository.findByEmail(principal.getName());

			Long userId = user.getId();

			// Call service to handle the business logic for checking out
			Booking booking = bookingService.checkOut(guestId, userId);

			// Add booking details to attributes for redirection
			attributes.addFlashAttribute("booking", booking);

			// Redirect to the checkout page
			return new RedirectView("/booking/checkout");
		} catch (RuntimeException e) {
			// Handle the error and redirect to an error page or show error
			attributes.addFlashAttribute("error", e.getMessage());
			return new RedirectView("/error");
		}
	}

	@GetMapping("/guests/by-room")
	@ResponseBody
	public List<Guest> getGuestsByRoom(@RequestParam Long roomId, Model model) {
		System.out.println("Fetching guests for Room ID: " + roomId);
		List<Guest> guests = bookingRepository.findGuestsByRoomIdAndStatus(roomId);

		System.out.println("Get Guest " + guests);
		model.addAttribute("guests", guests);
		return guests; // Points to bookings/guests/by-room.html
	}

	@GetMapping("/get-booking-id-by-guest")
	@ResponseBody
	public Optional<Booking> getBookingIdByGuest(@RequestParam("guestId") Long guestId) {
		// Fetch the booking ID using the guest ID
		Optional<Booking> booking = bookingRepository.findBookingByGuestId(guestId);

		System.out.println("Booking Details : " + booking.get());
		return booking;
	}

}
