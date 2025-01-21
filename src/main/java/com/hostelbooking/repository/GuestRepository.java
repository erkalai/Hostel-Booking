package com.hostelbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Guest;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

	// Query to find guest by KID
	Optional<Guest> findByKid(String kid);

//	 Optional<Guest> findByMobileNumberAndKid(String mobileNumber, String kid);
//    
//    Guest findGuestById(Long id);
//
//	List<Guest> findByRoomId(Long id);
//	
//	List<Guest> findByRoom_RoomNumber(int roomNumber);
//	
//	   List<Guest> findByCheckInDate(LocalDate checkInDate);
//
//	    List<Guest> findByCheckOutDate(LocalDate checkOutDate);

//	    List<Guest> findByIdNumber(String idNumber);

//	    @Query("SELECT g FROM Guest g WHERE MONTH(g.checkInDate) = :month AND YEAR(g.checkInDate) = :year")
//	    List<Guest> findByCheckInDateMonthAndYear(@Param("month") int month, @Param("year") int year);

//	    List<Guest> findByStatus(GuestStatus status);
}
