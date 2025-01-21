//package com.hostelbooking.controller;
//
//import com.hostelbooking.entity.Booking;
//import com.hostelbooking.enums.GuestStatus;
//import com.hostelbooking.service.ReportService;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.stereotype.Controller;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/reports")
//public class ReportController {
//
//    @Autowired
//    private ReportService reportService;
// 
//    
//    
//    @GetMapping("/generate")
//    public String generateReport(Model model,
//                                 @RequestParam(required = false) LocalDate day,
//                                 @RequestParam(required = false) Integer month,
//                                 @RequestParam(required = false) Integer year,
//                                 @RequestParam(required = false) LocalDate startDate,
//                                 @RequestParam(required = false) LocalDate endDate,
//                                 @RequestParam(required = false) GuestStatus status) {
//
//        List<Booking> bookings = new ArrayList<>();
//        if (day != null) {
//            bookings = reportService.getReportByDay(day);
//        } else if (month != null && year != null) {
//            bookings = reportService.getReportByMonth(month, year);
//        } else if (year != null) {
//            bookings = reportService.getReportByYear(year);
//        } else if (startDate != null && endDate != null) {
//            bookings = reportService.getReportByDateRange(startDate, endDate);
//        } else if (status != null) {
//            bookings = reportService.getReportByStatus(status);
//        }
//
//        model.addAttribute("bookings", bookings);
//        return "bookings/report";  // Thymeleaf template for the report
//    }
//
//    
//    
//    
//    
//   
//}
