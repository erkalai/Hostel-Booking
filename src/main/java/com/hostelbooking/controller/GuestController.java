package com.hostelbooking.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.entity.Guest;
import com.hostelbooking.repository.GuestRepository;
import com.hostelbooking.service.GuestService;

@Controller
@RequestMapping("/guests")
public class GuestController {

	private final GuestService guestService;

	private GuestRepository guestRepository;

	@Autowired
	public GuestController(GuestService guestService) {
		this.guestService = guestService;
	}

	// Fetch guest details by KID
	@GetMapping("/findByKid")
	public ResponseEntity<Guest> findByKid(@RequestParam String kid) {
		Optional<Guest> guestOpt = guestRepository.findByKid(kid);
		if (guestOpt.isPresent()) {
			return ResponseEntity.ok(guestOpt.get());
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

}
