package com.example.smartgel_v4;

public class MyAlerte {

    private  int idBorne;

    private int gel;
    private int batterie;
    private String salle;
    private String heure;
    private String date;
    private int idEtablissement;


    public MyAlerte(int idBorne, int gel, int batterie, String salle, String heure, String date, int idEtablissement) {
        this.idBorne = idBorne;
        this.salle = salle;
        this.batterie = batterie;
        this.gel = gel;
        this.heure = heure;
        this.date = date;
        this.idEtablissement = idEtablissement;
    }




    public int getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(int idEtablissement) {
        this.idEtablissement = idEtablissement;
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




}
