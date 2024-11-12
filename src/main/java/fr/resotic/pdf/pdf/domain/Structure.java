package fr.resotic.pdf.pdf.domain;

import java.io.Serializable;

public class Structure implements Serializable {

    private String intitule;

    private Integer regimeTva;

    private Integer paiementTva;

    public Structure() {
        super();
    }

    public String getIntitule() {
        return intitule;
    }

    public Integer getRegimeTva() {
        return regimeTva;
    }

    public Integer getPaiementTva() {
        return paiementTva;
    }
}
