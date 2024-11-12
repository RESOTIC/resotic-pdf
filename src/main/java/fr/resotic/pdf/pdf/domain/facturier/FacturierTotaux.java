package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Tva;

import java.math.BigDecimal;

public class FacturierTotaux {

    private Tva tva;
    private BigDecimal totalTva;
    private BigDecimal totalTvaGlobal;
    private BigDecimal netHt;

    public FacturierTotaux() {
        // constructeur par défaut
    }

    public Tva getTva() {
        return tva;
    }

    public BigDecimal getTotalTva() {
        return totalTva;
    }

    public BigDecimal getNetHt() {
        return netHt;
    }

    public BigDecimal getTotalTvaGlobal() {
        return totalTvaGlobal;
    }

}
