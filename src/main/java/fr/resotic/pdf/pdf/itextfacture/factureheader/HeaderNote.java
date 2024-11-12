package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class HeaderNote extends ItextComponent {

    public HeaderNote(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 0f, 0f, new Table(UnitValue.createPercentArray(new float[]{1})));
        view.setHeight(20f);
        view.useAllAvailableWidth();
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (!StringUtils.isEmpty(facturierDocument.getNote())) {
            view.addCell(itextCellFactory.createCommentaireFooter(facturierDocument.getNote()));
        }
    }
}
