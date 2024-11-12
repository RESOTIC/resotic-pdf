package fr.resotic.pdf.pdf.domain.facturier;

import java.math.BigDecimal;

public class FacturierArticleSectionFacturierArticle {

    private Integer id;

    private FacturierArticleSection section;

    private FacturierArticle article;

    private Integer ordre;

    private BigDecimal quantite;

    private BigDecimal remise;

    private BigDecimal montantNetHt;

    private BigDecimal montantNetTtc;

    private BigDecimal montantTva;

    public FacturierArticleSectionFacturierArticle() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacturierArticleSection getSection() {
        return section;
    }

    public void setSection(FacturierArticleSection section) {
        this.section = section;
    }

    public FacturierArticle getArticle() {
        return article;
    }

    public void setArticle(FacturierArticle article) {
        this.article = article;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getMontantNetHt() {
        return montantNetHt;
    }

    public void setMontantNetHt(BigDecimal montantNetHt) {
        this.montantNetHt = montantNetHt;
    }

    public BigDecimal getMontantNetTtc() {
        return montantNetTtc;
    }

    public void setMontantNetTtc(BigDecimal montantNetTtc) {
        this.montantNetTtc = montantNetTtc;
    }

    public BigDecimal getMontantTva() {
        return montantTva;
    }

    public void setMontantTva(BigDecimal montantTva) {
        this.montantTva = montantTva;
    }
}
