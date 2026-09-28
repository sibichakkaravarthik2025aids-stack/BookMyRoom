package com.example.bookmyroom.service;

import com.example.bookmyroom.entity.Guest;
import com.example.bookmyroom.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest addGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Guest not found with id: " + id));
    }

    public Guest updateGuest(Long id, Guest updatedGuest) {

        Guest existingGuest = getGuestById(id);

        existingGuest.setName(updatedGuest.getName());
        existingGuest.setEmail(updatedGuest.getEmail());
        existingGuest.setPhone(updatedGuest.getPhone());

        return guestRepository.save(existingGuest);
    }

    public void deleteGuest(Long id) {

        Guest guest = getGuestById(id);

        guestRepository.delete(guest);
    }
}