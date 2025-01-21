package com.hostelbooking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hostelbooking.entity.Program;
import com.hostelbooking.repository.ProgramRepository;
import com.hostelbooking.service.ProgramService;

@Controller
@RequestMapping("/programs")
public class ProgramController {

	@Autowired
	private ProgramService programService;

	@Autowired
	private ProgramRepository programRepository;
	// Display list of programs and the form to create/update

	@GetMapping
	public String showPrograms(Model model) {
		List<Program> programs = programRepository.findAllOrderedByStarred();
		model.addAttribute("programs", programs);
		model.addAttribute("program", new Program());
		return "common/program";
	}

	@PostMapping("/create")
	public String saveProgram(@ModelAttribute Program program) {
		programService.saveProgram(program);
		return "redirect:/programs";
	}

	// Display the form for editing a program
	@GetMapping("/edit/{id}")
	public String editProgram(@PathVariable Long id, Model model) {
		Program program = programService.findProgramById(id);
		model.addAttribute("program", program);
		model.addAttribute("programs", programService.findAllPrograms());
		return "common/program";
	}

	@PostMapping({ "/edit/{id}" })
	public String saveProgram(@ModelAttribute Program program, @PathVariable(required = false) Long id, Model model) {
		if (id != null) {
			program.setId(id);
		}
		programService.saveProgram(program);
		model.addAttribute("successMessage", "Program saved successfully!");
		return "redirect:/programs";
	}

	// Handle delete request
	@GetMapping("/delete/{id}")
	public String deleteProgram(@PathVariable Long id) {
		programService.deleteProgram(id);
		return "redirect:/programs";
	}

	@GetMapping("/search")
	public ResponseEntity<List<String>> searchPrograms(@RequestParam String query) {
		List<String> programNames = programRepository.findProgramNamesByNameContaining(query);
		return ResponseEntity.ok(programNames);
	}
}
