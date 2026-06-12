package com.ynov.booking.service;

import com.ynov.booking.dto.CreateRoomRequest;
import com.ynov.booking.exception.ValidationException;
import com.ynov.booking.model.Room;
import com.ynov.booking.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomService service;

    @Test
    void create_returnsRoomWithGivenData() {
        when(repository.save(any(Room.class))).thenAnswer(invocation -> {
            Room r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("Salle A");
        request.setCapacity(8);

        Room result = service.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Salle A");
        assertThat(result.getCapacity()).isEqualTo(8);
    }

    @Test
    void create_throwsWhenNameIsBlank() {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("  ");
        request.setCapacity(5);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void create_throwsWhenCapacityIsBelowOne() {
        CreateRoomRequest request = new CreateRoomRequest();
        request.setName("Salle A");
        request.setCapacity(0);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(repository, never()).save(any());
    }

    @Test
    void getAll_returnsAllRooms() {
        when(repository.findAll()).thenReturn(List.of(new Room("A", 4), new Room("B", 6)));

        List<Room> result = service.getAll();

        assertThat(result).hasSize(2);
    }
}
