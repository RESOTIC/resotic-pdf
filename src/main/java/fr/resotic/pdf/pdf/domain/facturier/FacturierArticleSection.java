package fr.resotic.pdf.pdf.domain.facturier;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FacturierArticleSection {

    private Integer id;

    private FacturierModele modele;

    private String libelle;

    private Integer ordre;

    private List<FacturierArticleSectionFacturierArticle> articles = new ArrayList<>(0);

    private Boolean sousTotal;

    private BigDecimal totalHt;

    public FacturierArticleSection() {
        // constructeur par défaut
    }

    public String getLibellePDF() {
        if (!StringUtils.isEmpty(getLibelle())) {
            return getLibelle().replaceAll("\t", " ");
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public FacturierArticleSection setId(Integer id) {
        this.id = id;
        return this;
    }

    public FacturierModele getModele() {
        return modele;
    }

    public FacturierArticleSection setModele(FacturierModele modele) {
        this.modele = modele;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public FacturierArticleSection setLibelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public FacturierArticleSection setOrdre(Integer ordre) {
        this.ordre = ordre;
        return this;
    }

    public List<FacturierArticleSectionFacturierArticle> getArticles() {
        return articles;
    }

    public FacturierArticleSection setArticles(List<FacturierArticleSectionFacturierArticle> articles) {
        this.articles = articles;
        return this;
    }

    public Boolean getSousTotal() {
        return sousTotal;
    }

    public BigDecimal getTotalHt() {
        return totalHt;
    }
}
