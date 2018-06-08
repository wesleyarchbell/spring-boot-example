package com.wesley.reservations.service;

import com.wesley.reservations.service.dto.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public final class ReservationServiceController {

    private ReservationService reservationService;

    @Autowired
    public ReservationServiceController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reservations/{date}")
    public List<RoomReservation> getReservations(@PathVariable(name = "date") String dateString) {
        return reservationService.getRoomReservationsForDate(dateString);
    }

}