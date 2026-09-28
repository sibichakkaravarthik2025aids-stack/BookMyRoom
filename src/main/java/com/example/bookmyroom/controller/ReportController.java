package com.example.bookmyroom.controller;

import com.example.bookmyroom.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // Monthly Occupancy Report
    @GetMapping("/occupancy")
    public ResponseEntity<Map<String, Object>> getMonthlyOccupancyReport(
            @RequestParam int year,
            @RequestParam int month) {

        return ResponseEntity.ok(
                reportService.getMonthlyOccupancyReport(
                        year,
                        month
                )
        );
    }
}