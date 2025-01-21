package com.hostelbooking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.Complaint;
import com.hostelbooking.repository.ComplaintRepository;

import jakarta.transaction.Transactional;

@Service
public class ComplaintService {
	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	public ComplaintService(ComplaintRepository complaintRepository) {
		this.complaintRepository = complaintRepository;
	}

	@Transactional
	public Complaint saveComplaint(Complaint complaint) {
		try {
			System.out.println("Saving complaint: " + complaint);
			return complaintRepository.save(complaint);
		} catch (Exception e) {
			System.err.println("Error saving complaint: " + e.getMessage());
			throw e;
		}
	}

	// Method to count the number of pending complaints
	public long countPendingComplaints() {
		return complaintRepository.countByStatus(Complaint.Status.PENDING);
	}

	// Method to fetch complaints with status PENDING
	public List<Complaint> getPendingComplaints() {
		return complaintRepository.findByStatus("PENDING");
	}

	// Method to retrieve all complaints
	public List<Complaint> findAllComplaints() {
		return complaintRepository.findAll();
	}

	public Complaint getComplaintById(Long id) {
		return complaintRepository.findById(id).orElseThrow(() -> new RuntimeException("Complaint not found"));
	}

}
