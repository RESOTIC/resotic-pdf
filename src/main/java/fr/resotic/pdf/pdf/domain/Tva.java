package fr.resotic.pdf.pdf.domain;

import java.math.BigDecimal;

public class Tva implements java.io.Serializable {

    private Integer id;
    private String libelle;
    private BigDecimal taux;

    public Tva() {
        // constructeur par defaut
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public BigDecimal getTaux() {
        return this.taux;
    }

}
