package com.example.smartgel_v4;

public class MyAlerte {

    private  int idBorne;
    private String salle;
    private int batterie;
    private int gel;
    private String heure;

    public MyAlerte(int idBorne, String salle, int batterie, int gel, String heure, String date) {
        this.idBorne = idBorne;
        this.salle = salle;
        this.batterie = batterie;
        this.gel = gel;
        this.heure = heure;
        this.date = date;
    }


    public int getIdBorne() {
        return idBorne;
    }

    public void setIdBorne(int idBorne) {
        this.idBorne = idBorne;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public int getBatterie() {
        return batterie;
    }

    public void setBatterie(int batterie) {
        this.batterie = batterie;
    }

    public int getGel() {
        return gel;
    }

    public void setGel(int gel) {
        this.gel = gel;
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        this.heure = heure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;


}
