package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.domain.facturier.FacturierAcompteForm;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import fr.resotic.pdf.pdf.util.NumberUtil;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;


public class FooterAcompte extends ItextComponent {

    private Table viewAcompte;

    private final float[] viewAcompteWidth = {2.4f, 1.3f, 1.3f, 1.3f};
    private final float[] viewAcompteWidthNoTva = {2.4f, 1.3f};

    public FooterAcompte(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1f})));
        view.useAllAvailableWidth();
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        this.initAcompteView(facturierDocument);
        view.addCell(itextCellFactory.getCellNoBorder(viewAcompte, HorizontalAlignment.LEFT)).setHorizontalAlignment(HorizontalAlignment.LEFT);
    }

    private void initAcompteView(FacturierDocument facturierDocument) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(facturierDocument.getProfil().getExonerationTvaMention() == null) {
            viewAcompte = new Table(UnitValue.createPercentArray(viewAcompteWidth)).useAllAvailableWidth().setBorder(Border.NO_BORDER);
        } else {
            viewAcompte = new Table(UnitValue.createPercentArray(viewAcompteWidthNoTva)).useAllAvailableWidth().setBorder(Border.NO_BORDER);
        }

        viewAcompte.setHorizontalAlignment(HorizontalAlignment.LEFT);

        viewAcompte.addCell(itextCellFactory.createTvaFooterHeader("Acompte(s) déjà facturé(s)"));
        viewAcompte.addCell(itextCellFactory.createTvaFooterHeader("Montant Net HT"));
        if(facturierDocument.getProfil().getExonerationTvaMention() == null) {
            viewAcompte.addCell(itextCellFactory.createTvaFooterHeader("Montant TVA"));
            viewAcompte.addCell(itextCellFactory.createTvaFooterHeader("Montant Net TTC"));
        }

        for (FacturierAcompteForm acompte : facturierDocument.getAcomptes()) {
            viewAcompte.addCell(itextCellFactory.createCellFooterText(acompte.getDocument().getReference() + " du " + sdf.format(acompte.getDate())));
            viewAcompte.addCell(itextCellFactory.createCellFooterValue((BooleanUtils.isTrue(acompte.getAvoir()) ? "-" : "") + NumberUtil.formatNumberIfNumeric(acompte.getMontantHt(), 2)).setVerticalAlignment(VerticalAlignment.MIDDLE));
            if(facturierDocument.getProfil().getExonerationTvaMention() == null) {
                viewAcompte.addCell(itextCellFactory.createCellFooterValue((BooleanUtils.isTrue(acompte.getAvoir()) ? "-" : "") + NumberUtil.formatNumberIfNumeric(acompte.getMontantTva(), 2)).setVerticalAlignment(VerticalAlignment.MIDDLE));
                viewAcompte.addCell(itextCellFactory.createCellFooterValue((BooleanUtils.isTrue(acompte.getAvoir()) ? "-" : "") + NumberUtil.formatNumberIfNumeric(acompte.getMontant(), 2)).setVerticalAlignment(VerticalAlignment.MIDDLE));
            }
        }
    }
}
