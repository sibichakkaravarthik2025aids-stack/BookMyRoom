package com.example.bookmyroom.controller;

import com.example.bookmyroom.entity.Room;
import com.example.bookmyroom.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Add a new room
    @PostMapping
    public ResponseEntity<Room> addRoom(
            @Valid @RequestBody Room room) {

        return ResponseEntity.ok(
                roomService.addRoom(room)
        );
    }
    // View all rooms
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    @GetMapping("/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam String roomType) {

        return ResponseEntity.ok(
                roomService.searchRooms(roomType)
        );
    }
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
            @RequestParam LocalDate checkInDate,
            @RequestParam LocalDate checkOutDate) {

        if (!checkOutDate.isAfter(checkInDate)) {
            throw new RuntimeException(
                    "Check-out date must be after check-in date");
        }

        return ResponseEntity.ok(
                roomService.getAvailableRooms(
                        checkInDate,
                        checkOutDate
                )
        );
    }

    // View room by ID
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Update room
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody Room room) {

        return ResponseEntity.ok(
                roomService.updateRoom(id, room)
        );
    }

    // Delete room
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {

        roomService.deleteRoom(id);

        return ResponseEntity.ok("Room deleted successfully");
    }
}