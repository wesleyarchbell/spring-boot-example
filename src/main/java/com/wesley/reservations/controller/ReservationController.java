package com.wesley.reservations.controller;

import com.wesley.reservations.service.ReservationService;
import com.wesley.reservations.service.dto.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller()
@RequestMapping("/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getReservations(@RequestParam(name = "date", required = false) String dateString, Model model) {
        List<RoomReservation> roomReservations = this.reservationService.getRoomReservationsForDate(dateString);
        model.addAttribute("roomReservations", roomReservations);
        return "reservations";
    }

}