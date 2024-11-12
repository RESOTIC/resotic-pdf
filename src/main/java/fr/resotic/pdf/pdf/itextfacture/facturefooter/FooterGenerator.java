package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.IRenderer;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.ItextEfacGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.FacturePartGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;

public class FooterGenerator implements FacturePartGenerator {

    private final FooterAcompte footerAcompte;
    private final FooterSolde footerSolde;
    private final FooterFacture footerFacture;
    private final FooterDevis footerDevis;
    private final FooterBL footerBL;
    private final FooterInfoBancaire footerInfoBancaire;

    private FacturierDocument facturierDocument;

    public FooterGenerator(ItextCellFactory itextCellFactory, Boolean isCouponPresent) {
        footerAcompte = new FooterAcompte(itextCellFactory);
        footerSolde = new FooterSolde(itextCellFactory);
        footerFacture = new FooterFacture(itextCellFactory, isCouponPresent);
        footerDevis = new FooterDevis(itextCellFactory);
        footerBL = new FooterBL(itextCellFactory);
        footerInfoBancaire = new FooterInfoBancaire(itextCellFactory);
    }

    @Override
    public void addContentToDocument(Document document, FacturierDocument facturierDocument, byte[] logo, Boolean isFixedPosition) throws IOException {
        document.add(createFooterTable(document, isFixedPosition));
    }

    @Override
    public ItextComponent initContent(Document document, FacturierDocument facturierDocument) throws IOException {
        this.facturierDocument = facturierDocument;
        if (facturierDocument.hasAcompteV2()) {
            footerAcompte.init(facturierDocument);
        }
        if (!FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId())) {
            footerSolde.init(facturierDocument);
        }
        footerInfoBancaire.init(facturierDocument);
        initFooterType();
        return null;
    }

    private Table createFooterTable(Document document, Boolean isFixedPosition) throws IOException {
        Table footerView = new Table(1).setBorder(Border.NO_BORDER);
        Cell solde = new Cell().setBorder(Border.NO_BORDER).add(footerSolde.getView()).setPadding(0);
        Cell infoBancaire = new Cell().setBorder(Border.NO_BORDER).add(footerInfoBancaire.getView()).setPadding(0);

        if (facturierDocument.hasAcompteV2()) {
            Cell acomptes = new Cell().setBorder(Border.NO_BORDER).add(footerAcompte.getView()).setPadding(0).setPaddingBottom(20);
            footerView.addCell(acomptes);
        }

        footerView.addCell(solde);
        footerView.addCell(infoBancaire);
        footerView.addCell(getFooterFromType());

        //si footer ne tient pas dans la page alors on le cole en dessous des articles
        if (BooleanUtils.isTrue(isFixedPosition)) {
            float firstPageBanniereSize = 0;
            if (document.getPdfDocument().getNumberOfPages() == 1) {
                if (facturierDocument.getModele().getBanniere() != null && !facturierDocument.getModele().getBanniere().isEmpty()) {
                    firstPageBanniereSize = ItextEfacGenerator.BANNIERE;
                } else if (!BooleanUtils.isTrue(facturierDocument.getModele().getIsLogoIndep()) && facturierDocument.getProfil().getBanniere() != null && !facturierDocument.getProfil().getBanniere().isEmpty()) {
                    firstPageBanniereSize = ItextEfacGenerator.BANNIERE;
                }
            }

            footerView.setFixedPosition(document.getLeftMargin(), document.getBottomMargin() + firstPageBanniereSize /*+ ItextEfacGenerator.NB_PAGE*/, document.getPdfDocument().getDefaultPageSize().getWidth() - document.getLeftMargin() - document.getRightMargin());
        }
        return footerView;
    }

    @Override
    public float calculateHeight(Document document, FacturierDocument facturierDocument) throws IOException {
        Table footerToCompute = createFooterTable(document, true);
        IRenderer tableRenderer = footerToCompute.createRendererSubTree().setParent(document.getRenderer());

        LayoutResult tableLayoutResult =
                tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(document.getPdfDocument().getDefaultPageSize().getWidth(), 1000))));

        return tableLayoutResult.getOccupiedArea().getBBox().getHeight();
    }

    private void initFooterType() throws IOException {
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_BL)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_VALORISE)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
            footerBL.init();
        }
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_FACTURE)) {
            footerFacture.init(facturierDocument);
        }
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_LIBRE)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_TOTAL)) {
            footerFacture.init(facturierDocument);
        }
        if (FacturierType.TYPE_DEVIS.equals(facturierDocument.getType().getId())) {
            footerDevis.init(facturierDocument);
        }
    }

    private Cell getFooterFromType() {
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_BL)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_VALORISE)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
            return new Cell().add(footerBL.getView()).setBorder(Border.NO_BORDER).setPadding(0);
        }
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_FACTURE)) {
            return new Cell().add(footerFacture.getView()).setBorder(Border.NO_BORDER).setPadding(0);
        }
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_LIBRE)
                || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_TOTAL)) {
            return new Cell().add(footerFacture.getView()).setBorder(Border.NO_BORDER).setPadding(0);
        }
        if (FacturierType.TYPE_DEVIS.equals(facturierDocument.getType().getId())) {
            return new Cell().add(footerDevis.getView()).setBorder(Border.NO_BORDER).setPadding(0);
        }
        return null;
    }
}
