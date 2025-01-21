package com.hostelbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.service.SystemSettingService;

@Controller
@RequestMapping("/settings")
public class AdminSettingController {

	@Autowired
	private SystemSettingService systemSettingService;

	@GetMapping
	public String showSettings(Model model) {
		boolean allowRegistration = systemSettingService.isRegistrationAllowed();
		model.addAttribute("allowRegistration", allowRegistration);
		return "admin/settings";
	}

	@PostMapping("/update-registration")
	public String updateRegistrationStatus(
			@RequestParam(value = "allowRegistration", defaultValue = "false") boolean allowRegistration) {
		systemSettingService.updateRegistrationStatus(allowRegistration);
		return "redirect:/settings";
	}

}
