package com.example.bookmyroom.repository;

import com.example.bookmyroom.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndStatus(
            Long roomId,
            String status
    );

    List<Booking> findByCheckInDateLessThanAndCheckOutDateGreaterThan(
            LocalDate checkOutDate,
            LocalDate checkInDate
    );
    List<Booking> findByStatus(String status);
}