package com.ynov.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynov.booking.exception.AlreadyCancelledException;
import com.ynov.booking.exception.OverlappingReservationException;
import com.ynov.booking.exception.ReservationNotFoundException;
import com.ynov.booking.exception.ValidationException;
import com.ynov.booking.model.Reservation;
import com.ynov.booking.model.ReservationStatus;
import com.ynov.booking.model.Room;
import com.ynov.booking.service.ReservationService;
import com.ynov.booking.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({RoomController.class, ReservationController.class})
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;

    @MockBean
    private ReservationService reservationService;

    @Test
    void createRoomReturns201() throws Exception {
        Room room = new Room("Salle A", 8);
        room.setId(1L);
        when(roomService.create(any())).thenReturn(room);

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Salle A\",\"capacity\":8}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Salle A"));
    }

    @Test
    void createRoomReturns400WhenInvalid() throws Exception {
        when(roomService.create(any())).thenThrow(new ValidationException("nom obligatoire"));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"capacity\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservationReturns201() throws Exception {
        Reservation reservation = new Reservation(1L, "Timeo",
                LocalDateTime.of(2026, 6, 12, 10, 0),
                LocalDateTime.of(2026, 6, 12, 11, 0),
                ReservationStatus.CONFIRMED);
        reservation.setId(1L);
        when(reservationService.create(any())).thenReturn(reservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":1,\"personName\":\"Timeo\",\"start\":\"2026-06-12T10:00:00\",\"end\":\"2026-06-12T11:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void getReservationReturns404WhenMissing() throws Exception {
        when(reservationService.getById(99L)).thenThrow(new ReservationNotFoundException(99L));

        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createReservationReturns409OnOverlap() throws Exception {
        when(reservationService.create(any())).thenThrow(new OverlappingReservationException());

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roomId\":1,\"personName\":\"Timeo\",\"start\":\"2026-06-12T10:00:00\",\"end\":\"2026-06-12T11:00:00\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void cancelReservationReturns409WhenAlreadyCancelled() throws Exception {
        when(reservationService.cancel(1L)).thenThrow(new AlreadyCancelledException(1L));

        mockMvc.perform(patch("/api/reservations/1/cancel"))
                .andExpect(status().isConflict());
    }
}
