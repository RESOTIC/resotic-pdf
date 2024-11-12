package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierCommentaire;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierModeleFacturierCommentaire;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FooterCommentaires extends ItextComponent {

    public FooterCommentaires(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1f})));
        view.useAllAvailableWidth();
        view.setBorder(Border.NO_BORDER);
        view.setMarginRight(20f);
        view.setPaddingBottom(20f);
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (facturierDocument.getProfil().getExonerationTvaMention() != null) {
            view.addCell(itextCellFactory.createCommentaireFooter(facturierDocument.getProfil().getExonerationTvaMention().getLibelle()));
        }

        List<FacturierModeleFacturierCommentaire> commentaireList = facturierDocument.getModele().getModeleCommentaires();
        if(commentaireList != null && !commentaireList.isEmpty()) {
            for (FacturierModeleFacturierCommentaire commentaire : commentaireList) {
                if (BooleanUtils.isTrue(commentaire.getCheck())) {
                    view.addCell(itextCellFactory.createCommentaireFooter(commentaire.getCommentaire().getLibelle()).setPadding(0f));
                }
            }
        }
    }
}
