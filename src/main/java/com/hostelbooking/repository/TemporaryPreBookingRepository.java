package com.hostelbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.TemporaryPreBooking;

@Repository
public interface TemporaryPreBookingRepository extends JpaRepository<TemporaryPreBooking, Long> {

	Optional<TemporaryPreBooking> findByKid(String kid);

	void deleteByKid(String kid);
}
