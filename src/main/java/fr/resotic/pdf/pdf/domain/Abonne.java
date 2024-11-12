package fr.resotic.pdf.pdf.domain;

public class Abonne {

    private int id;

    private String numDossier;

    private String civilite;

    private String nom;

    private String prenom;

    private Structure structure;

    private Integer calculTva;

    public Abonne() {
    }

    public int getId() {
        return id;
    }

    public String getNumDossier() {
        return numDossier;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Structure getStructure() {
        return structure;
    }

    public Integer getCalculTva() {
        return calculTva;
    }
}
