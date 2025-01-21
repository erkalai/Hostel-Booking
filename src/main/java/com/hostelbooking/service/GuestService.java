package com.hostelbooking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Guest;
import com.hostelbooking.repository.GuestRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GuestService {

	private final GuestRepository guestRepository;

	@Autowired
	public GuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

}
