package com.isameldev.skybook.reservation.entity;

import com.isameldev.skybook.vol.entity.Vol;
import jakarta.persistence.*;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomPassager;

    @ManyToOne
    @JoinColumn(name = "vol_id")
    private Vol vol;


    public Reservation (){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPassager() {
        return nomPassager;
    }

    public void setNomPassager(String nomPassager) {
        this.nomPassager = nomPassager;
    }

    public Vol getVol() {
        return vol;
    }

    public void setVol(Vol vol) {
        this.vol = vol;
    }
}
