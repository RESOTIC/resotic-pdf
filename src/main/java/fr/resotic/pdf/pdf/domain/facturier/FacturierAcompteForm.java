package fr.resotic.pdf.pdf.domain.facturier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FacturierAcompteForm {

    private FacturierDocument document;

    private Date date;

    private Integer typeAcompte;

    private BigDecimal montant;

    private Boolean factureSolde;

    private String commentaire;
    private Boolean avoir;
    private BigDecimal montantHt;

    private BigDecimal montantTva;
    private Set<FacturierTotaux> totaux = new HashSet<>(0);

    public FacturierAcompteForm() {
        // constructeur par defaut
    }

    public FacturierDocument getDocument() {
        return document;
    }

    public Date getDate() {
        return date;
    }

    public Integer getTypeAcompte() {
        return typeAcompte;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public Boolean getFactureSolde() {
        return factureSolde;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public Boolean getAvoir() {
        return avoir;
    }

    public BigDecimal getMontantHt() {
        return montantHt;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public Set<FacturierTotaux> getTotaux() {
        return totaux;
    }
}
