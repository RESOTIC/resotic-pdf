package fr.resotic.pdf.pdf.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pays {

    private Integer id;

    private String libelle;

    public Pays() {
        // constructeur par défaut
    }

    public Integer getId() {
        return this.id;
    }

    public String getLibelle() {
        return libelle;
    }

}
