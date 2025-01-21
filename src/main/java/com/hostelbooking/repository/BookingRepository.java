package com.hostelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.dto.BookingReportDTO;
import com.hostelbooking.entity.Booking;
import com.hostelbooking.entity.Guest;
import com.hostelbooking.enums.GuestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Optional<Booking> findByGuestIdAndStatusInOrderByStatusAsc(Long guestId, List<GuestStatus> statuses);

//	@Query("SELECT b.guest FROM Booking b JOIN FETCH b.guest WHERE b.room.id = :roomId AND b.status = 'CHECK_IN'")
//	List<Guest> findGuestsByRoomIdAndStatus(@Param("roomId") Long roomId);

	@Query("SELECT b.guest FROM Booking b WHERE b.room.id = :roomId AND b.status = 'CHECK_IN'")
	List<Guest> findGuestsByRoomIdAndStatus(@Param("roomId") Long roomId);

	@Query("SELECT b FROM Booking b WHERE b.guest.id = :guestId")
	Optional<Booking> findBookingByGuestId(Long guestId);
//	
//	@Query("SELECT b FROM Booking b WHERE b.guest.guestId = :guestId")
//    Optional<Booking> findBookingByGuestId(Long guestId);

	// -------------------------------- New Report -----------------------//

	@Query("SELECT b.id, g.name, r.roomNumber, b.checkInDate, b.checkInTime, b.checkOutDate, b.checkOutTime, b.status "
			+ "FROM Booking b " + "JOIN b.guest g " + "JOIN b.room r " + "WHERE " + "(b.status = 'CHECK_IN' "
			+ "OR (b.status = 'CHECK_OUT' AND :startDate IS NOT NULL AND :endDate IS NOT NULL)) "
			+ "AND (:startDate IS NULL OR b.checkInDate >= :startDate OR b.checkOutDate >= :startDate) "
			+ "AND (:endDate IS NULL OR b.checkInDate <= :endDate OR b.checkOutDate <= :endDate) "
			+ "AND (:month IS NULL OR FUNCTION('MONTH', b.checkInDate) = :month OR FUNCTION('MONTH', b.checkOutDate) = :month) "
			+ "AND (:year IS NULL OR FUNCTION('YEAR', b.checkInDate) = :year OR FUNCTION('YEAR', b.checkOutDate) = :year)")
	List<Object[]> filterBookings(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
			@Param("month") Integer month, @Param("year") Integer year);

	// -------------------------------- End Report -----------------------//

	// -------------- Report Get and Filter----------- //

	List<Booking> findByCheckInDateBetweenAndStatus(LocalDate startDate, LocalDate endDate, GuestStatus status);

	List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);

	List<Booking> findByCheckInDateAndStatus(LocalDate date, GuestStatus status);

	List<Booking> findByCheckInDate(LocalDate date);

	List<Booking> findByCheckOutDate(LocalDate date);

	List<Booking> findByStatus(GuestStatus status);

	// ------------------------ END -------------- //

//    List<Booking> findByRoom(Room room);
//    
//    @Query("SELECT b FROM Booking b WHERE b.checkInDate >= :startDate AND b.checkInDate <= :endDate")
//    List<Booking> findBookingsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
//    
// // Find all bookings by guest
//    Optional<Booking> findByGuest(Guest guest);
//
//    // Find a booking by guest
//    Optional<Booking> findByGuestAndStatus(Guest guest, Booking.BookingStatus status);
//
//    // Find all bookings by status
//    List<Booking> findByStatus(Booking.BookingStatus status);
//
//    // Find bookings by room
//    List<Booking> findByRoomId(Long roomId);
//
//    // Find all bookings within a specific date range (for example, check-in date)
//    List<Booking> findByCheckInDateBetween(LocalDate startDate, LocalDate endDate);
//    
//    Optional<Booking> findByGuestIdAndRoomIdAndStatus(Long guestId, Long roomId, Booking.BookingStatus status);
//    
//    Optional<Booking> findByGuestAndRoom(Guest guest, Room room);
}
