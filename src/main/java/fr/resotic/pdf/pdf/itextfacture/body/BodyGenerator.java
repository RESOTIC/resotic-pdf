package fr.resotic.pdf.pdf.itextfacture.body;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.IRenderer;
import fr.resotic.pdf.pdf.domain.facturier.FacturierArticleSection;
import fr.resotic.pdf.pdf.domain.facturier.FacturierArticleSectionFacturierArticle;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.ItextEfacGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.FacturePartGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.Row;
import fr.resotic.pdf.pdf.util.NumberUtil;
import org.apache.commons.lang3.BooleanUtils;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BodyGenerator implements FacturePartGenerator {

    private final List<BodyTable> bodies;
    private final BodyArticleGenerator bodyArticleGenerator;
    private final ItextCellFactory itextCellFactory;
    private float[] WIDTHS;

    public BodyGenerator(ItextCellFactory itextCellFactory) {
        this.bodies = new ArrayList<>();
        this.bodyArticleGenerator = new BodyArticleGenerator(itextCellFactory);
        this.itextCellFactory = itextCellFactory;
    }

    @Override
    public void addContentToDocument(Document document, FacturierDocument facturierDocument, byte[] logo, Boolean isFixedPosition) {
        for (BodyTable body : bodies) {
            document.add(body.getView());
        }
    }

    public void fillContent(Document document, boolean isValorise, FacturierDocument facturierDocument, float footerHeight, boolean containsBannieres, Float contentHeight) throws Exception {
        initWidths(isValorise, isRemisePresente(facturierDocument), isExonerationTva(facturierDocument));
        BodyTable currentBody = initContent(document, facturierDocument);
        currentBody.getView().setMarginBottom(5f);
        List<Row> rows = generateArticleRows(facturierDocument);

        for (int index = 0; index < rows.size(); index++) {
            fillBodyViewRow(currentBody.getView(), rows.get(index).getCells());
        }
        if (contentHeight != null) {
            finalizeBodyContent(currentBody.getView(), footerHeight, contentHeight, facturierDocument);
        }

        bodies.add(currentBody);
    }

    public float getContentHeight(Document document) {
        LayoutArea currentArea = document.getRenderer().getCurrentArea();
        Rectangle rectangle = currentArea.getBBox();
        return rectangle.getHeight();
    }

    public void finalizeBodyContent(Table currentBody, float footerHeight, float contentHeight, FacturierDocument facturierDocument) throws IOException {
        float height;
        if (footerHeight > contentHeight) {
            float newPageHeight = PageSize.A4.getHeight() - footerHeight - ItextEfacGenerator.MARGIN_UP_DOWN - ItextEfacGenerator.HEADER_TABLE - 20;
            if (newPageHeight < 0) {
                height = 10;
            } else {
                height = contentHeight + newPageHeight;
            }
        } else {
            height = contentHeight - footerHeight;
        }

        // pour l'ajout de la ligne Total HT
        if(facturierDocument.hasAcompteV2()) {
            height = height - 25;
        }

        for (Cell cell : itextCellFactory.fillRowWithEmptyCells(WIDTHS.length)) {
            cell.setHeight(height);
            cell.setBorderBottom(new SolidBorder(1));
            currentBody.addCell(cell);
        }

        if(facturierDocument.hasAcompteV2()) {
            currentBody.addCell(itextCellFactory.createColspanFooterHeader("Total HT", 3));
            int colspann = isRemisePresente(facturierDocument) ? WIDTHS.length - 3 : WIDTHS.length - 4;
            currentBody.addCell(itextCellFactory.createCellColspanFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalHt(), 2), colspann).setVerticalAlignment(VerticalAlignment.MIDDLE));
        }
    }

    @Override
    public BodyTable initContent(Document document, FacturierDocument facturierDocument) throws IOException {
        BodyTable bodyTable = new BodyTable(itextCellFactory, WIDTHS);
        bodyTable.init(!FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId()), isRemisePresente(facturierDocument), isExonerationTva(facturierDocument), facturierDocument.getModele().getSaisieTtc());
        return bodyTable;
    }

    private Boolean isRemisePresente(FacturierDocument facturierDocument) throws IOException {
        for (FacturierArticleSection section : facturierDocument.getModele().getSections()) {
            for (FacturierArticleSectionFacturierArticle facturierArticle : section.getArticles()) {
                if (facturierArticle.getRemise() != null && facturierArticle.getRemise().compareTo(BigDecimal.ZERO) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Boolean isExonerationTva(FacturierDocument facturierDocument) throws IOException {
        return facturierDocument.getProfil().getExonerationTvaMention() != null;
    }

    private List<Row> generateArticleRows(FacturierDocument facturierDocument) throws IOException {
        List<Row> bodyRow = new ArrayList<>();
        boolean isValorise = !FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId());

        for (FacturierArticleSection section : facturierDocument.getModele().getSections()) {
            bodyRow.addAll(bodyArticleGenerator.createRowSection(section));

            for (FacturierArticleSectionFacturierArticle article : section.getArticles()) {
                bodyRow.addAll(bodyArticleGenerator.createArticleAndCommentaires(article, isValorise, isRemisePresente(facturierDocument), isExonerationTva(facturierDocument), facturierDocument.getModele().getSaisieTtc()));
            }

            if(BooleanUtils.isTrue(section.getSousTotal())) {
                bodyRow.add(bodyArticleGenerator.createRowSousTotal(section));
            }
        }

        return bodyRow;
    }

    private void fillBodyViewRow(Table view, List<Cell> cells) {
        for (Cell cell : cells) {
            view.addCell(cell);
        }
    }

    @Override
    public float calculateHeight(Document document, FacturierDocument facturierDocument) {
        float height = 0f;
        for (BodyTable bodyTable : bodies) {
            height += calculateHeight(document, bodyTable.getView());
        }

        return height;
    }

    private float calculateHeight(Document document, Table table) {
        IRenderer tableRenderer = table.createRendererSubTree().setParent(document.getRenderer());

        LayoutResult tableLayoutResult =
                tableRenderer.layout(
                        new LayoutContext(
                                new LayoutArea(0,
                                        document.getPageEffectiveArea(document.getPdfDocument().getDefaultPageSize()))));
        return tableLayoutResult.getOccupiedArea().getBBox().getHeight();
    }

    private void initWidths(Boolean isValorise, Boolean remisePresente, boolean exonerationTvaPresente) {
        if (!isValorise) {
            WIDTHS = new float[]{5f, 1f, 1f};
        } else {
            if (!remisePresente) {
                if (BooleanUtils.isFalse(exonerationTvaPresente)) {
                    WIDTHS = new float[]{2.4f, 0.3f, 0.2f, 0.6f, 0.5f, 0.4f};
                } else {
                    WIDTHS = new float[]{2.4f, 0.3f, 0.2f, 0.6f, 0.5f};
                }
            } else {
                if (BooleanUtils.isFalse(exonerationTvaPresente)) {
                    WIDTHS = new float[]{2.4f, 0.3f, 0.2f, 0.6f, 0.4f, 0.3f, 0.4f};
                } else {
                    WIDTHS = new float[]{2.4f, 0.3f, 0.2f, 0.6f, 0.4f, 0.3f};
                }

            }
        }
        bodyArticleGenerator.setColumns(WIDTHS.length);
    }


}
