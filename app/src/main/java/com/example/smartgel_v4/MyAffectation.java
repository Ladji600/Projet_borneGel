package com.example.smartgel_v4;

public class MyAffectation {
    private  int idBorne;
    private int batterie;
    private int gel;
    private String heure;
    private String date;
    private String salle;
    private String mission;

    private int idEmploye;

    //Constructeur


    public MyAffectation(int idBorne, String salle, int batterie, int gel, String mission, String date, String heure, int idEmploye) {
        this.idBorne = idBorne;
        this.batterie = batterie;
        this.gel = gel;
        this.heure = heure;
        this.date = date;
        this.salle = salle;
        this.mission = mission;
        this.idEmploye = idEmploye;
    }

    public int getIdBorne() {
        return idBorne;
    }

    public void setIdBorne(int idBorne) {
        this.idBorne = idBorne;
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

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public int getIdEmploye() { return idEmploye; }

    public void setIdEmploye(int idEmploye) {this.idEmploye = idEmploye; }


}
