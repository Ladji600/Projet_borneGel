package com.example.smartgel_v4;

public class MyMissionView {
    private int idMission;
    private String type;
    private String heure;
    private String date;
    private int idEmployes;
    private int idBorne;
    private String salle;
    private String niveauGel;
    private String niveauBatterie;
    private int idStatus;




    public MyMissionView(int idMission, String type, String heure, String date, int idEmployes, int idBorne, String salle, String niveauGel, String niveauBatterie, int idStatus) {
        this.idMission = idMission;
        this.type = type;
        this.heure = heure;
        this.date = date;
        this.idEmployes = idEmployes;
        this.idBorne = idBorne;
        this.salle = salle;
        this.niveauGel = niveauGel;
        this.niveauBatterie = niveauBatterie;
        this.idStatus = idStatus;
    }

    public int getIdMission() {
        return idMission;
    }

    public String getType() {
        return type;
    }

    public String getHeure() {
        return heure;
    }

    public String getDate() {
        return date;
    }

    public int getIdEmployes() {
        return idEmployes;
    }

    public int getIdBorne() {
        return idBorne;
    }

    public String getSalle() {
        return salle;
    }
    public String getNiveauGel() {
        return niveauGel;
    }

    public void setNiveauGel(String niveauGel) {
        this.niveauGel = niveauGel;
    }

    public String getNiveauBatterie() {
        return niveauBatterie;
    }

    public void setNiveauBatterie(String niveauBatterie) {
        this.niveauBatterie = niveauBatterie;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

}
