package com.example.bookmyroom.service;

import com.example.bookmyroom.entity.Booking;
import com.example.bookmyroom.entity.Guest;
import com.example.bookmyroom.entity.Room;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.GuestRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public BookingService(
            BookingRepository bookingRepository,
            RoomRepository roomRepository,
            GuestRepository guestRepository) {

        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public Booking createBooking(
            Long roomId,
            Long guestId,
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        // 1. Validate dates
        if (checkInDate == null || checkOutDate == null) {
            throw new RuntimeException("Check-in and check-out dates are required");
        }

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new RuntimeException(
                    "Check-out date must be after check-in date");
        }

        // 2. Find room
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + roomId));

        // 3. Find guest
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Guest not found with id: " + guestId));

        // 4. Check overlapping bookings
        List<Booking> existingBookings =
                bookingRepository.findByRoomIdAndStatus(
                        roomId,
                        "CONFIRMED");

        for (Booking existingBooking : existingBookings) {

            boolean overlapping =
                    checkInDate.isBefore(existingBooking.getCheckOutDate())
                            && checkOutDate.isAfter(
                            existingBooking.getCheckInDate());

            if (overlapping) {
                throw new RuntimeException(
                        "Room is already booked for the selected dates");
            }
        }

        // 5. Calculate number of nights
        long numberOfNights =
                ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        // 6. Calculate total cost
        double totalCost =
                numberOfNights * room.getRatePerNight();

        // 7. Create booking
        Booking booking = new Booking();

        booking.setRoom(room);
        booking.setGuest(guest);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalCost(totalCost);
        booking.setStatus("CONFIRMED");

        // 8. Save booking
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Booking not found with id: " + id));
    }

    public void cancelBooking(Long id) {

        Booking booking = getBookingById(id);

        booking.setStatus("CANCELLED");

        bookingRepository.save(booking);
    }
}