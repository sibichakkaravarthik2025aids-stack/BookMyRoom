package com.example.bookmyroom.controller;

import com.example.bookmyroom.entity.Guest;
import com.example.bookmyroom.service.GuestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    // Add a new guest
    @PostMapping
    public ResponseEntity<Guest> addGuest(
            @Valid @RequestBody Guest guest) {

        return ResponseEntity.ok(
                guestService.addGuest(guest)
        );
    }
    // View all guests
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        return ResponseEntity.ok(guestService.getAllGuests());
    }

    // View guest by ID
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    // Update guest
    @PutMapping("/{id}")
    public ResponseEntity<Guest> updateGuest(
            @PathVariable Long id,
            @Valid @RequestBody Guest guest) {

        return ResponseEntity.ok(
                guestService.updateGuest(id, guest)
        );
    }

    // Delete guest
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable Long id) {

        guestService.deleteGuest(id);

        return ResponseEntity.ok("Guest deleted successfully");
    }
}