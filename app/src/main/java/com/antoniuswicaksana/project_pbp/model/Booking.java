package com.antoniuswicaksana.project_pbp.model;

import java.io.Serializable;

public class Booking implements Serializable {

    String id, clientID, paket, alamat, tanggal, waktu;

    public Booking(String id, String clientID, String paket, String alamat, String tanggal, String waktu) {
        this.id = id;
        this.clientID = clientID;
        this.paket = paket;
        this.alamat = alamat;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }
}
