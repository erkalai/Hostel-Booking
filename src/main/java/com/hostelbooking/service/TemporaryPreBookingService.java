package com.hostelbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.TemporaryPreBooking;
import com.hostelbooking.repository.TemporaryPreBookingRepository;

@Service
public class TemporaryPreBookingService {

	@Autowired
	private TemporaryPreBookingRepository repository;

	public TemporaryPreBooking getDetailsByKid(String kid) {
		return repository.findByKid(kid).orElseThrow(() -> new RuntimeException("Guest not found for KID: " + kid));
	}

	public TemporaryPreBooking savePreBooking(TemporaryPreBooking preBooking) {
		return repository.save(preBooking);
	}

	public void deleteByKid(String kid) {
		repository.deleteByKid(kid);
	}
}
