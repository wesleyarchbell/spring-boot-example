package com.wesley.reservations.controller;

import com.wesley.reservations.service.ReservationService;
import com.wesley.reservations.service.dto.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@Controller()
@RequestMapping("/reservations")
public final class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @RequestMapping(method = RequestMethod.GET)
    public String getReservations(@RequestParam(name = "date", required = false) String dateString, Model model) {
        LocalDate date = LocalDate.now();
        if (dateString != null) {
           try {
               date = LocalDate.parse(dateString);
           } catch (DateTimeParseException e) {
               date = LocalDate.now();
           }
        }
        List<RoomReservation> roomReservations = getRoomReservations(date);
        model.addAttribute("roomReservations", roomReservations);
        return "reservations";
    }

    private List<RoomReservation> getRoomReservations(LocalDate date) {
        Date startDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return this.reservationService.getRoomReservationsForDate(startDate);
    }

}