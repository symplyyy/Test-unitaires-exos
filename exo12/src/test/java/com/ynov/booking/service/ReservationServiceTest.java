package com.ynov.booking.service;

import com.ynov.booking.dto.CreateReservationRequest;
import com.ynov.booking.exception.AlreadyCancelledException;
import com.ynov.booking.exception.OverlappingReservationException;
import com.ynov.booking.exception.ReservationNotFoundException;
import com.ynov.booking.exception.RoomNotFoundException;
import com.ynov.booking.exception.ValidationException;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.model.ReservationStatus;
import com.ynov.booking.model.Room;
import com.ynov.booking.repository.ReservationRepository;
import com.ynov.booking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private ReservationService service;

    private CreateReservationRequest request;
    private final LocalDateTime start = LocalDateTime.of(2026, 6, 12, 10, 0);
    private final LocalDateTime end = LocalDateTime.of(2026, 6, 12, 11, 0);

    @BeforeEach
    void setUp() {
        request = new CreateReservationRequest();
        request.setRoomId(1L);
        request.setPersonName("Timeo");
        request.setStart(start);
        request.setEnd(end);
    }

    @Test
    void create_returnsConfirmedReservation() {
        Room room = existingRoom();
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation r = invocation.getArgument(0);
            r.setId(1L);
            return r;
        });

        Reservation result = service.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
        assertThat(result.getPersonName()).isEqualTo("Timeo");
    }

    @Test
    void create_throwsWhenRoomDoesNotExist() {
        when(roomRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(RoomNotFoundException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenPersonNameIsBlank() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        request.setPersonName("   ");

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenEndIsBeforeStart() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        request.setEnd(start.minusHours(1));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenEndEqualsStart() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        request.setEnd(start);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ValidationException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_throwsWhenSlotOverlapsConfirmedReservation() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        Reservation existing = new Reservation(1L, "Bob",
                start.plusMinutes(30), end.plusMinutes(30), ReservationStatus.CONFIRMED);
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of(existing));

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(OverlappingReservationException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void create_allowsSlotOverlappingACancelledReservation() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        Reservation cancelled = new Reservation(1L, "Bob",
                start.plusMinutes(30), end.plusMinutes(30), ReservationStatus.CANCELLED);
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of(cancelled));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.create(request);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void create_allowsAdjacentSlot() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(existingRoom()));
        Reservation before = new Reservation(1L, "Bob",
                start.minusHours(1), start, ReservationStatus.CONFIRMED);
        when(reservationRepository.findByRoomId(1L)).thenReturn(List.of(before));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.create(request);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    void getById_returnsReservationWhenItExists() {
        Reservation reservation = confirmedReservation(7L);
        when(reservationRepository.findById(7L)).thenReturn(Optional.of(reservation));

        Reservation result = service.getById(7L);

        assertThat(result.getId()).isEqualTo(7L);
    }

    @Test
    void getById_throwsWhenReservationDoesNotExist() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(99L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void cancel_changesStatusToCancelled() {
        Reservation reservation = confirmedReservation(7L);
        when(reservationRepository.findById(7L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Reservation result = service.cancel(7L);

        assertThat(result.getStatus()).isEqualTo(ReservationStatus.CANCELLED);
    }

    @Test
    void cancel_throwsWhenAlreadyCancelled() {
        Reservation reservation = confirmedReservation(7L);
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(7L)).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> service.cancel(7L))
                .isInstanceOf(AlreadyCancelledException.class);

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancel_throwsWhenReservationDoesNotExist() {
        when(reservationRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(42L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    private Room existingRoom() {
        Room room = new Room("Salle A", 10);
        room.setId(1L);
        return room;
    }

    private Reservation confirmedReservation(Long id) {
        Reservation reservation = new Reservation(1L, "Timeo", start, end, ReservationStatus.CONFIRMED);
        reservation.setId(id);
        return reservation;
    }
}
