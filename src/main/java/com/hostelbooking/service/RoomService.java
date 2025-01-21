package com.hostelbooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Room;
import com.hostelbooking.repository.RoomRepository;

import jakarta.transaction.Transactional;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	public List<Room> getAvailableRooms() {
		return roomRepository.findByCapacityGreaterThan(0);
	}

	public Optional<Room> getRoomById(Long id) {
		return roomRepository.findById(id);
	}

	public List<Room> getAllRooms() {
		return roomRepository.findAll();
	}

	// Create Room By Admin
	public Room createRoom(Room room) {
		Optional<Room> existingRoom = roomRepository.findByRoomNumberAndFloor(room.getRoomNumber(), room.getFloor());
		if (existingRoom.isPresent()) {
			throw new IllegalArgumentException(
					"Room " + room.getRoomNumber() + " already exists on floor " + room.getFloor());
		}

		if (room.getCapacity() > room.getMaxCapacity()) {
			throw new IllegalArgumentException("Capacity cannot exceed max capacity.");
		}

		return roomRepository.save(room);
	}

	public Room updateRoom(Long id, Room roomDetails) {
		Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
		room.setRoomNumber(roomDetails.getRoomNumber());
		room.setFloor(roomDetails.getFloor());
		room.setCapacity(roomDetails.getCapacity());
		room.setMaxCapacity(roomDetails.getMaxCapacity());
		return roomRepository.save(room);
	}

	public void deleteRoom(Long id) {
		roomRepository.deleteById(id);
	}

	@Transactional
	public Room updateRoomCapacity(Long roomId, int change) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new RuntimeException("Room not found with ID: " + roomId));

		int newCapacity = room.getCapacity() + change;

		// Validate new capacity
		if (newCapacity < 0) {
			throw new RuntimeException("Cannot reduce capacity below zero for room ID: " + roomId);
		}
		if (newCapacity > room.getMaxCapacity()) {
			throw new RuntimeException("Cannot exceed maximum capacity for room ID: " + roomId);
		}

		room.setCapacity(newCapacity);
		return roomRepository.save(room);
	}

	// -------------- Render Foor

	public List<Integer> getUniqueFloors() {
		return roomRepository.findDistinctFloors();
	}

	// --------------End

}
