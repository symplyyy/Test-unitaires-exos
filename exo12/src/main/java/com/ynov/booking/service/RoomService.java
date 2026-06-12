package com.ynov.booking.service;

import com.ynov.booking.dto.CreateRoomRequest;
import com.ynov.booking.model.Room;
import com.ynov.booking.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository repository;

    public RoomService(RoomRepository repository) {
        this.repository = repository;
    }

    public Room create(CreateRoomRequest request) {
        throw new UnsupportedOperationException("pas encore implémenté");
    }

    public List<Room> getAll() {
        throw new UnsupportedOperationException("pas encore implémenté");
    }
}
