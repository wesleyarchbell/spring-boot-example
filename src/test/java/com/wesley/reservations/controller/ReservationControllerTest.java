package com.wesley.reservations.controller;

import com.wesley.reservations.service.ReservationService;
import com.wesley.reservations.service.dto.RoomReservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ReservationController.class)
public final class ReservationControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getReservations() throws Exception {
        List<RoomReservation> roomReservationList = new ArrayList<>();
        roomReservationList.add(build(12L, "John", "Smith", 34L, "Suite A", "23A", "2018-05-01"));
        given(reservationService.getRoomReservationsForDate(("2018-05-01"))).willReturn(roomReservationList);

        mockMvc.perform(get("/reservations?date=2018-05-01")).andExpect(status().isOk()).andExpect(content().string(containsString("Suite A")));
    }

    private RoomReservation build(long guestId, String firstName, String lastName,
                                  long roomId, String roomName, String roomNumber,
                                  String reservationDate) {
        return new RoomReservation(roomId, guestId, roomName, roomNumber, firstName, lastName, from(reservationDate));
    }

    private Date from(String date) {
        LocalDate startDate = LocalDate.parse(date);
        return Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}