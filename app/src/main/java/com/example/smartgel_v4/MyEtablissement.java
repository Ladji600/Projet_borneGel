package com.example.smartgel_v4;

public class MyEtablissement {

    public MyEtablissement(int idEtablissement, String etablissement){
        this.idEtablissement = idEtablissement;
        this.etablissement= etablissement;

    }
    private  int idEtablissement;

    public int getIdEtablissement() {
        return idEtablissement;
    }

    public void setIdEtablissement(int idEtablissement) {
        this.idEtablissement = idEtablissement;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    private String etablissement;

    //Constructeur

}
