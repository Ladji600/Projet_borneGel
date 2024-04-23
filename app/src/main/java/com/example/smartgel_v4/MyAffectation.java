package com.example.smartgel_v4;

public class MyAffectation {

    public int getIdMission() {
        return idMission;
    }

    public void setIdMission(int idMission) {
        this.idMission = idMission;
    }

    public String getTypeMission() {
        return typeMission;
    }

    public void setTypeMission(String typeMission) {
        this.typeMission = typeMission;
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

    public int getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(int idEmploye) {
        this.idEmploye = idEmploye;
    }

    public int getIdBorne() {
        return idBorne;
    }

    public void setIdBorne(int idBorne) {
        this.idBorne = idBorne;
    }

    private int idMission;
    private String typeMission;
    private String heure;
    private String date;
    private int idEmploye;
    private  int idBorne;




    //Constructeur


    public MyAffectation(int idMission, String typeMission, String heure, String date, int idEmploye, int idBorne) {
        this.idMission = idMission;
        this.typeMission = typeMission;
        this.heure = heure;
        this.date = date;
        this.idEmploye = idEmploye;
        this.idBorne = idBorne;


    }




}
