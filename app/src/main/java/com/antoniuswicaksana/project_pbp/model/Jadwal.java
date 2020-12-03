package com.antoniuswicaksana.project_pbp.model;

import java.io.Serializable;

public class Jadwal implements Serializable {

    String id, tanggal, waktu, keterangan;

    public Jadwal(String id, String tanggal, String waktu, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
