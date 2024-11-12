package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierAcompteForm;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import fr.resotic.pdf.pdf.util.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;

public class FooterDevis extends ItextComponent {

    private final FooterCommentaires footerCommentaires;

    public FooterDevis(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1f})));
        view.useAllAvailableWidth();
        footerCommentaires = new FooterCommentaires(itextCellFactory);
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (!StringUtils.isEmpty(facturierDocument.getModele().getTexteDevisCleanHtml())) {
            Cell texteDevis = itextCellFactory.createTexteDevis(facturierDocument.getModele().getTexteDevisCleanHtml());
            view.addCell(texteDevis);
        }

        if (facturierDocument.hasAcompte()) {
            BigDecimal totAcompte = new BigDecimal(0);
            for (FacturierAcompteForm acompte : facturierDocument.getAcomptes()) {
                totAcompte = totAcompte.add(acompte.getMontant());
            }
            Cell acompte = itextCellFactory.createTexteDevis("L’acceptation de ce devis, vous engage à verser un acompte de " + NumberUtil.formatNumberIfNumeric(totAcompte, 2));
            view.addCell(acompte);
            Cell acompteSuite = itextCellFactory.createTexteDevis("Merci de joindre le règlement au devis signé.");
            view.addCell(acompteSuite);
        }

        footerCommentaires.init(facturierDocument);
        Cell commentaires = new Cell().add(footerCommentaires.getView()).setBorder(Border.NO_BORDER);
        view.addCell(commentaires);
    }
}
