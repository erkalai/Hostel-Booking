package com.hostelbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

	List<Room> findByCapacityGreaterThan(int capacity);

	Optional<Room> findByRoomNumberAndFloor(String roomNumber, int floor);

	// ---------------- Render --------------//

	@Query("SELECT DISTINCT r.floor FROM Room r")
	List<Integer> findDistinctFloors();

	// -----------------End -----------------//

//    Room findByRoomNumber(int roomNumber);
//    List<Room> findByIsAvailable(boolean isAvailable);
//    
////    @EntityGraph(attributePaths = "guests")
////    Optional<Room> findById(Long id);
//    
//    @Query("SELECT r FROM Room r LEFT JOIN FETCH r.guests WHERE r.id = :roomId")
//    Optional<Room> findById(@Param("roomId") Long roomId);
//    
//    @EntityGraph(attributePaths = {"guests"})
//    Optional<Room> findRoomWithGuestsById(Long id);

}
