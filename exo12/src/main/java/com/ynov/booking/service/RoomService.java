package com.ynov.booking.service;

import com.ynov.booking.dto.CreateRoomRequest;
import com.ynov.booking.exception.ValidationException;
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
        if (request.getName() == null || request.getName().isBlank()) {
            throw new ValidationException("Le nom est obligatoire");
        }
        if (request.getCapacity() < 1) {
            throw new ValidationException("La capacite doit etre superieure ou egale a 1");
        }

        Room room = new Room(request.getName(), request.getCapacity());
        return repository.save(room);
    }

    public List<Room> getAll() {
        return repository.findAll();
    }
}
