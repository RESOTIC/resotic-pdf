package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.ItextEfacGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.FacturePartGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.StringUtils;

public class HeaderGenerator implements FacturePartGenerator {

    private final HeaderLogo headerLogo;
    private final HeaderProfil headerProfil;
    private final HeaderClient headerClient;
    private final HeaderReference headerReference;
    private final HeaderNote headerNote;
    private final Table generalHeaderView;
    private final Table noteView;

    public HeaderGenerator(ItextCellFactory itextCellFactory) {
        headerLogo = new HeaderLogo(itextCellFactory);
        headerProfil = new HeaderProfil(itextCellFactory);
        headerClient = new HeaderClient(itextCellFactory);
        headerReference = new HeaderReference(itextCellFactory);
        headerNote = new HeaderNote(itextCellFactory);
        generalHeaderView = new Table(UnitValue.createPercentArray(new float[]{0.85f, 1}));
        noteView = new Table(UnitValue.createPercentArray(new float[]{1}));
    }

    @Override
    public void addContentToDocument(Document document, FacturierDocument facturierDocument, byte[] logo, Boolean isFixedPosition) {
        addCellsToView(generalHeaderView, document, facturierDocument, logo);
        document.add(generalHeaderView);
        document.setBottomMargin(ItextEfacGenerator.MARGES_DOCUMENT + ItextEfacGenerator.NB_PAGE);

        if (!StringUtils.isEmpty(facturierDocument.getNote())) {
            noteView.addCell(new Cell().setBorder(Border.NO_BORDER).add(headerNote.getView()));
            document.add(noteView);
        }
    }

    @Override
    public ItextComponent initContent(Document document, FacturierDocument facturierDocument) {
        try {
            initContent(document, facturierDocument, null);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public float calculateHeight(Document document, FacturierDocument facturierDocument) {
        Table component = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
        addCellsToView(component, document, facturierDocument, null);
        LayoutResult result = component.createRendererSubTree().setParent(document.getRenderer()).layout(
                new LayoutContext(new LayoutArea(1, new Rectangle(0, 0, 595 - 28.3464f, 842 - 28.3464f))));
        return result.getOccupiedArea().getBBox().getHeight();
    }

    private void addCellsToView(Table table, Document document, FacturierDocument facturierDocument, byte[] logoImage) {
        Integer clientHeight = 185;
        Integer referenceHeight = 170;
        float clientBottom = 311.8110f;
        if (FacturierType.TYPE_BL.equals(facturierDocument.getType().getId())
                || FacturierType.TYPE_BL_VALORISE.equals(facturierDocument.getType().getId())
                || FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId())) {
            clientHeight = 245;
            clientBottom = 356.8110f;
            referenceHeight = 215;
        }

        Cell logo = new Cell().setHeight(150).setBorder(Border.NO_BORDER).add(headerLogo.getView()).setWidth(PageSize.A4.getWidth() / 2);
        Cell profil = new Cell().setHeight(150).setBorder(Border.NO_BORDER).add(headerProfil.getView()).setPaddingLeft(20).setWidth(PageSize.A4.getWidth() / 2);

        Cell client = new Cell().setHeight(clientHeight).setBorder(Border.NO_BORDER).add(headerClient.getView());
        Cell reference = new Cell().setHeight(referenceHeight).setBorder(Border.NO_BORDER).add(headerReference.getView());
        Cell clientBorder = new Cell().setOpacity(0f).setBorder(Border.NO_BORDER).setHeight(headerClient.calculateHeight(document) - 50f);

        if (logoImage == null) {
            table.addCell(profil);
            table.addCell(logo);
        } else {
            table.addCell(logo);
            table.addCell(profil);
        }

        table.addCell(reference);
        table.addCell(clientBorder);

        document.add(client.setFixedPosition(309.307f, 842 - clientBottom, 283.4645f));
    }

    public void initContent(Document document, FacturierDocument facturierDocument, byte[] logo) throws Exception {
        headerLogo.init(logo, document);
        headerProfil.init(facturierDocument);
        if (facturierDocument.getClient() != null && facturierDocument.getClient().getId() != null) {
            headerClient.init(facturierDocument);
        }
        headerReference.init(facturierDocument);
        headerNote.init(facturierDocument);
    }
}
