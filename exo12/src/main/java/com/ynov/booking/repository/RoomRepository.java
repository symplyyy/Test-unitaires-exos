package com.ynov.booking.repository;

import com.ynov.booking.model.Room;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RoomRepository {

    private final Map<Long, Room> rooms = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(sequence.incrementAndGet());
        }
        rooms.put(room.getId(), room);
        return room;
    }

    public Optional<Room> findById(Long id) {
        return Optional.ofNullable(rooms.get(id));
    }

    public List<Room> findAll() {
        return new ArrayList<>(rooms.values());
    }
}
