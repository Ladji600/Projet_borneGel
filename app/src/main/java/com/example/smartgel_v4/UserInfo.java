package com.example.smartgel_v4;
public class UserInfo {
    private String nom;
    private String prenom;
    private String mail;
    private int idEmployes;
    private int role;



    public UserInfo(int idEmployes, String nom, String prenom, String mail, int role) {
        this.nom = nom;
        this.prenom = prenom;
        this.mail = mail;
        this.idEmployes = idEmployes;
        this.role = role;
    }

    public int getIdEmployes() {
        return idEmployes;
    }

    public int getRole() {
        return role;
    }


    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (" + mail + ")";
    }
}
