package com.ynov.booking.controller;

import com.ynov.booking.dto.CreateRoomRequest;
import com.ynov.booking.model.Room;
import com.ynov.booking.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService service;

    public RoomController(RoomService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody CreateRoomRequest request) {
        Room created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Room> getAll() {
        return service.getAll();
    }
}
