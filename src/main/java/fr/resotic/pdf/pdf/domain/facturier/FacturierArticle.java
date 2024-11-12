package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Article;
import org.apache.commons.lang3.StringUtils;

public class FacturierArticle {

    private Integer id;

    private Article article;

    private Integer decimalePdf;

    private Integer decimale;

    public FacturierArticle() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Integer getDecimalePdf() {
        return decimalePdf;
    }

    public void setDecimalePdf(Integer decimalePdf) {
        this.decimalePdf = decimalePdf;
    }

    public Integer getDecimale() {
        //minimum 2 décimales
        if (decimale == null || decimale < 2) {
            return 2;
        }
        return decimale;
    }

    public void setDecimale(Integer decimale) {
        this.decimale = decimale;
    }


    public String getInitials() {
        if (StringUtils.isNotBlank(article.getLibelle())) {
            return StringUtils.capitalize(article.getLibelle().substring(0, Math.min(2, article.getLibelle().length())).toLowerCase());
        }
        return "";
    }
}
