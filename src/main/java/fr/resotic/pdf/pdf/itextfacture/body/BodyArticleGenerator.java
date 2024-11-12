package fr.resotic.pdf.pdf.itextfacture.body;

import com.itextpdf.layout.element.Cell;
import fr.resotic.pdf.pdf.domain.facturier.FacturierArticleSection;
import fr.resotic.pdf.pdf.domain.facturier.FacturierArticleSectionFacturierArticle;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.Row;
import fr.resotic.pdf.pdf.util.NumberUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BodyArticleGenerator {
    private final ItextCellFactory itextCellFactory;
    private Integer columns;

    public BodyArticleGenerator(ItextCellFactory itextCellFactory) {
        this.itextCellFactory = itextCellFactory;
    }

    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public List<Row> createRowSection(FacturierArticleSection section) {
        List<Row> rows = new ArrayList<>();

        if (!StringUtils.isEmpty(section.getLibelle())) {
            rows.add(createSection(section.getLibellePDF()));
        }

        return rows;
    }

    public List<Row> createArticleAndCommentaires(FacturierArticleSectionFacturierArticle article, boolean isValorise, Boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc) {
        List<Row> articleAndCommentaires = new ArrayList<>();

        if (!StringUtils.isEmpty(article.getArticle().getArticle().getLibelle())) {
            articleAndCommentaires.add(createFirstLineArticle(article, isValorise, remisePresente, isExonerationTva, saisieTtc,
                    article.getArticle().getArticle().getLibellePDF(), article.getArticle().getArticle().getCommentairePDF()));
        }

        return articleAndCommentaires;
    }

    private Row createFirstLineArticle(FacturierArticleSectionFacturierArticle article, boolean isValorise, boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc, String articleText, String commentaireText) {
        List<Cell> articleCells = new ArrayList<>(this.createArticleNonValorise(article, articleText, commentaireText));
        if (isValorise) {
            createArticleValorise(article, articleCells, remisePresente, isExonerationTva, saisieTtc);
        }
        return new Row(articleCells);
    }

    private List<Cell> createArticleNonValorise(FacturierArticleSectionFacturierArticle facturierArticle, String articleText, String commentaireText) {
        List<Cell> cells = new ArrayList<>();
        cells.add(itextCellFactory.createCellBodyArticle(articleText, true, commentaireText));
        cells.add(itextCellFactory.createCellBodyArticle(NumberUtil.getDecimale(facturierArticle.getQuantite().doubleValue(), facturierArticle.getArticle().getDecimalePdf() == null ? 0 : facturierArticle.getArticle().getDecimalePdf()), false, null));
        cells.add(itextCellFactory.createCellBodyArticle(facturierArticle.getArticle().getArticle().getUnite(), false, null));
        return cells;
    }

    private void createArticleValorise(FacturierArticleSectionFacturierArticle article, List<Cell> cells, Boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc) {

        if ((BooleanUtils.isTrue(saisieTtc) && article.getArticle().getArticle().getPrixTtc() != null) || (BooleanUtils.isNotTrue(saisieTtc) && article.getArticle().getArticle().getPrixHt() != null)) {
            cells.add(itextCellFactory.createCellBodyArticle(NumberUtil.formatNumberIfNumeric(BooleanUtils.isTrue(saisieTtc) ? article.getArticle().getArticle().getPrixTtc() : article.getArticle().getArticle().getPrixHt(), article.getArticle().getDecimale()), false, null));
        } else {
            cells.add(itextCellFactory.createCellBodyArticle(" ", false, null));
        }

        if (remisePresente) {
            if (article.getRemise() != null && article.getRemise().compareTo(BigDecimal.ZERO) > 0) {
                cells.add(itextCellFactory.createCellBodyArticle(NumberUtil.formatNumberIfNumeric(article.getRemise(), 2, false) + "%", false, null));
            } else {
                cells.add(itextCellFactory.createCellBodyArticle(" ", false, null));
            }
        }

        /* Montant net Ht */
        if ((BooleanUtils.isTrue(saisieTtc) && article.getMontantNetTtc() != null) || (BooleanUtils.isNotTrue(saisieTtc) && article.getMontantNetHt() != null)) {
            cells.add(itextCellFactory.createCellBodyArticle(NumberUtil.formatNumberIfNumeric(BooleanUtils.isTrue(saisieTtc) ? article.getMontantNetTtc() : article.getMontantNetHt(), article.getArticle().getDecimale()), false, null));
        } else {
            cells.add(itextCellFactory.createCellBodyArticle(" ", false, null));
        }

        /* Taux TVA */
        if (BooleanUtils.isFalse(isExonerationTva)) {
            if (article.getArticle().getArticle().getTva() != null && article.getArticle().getArticle().getTva().getTaux() != null) {
                cells.add(itextCellFactory.createCellBodyArticle(String.format("%.2f", article.getArticle().getArticle().getTva().getTaux()) + "%", false, null));
            } else {
                cells.add(itextCellFactory.createCellBodyArticle(" ", false, null));
            }
        }
    }


    private Row createSection(String text) {
        List<Cell> cells = new ArrayList<>();
        cells.add(itextCellFactory.createCellBodySection(text));
        cells.addAll(itextCellFactory.fillRowWithEmptyCells(this.columns - 1));
        return new Row(cells);
    }

    public Row createRowSousTotal(FacturierArticleSection section) {
        List<Cell> cells = new ArrayList<>();
        cells.add(itextCellFactory.createCellBodySousTotal("Sous-total : " + NumberUtil.formatNumberIfNumeric(section.getTotalHt(), 2)));
        cells.addAll(itextCellFactory.fillRowWithEmptyCells(this.columns - 1));
        return new Row(cells);
    }

}
