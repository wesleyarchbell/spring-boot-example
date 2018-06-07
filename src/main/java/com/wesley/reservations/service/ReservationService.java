package com.wesley.reservations.service;

import com.wesley.reservations.data.entity.Guest;
import com.wesley.reservations.data.entity.Reservation;
import com.wesley.reservations.data.entity.Room;
import com.wesley.reservations.data.repository.RoomRepository;
import com.wesley.reservations.data.webservice.GuestRepository;
import com.wesley.reservations.data.webservice.ReservationRepository;
import com.wesley.reservations.service.dto.RoomReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public final class ReservationService {

    private RoomRepository roomRepository;
    private GuestRepository guestRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(RoomRepository roomRepository, GuestRepository guestRepository, ReservationRepository reservationRepository) {
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RoomReservation> getRoomReservationsForDate(Date date) {
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
}