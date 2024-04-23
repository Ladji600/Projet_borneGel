package com.example.smartgel_v4;

public class MyEtablissement {

    public String getAddresse() {
        return addresse;
    }

    public void setAddresse(String addresse) {
        this.addresse = addresse;
    }

    public MyEtablissement(int idEtablissement, String etablissement, String addresse){
        this.idEtablissement = idEtablissement;
        this.etablissement= etablissement;
        this.addresse = addresse;

    }
    private  int idEtablissement;
    private String addresse;

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
