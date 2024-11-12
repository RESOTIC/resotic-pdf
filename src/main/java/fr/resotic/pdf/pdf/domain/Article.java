package fr.resotic.pdf.pdf.domain;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class Article {

    private Integer id;

    private Tva tva;

    private String libelle;

    private String commentaire;

    private String unite;

    private BigDecimal prixHt;

    private BigDecimal prixTtc;

    public String getLibellePDF() {
        if (!StringUtils.isEmpty(getLibelle())) {
            return getLibelle().replaceAll("\t", " ");
        }
        return null;
    }

    public String getCommentairePDF() {
        if (!StringUtils.isEmpty(getCommentaire())) {
            return getCommentaire().replaceAll("\t", " ");
        }

        return null;
    }

    public Article() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tva getTva() {
        return tva;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public String getUnite() {
        return unite;
    }

    public BigDecimal getPrixHt() {
        return prixHt;
    }

    public BigDecimal getPrixTtc() {
        return prixTtc;
    }

}
