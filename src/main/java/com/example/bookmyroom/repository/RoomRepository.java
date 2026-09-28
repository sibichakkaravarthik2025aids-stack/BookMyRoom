package com.example.bookmyroom.repository;

import com.example.bookmyroom.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByRoomTypeContainingIgnoreCase(String roomType);
}