package com.wesley.reservations.service;

import com.wesley.reservations.data.entity.Guest;
import com.wesley.reservations.data.entity.Reservation;
import com.wesley.reservations.data.entity.Room;
import com.wesley.reservations.data.repository.RoomRepository;
import com.wesley.reservations.data.repository.GuestRepository;
import com.wesley.reservations.data.repository.ReservationRepository;
import com.wesley.reservations.service.dto.RoomReservation;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ReservationService {

    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(String reservationDate) {
        Date date = from(reservationDate);
        Iterable<Room> rooms = this.roomRepository.findAll();
        Map<Long, RoomReservation> roomReservations = new HashMap<>();
        rooms.forEach(room -> {
            RoomReservation roomReservation = new RoomReservation();
            roomReservation.setRoomId(room.getId());
            roomReservation.setRoomName(room.getName());
            roomReservation.setRoomNumber(room.getNumber());
            roomReservations.putIfAbsent(room.getId(), roomReservation);
        });

        List<Reservation> reservationsByDate = this.reservationRepository.findByDate(new java.sql.Date(date.getTime()));
        reservationsByDate.forEach(reservation -> {
            Optional<Guest> guest = this.guestRepository.findById(reservation.getGuestId());
            if (guest.isPresent()) {
                Guest roomGuest = guest.get();
                RoomReservation roomReservation = roomReservations.get(reservation.getRoomId());
                roomReservation.setDate(reservation.getDate());
                roomReservation.setGuestId(roomGuest.getId());
                roomReservation.setFirstName(roomGuest.getFirstName());
                roomReservation.setLastName(roomGuest.getLastName());
            }
        });
        return new ArrayList<>(roomReservations.values());
    }

    private Date from(String date) {
        LocalDate startDate = LocalDate.now();
        if (date != null) {
            try {
                startDate = LocalDate.parse(date);
            } catch (DateTimeParseException e) {
                startDate = LocalDate.now();
            }
        }
        return Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}