package fr.resotic.pdf.pdf.domain;

import java.io.Serializable;

public class Rib implements Serializable {

    private Integer id;

    private Abonne abonne;

    private String libelle;

    private String iban;

    private String bic;

    private Boolean reusable;

    private Boolean visible;

    private Boolean defaut;

    public Rib() {
        this.visible = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Abonne getAbonne() {
        return abonne;
    }

    public void setAbonne(Abonne abonne) {
        this.abonne = abonne;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getIban() {
        return (iban == null) ? "" : iban.toUpperCase().replaceAll(" ", "");
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public Boolean getReusable() {
        return reusable;
    }

    public Rib setReusable(Boolean reusable) {
        this.reusable = reusable;
        return this;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Rib setVisible(Boolean visible) {
        this.visible = visible;
        return this;
    }

    public Boolean getDefaut() {
        return defaut;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }
}
