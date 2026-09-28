package com.example.bookmyroom.service;

import com.example.bookmyroom.entity.Booking;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    public ReportService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    public Map<String, Object> getMonthlyOccupancyReport(
            int year,
            int month) {

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate monthStart = yearMonth.atDay(1);
        LocalDate monthEnd = yearMonth.atEndOfMonth();

        List<Booking> bookings =
                bookingRepository.findByStatus("CONFIRMED");

        long occupiedRoomNights = 0;

        for (Booking booking : bookings) {

            LocalDate start = booking.getCheckInDate()
                    .isAfter(monthStart)
                    ? booking.getCheckInDate()
                    : monthStart;

            LocalDate end = booking.getCheckOutDate()
                    .isBefore(monthEnd.plusDays(1))
                    ? booking.getCheckOutDate()
                    : monthEnd.plusDays(1);

            if (start.isBefore(end)) {

                long nights =
                        java.time.temporal.ChronoUnit.DAYS
                                .between(start, end);

                occupiedRoomNights += nights;
            }
        }

        long totalRooms = roomRepository.count();

        long totalRoomNights =
                totalRooms * yearMonth.lengthOfMonth();

        double occupancyPercentage = 0;

        if (totalRoomNights > 0) {
            occupancyPercentage =
                    (occupiedRoomNights * 100.0)
                            / totalRoomNights;
        }

        Map<String, Object> report = new HashMap<>();

        report.put("month", yearMonth.toString());
        report.put("totalRooms", totalRooms);
        report.put("occupiedRoomNights", occupiedRoomNights);
        report.put("totalRoomNights", totalRoomNights);
        report.put("occupancyPercentage", occupancyPercentage);

        return report;
    }
}