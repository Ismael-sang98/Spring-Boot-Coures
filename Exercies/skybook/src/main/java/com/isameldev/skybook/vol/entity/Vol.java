package com.isameldev.skybook.vol.entity;

import com.isameldev.skybook.reservation.entity.Reservation;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Vol {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String destination;
    private double prix;
    private int placesDisponibles;

    @OneToMany(mappedBy = "vol")
    private Collection<Reservation> reservations;

    public Vol (){}

    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getPlacesDisponibles() {
        return placesDisponibles;
    }

    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }
}
