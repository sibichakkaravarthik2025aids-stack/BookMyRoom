package com.example.bookmyroom.service;

import com.example.bookmyroom.entity.Booking;
import com.example.bookmyroom.entity.Room;
import com.example.bookmyroom.repository.BookingRepository;
import com.example.bookmyroom.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    public RoomService(
            RoomRepository roomRepository,
            BookingRepository bookingRepository) {

        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
    }

    public Room addRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    public List<Room> searchRooms(String roomType) {

        return roomRepository.findByRoomTypeContainingIgnoreCase(roomType);
    }

    // Check available rooms
    public List<Room> getAvailableRooms(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        List<Room> allRooms = roomRepository.findAll();

        return allRooms.stream()
                .filter(room -> {

                    List<Booking> bookings =
                            bookingRepository.findByRoomIdAndStatus(
                                    room.getId(),
                                    "CONFIRMED"
                            );

                    return bookings.stream().noneMatch(booking ->
                            checkInDate.isBefore(
                                    booking.getCheckOutDate())
                                    &&
                                    checkOutDate.isAfter(
                                            booking.getCheckInDate())
                    );
                })
                .toList();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found with id: " + id));
    }

    public Room updateRoom(Long id, Room updatedRoom) {

        Room existingRoom = getRoomById(id);

        existingRoom.setRoomType(updatedRoom.getRoomType());
        existingRoom.setCapacity(updatedRoom.getCapacity());
        existingRoom.setRatePerNight(updatedRoom.getRatePerNight());

        return roomRepository.save(existingRoom);
    }

    public void deleteRoom(Long id) {

        Room room = getRoomById(id);

        roomRepository.delete(room);
    }
}