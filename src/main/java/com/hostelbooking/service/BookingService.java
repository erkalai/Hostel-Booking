package com.hostelbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.dto.BookingReportDTO;
import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.entity.Room;
import com.hostelbooking.entity.User;
import com.hostelbooking.enums.GuestStatus;
import com.hostelbooking.repository.BookingRepository;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.repository.ProgramRepository;
import com.hostelbooking.repository.RoomRepository;
import com.hostelbooking.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private RoomRepository roomRepository;
	private GuestRepository guestRepository;
	private final RoomService roomService;
	private UserRepository userRepository;
	private ProgramRepository programRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
			RoomRepository roomRepository, GuestRepository guestRepository, RoomService roomService) {
		this.bookingRepository = bookingRepository;
		this.roomRepository = roomRepository;
		this.guestRepository = guestRepository;
		this.roomService = roomService;
		this.userRepository = userRepository;
	}

	@Transactional
	public Booking checkIn(Guest guest, Long roomId, Long userId) {

		System.out.println("Guest details: " + guest.getName() + ", Room ID: " + roomId + ", User ID: " + userId);

		Room room = roomService.updateRoomCapacity(roomId, -1);

		System.out.println("************************************** : After Room" + room);
		String programName = guest.getProgramName();
		System.out.println("Program Name: " + programName);

//        Program program = programRepository.findByNameNative(guest.getProgramName())
//                .orElseThrow(() -> new RuntimeException("Program not found: " + guest.getProgramName()));
//
//
//
//        guest.setProgram(program);

		guest.setRoom(room);
		Guest savedGuest = guestRepository.save(guest);
		System.out.println("Guest saved with ID: " + savedGuest.getId());

		User user;
		try {
			user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
			System.out.println("User found with ID: " + user.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching user: " + e.getMessage());
		}

		// Create a new booking
		Booking booking = new Booking();
		booking.setGuest(savedGuest);
		booking.setRoom(room);
		booking.setCheckInDate(LocalDate.now());
		booking.setCheckInTime(LocalDateTime.now());
		booking.setStatus(GuestStatus.CHECK_IN);
		booking.setCheckInUser(user);

		return bookingRepository.save(booking);
	}

	@Transactional
	public Booking checkOut(Long guestId, Long userId) {
		List<GuestStatus> statuses = List.of(GuestStatus.CHECK_IN);
		Booking booking = bookingRepository.findByGuestIdAndStatusInOrderByStatusAsc(guestId, statuses)
				.orElseThrow(() -> new RuntimeException("Active booking not found for the guest"));

		roomService.updateRoomCapacity(booking.getRoom().getId(), 1);

		booking.setCheckOutDate(LocalDate.now());
		booking.setCheckOutTime(LocalDateTime.now());
		booking.setStatus(GuestStatus.CHECK_OUT);

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		booking.setCheckOutUser(user);

		return bookingRepository.save(booking);
	}

	@Transactional
	public void changeRoom(Long bookingId, Long newRoomId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		System.out.println("Fetched Booking: " + booking);

		Long oldRoomId = booking.getRoom().getId();
		roomService.updateRoomCapacity(oldRoomId, 1);
		System.out.println("Freed Old Room ID: " + oldRoomId);

		Room newRoom = roomService.updateRoomCapacity(newRoomId, -1);
		System.out.println("Allocated New Room ID: " + newRoomId);

		booking.setRoom(newRoom);
		booking.setStatus(GuestStatus.CHECK_IN);

		bookingRepository.save(booking);
		System.out.println("Updated Booking Saved: " + booking);

		Guest guest = booking.getGuest();
		guest.setRoom(newRoom);
		guestRepository.save(guest);
		System.out.println("Updated Guest with New Room: " + guest);
	}

	public Optional<Booking> getBookingByGuestId(Long guestId) {
		return bookingRepository.findBookingByGuestId(guestId);
	}
//   / 
	// ------------- Report Start --------------- //

	public List<BookingReportDTO> getFilteredBookings(LocalDate startDate, LocalDate endDate, Integer month,
			Integer year) {
		List<Object[]> result = bookingRepository.filterBookings(startDate, endDate, month, year);
		return result.stream().map(objects -> new BookingReportDTO((Long) objects[0], // bookingId
				(String) objects[1], (String) objects[2], (LocalDate) objects[3], (LocalDateTime) objects[4],
				(LocalDate) objects[5], (LocalDateTime) objects[6], (GuestStatus) objects[7]))
				.collect(Collectors.toList());
	}

	// ------------------ End -------------------------//

}
