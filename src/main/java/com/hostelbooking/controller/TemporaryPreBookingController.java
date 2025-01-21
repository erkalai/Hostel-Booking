package com.hostelbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostelbooking.entity.TemporaryPreBooking;
import com.hostelbooking.service.TemporaryPreBookingService;

@Controller
@RequestMapping("/prebooking")
public class TemporaryPreBookingController {

	@Autowired
	private TemporaryPreBookingService service;

	@GetMapping
	public String temporaryBooking(Model model) {
		return "bookings/PreBookedGuests";
	}

	@GetMapping("/guest/{kid}")
	public ResponseEntity<TemporaryPreBooking> getGuestByKid(@PathVariable String kid) {
		TemporaryPreBooking guest = service.getDetailsByKid(kid);
		return ResponseEntity.ok(guest);
	}

	@PostMapping("/add")
	public ResponseEntity<TemporaryPreBooking> addPreBooking(@ModelAttribute TemporaryPreBooking preBooking) {
		TemporaryPreBooking savedBooking = service.savePreBooking(preBooking);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
	}

}
