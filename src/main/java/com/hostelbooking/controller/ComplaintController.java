package com.hostelbooking.controller;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.entity.Complaint;
import com.hostelbooking.service.ComplaintService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/complaint")
public class ComplaintController {

	@Autowired
	private ComplaintService complaintService;

	 @Autowired
	    public ComplaintController(ComplaintService complaintService) {
	        this.complaintService = complaintService;
	    }
	
    @GetMapping
    public String showComplaintForm(Model model) {
        model.addAttribute("complaint", new Complaint());
        return "complaint";
    }

    @PostMapping
    public String handleComplaintSubmit(@Valid @ModelAttribute Complaint complaint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errorMessage", "Validation error: " + result.getAllErrors().get(0).getDefaultMessage());
            return "receptionist/dashboard";
        }

        try {
            if (complaint.getStatus() == null) {
                complaint.setStatus(Complaint.Status.PENDING);
            }

            complaintService.saveComplaint(complaint);
            model.addAttribute("successMessage", "Complaint submitted successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error submitting complaint: " + e.getMessage());
        }
        return "receptionist/dashboard";
    }

    @PostMapping("/update/{id}")
    public String updateComplaintStatus(@PathVariable Long id, @RequestParam String status, @RequestParam String description, Model model) {
        try {
            Complaint complaint = complaintService.getComplaintById(id);
            complaint.setStatus(Complaint.Status.valueOf(status));
            if (Complaint.Status.COMPLETED == complaint.getStatus()) {
                complaint.setCompletedDate(LocalDateTime.now());
                complaint.setCompletedDescription(description);
            }
            complaintService.saveComplaint(complaint);
            model.addAttribute("successMessage", "Complaint updated successfully!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating complaint: " + e.getMessage());
        }
        return "redirect:/complaint/management";
    }

    @GetMapping("/management")
    public String complaintManagement(Model model) {
        List<Complaint> complaints = complaintService.findAllComplaints();
        model.addAttribute("complaints", complaints);
        return "manageComplaints";
    }
 

    
}
