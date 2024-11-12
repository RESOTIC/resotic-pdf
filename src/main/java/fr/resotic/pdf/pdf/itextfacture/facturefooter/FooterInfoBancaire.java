package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class FooterInfoBancaire extends ItextComponent {

    public FooterInfoBancaire(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1})));
        view.setBorder(Border.NO_BORDER);
        view.setPadding(0);
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        String bicValue = facturierDocument.getProfil().getBic();
        String ibanValue = facturierDocument.getProfil().getIban();
        if (facturierDocument.getRib() != null && facturierDocument.getRib().getId() != null) {
            bicValue = facturierDocument.getRib().getBic();
            ibanValue = facturierDocument.getRib().getIban();
        }
        Cell bic = null;
        Cell iban = null;
        if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilBic()) && !StringUtils.isEmpty(bicValue)) {
            bic = itextCellFactory.createProfilBancaireText("Coordonnées bancaires - BIC : " + bicValue);
        }

        if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilIban()) && !StringUtils.isEmpty(ibanValue)) {
            StringBuilder stringBuilder = new StringBuilder();
            if (bic == null) {
                stringBuilder.append("Coordonnées bancaires - ");
            }
            stringBuilder.append("IBAN : ").append(ibanValue);
            iban = itextCellFactory.createProfilBancaireText(stringBuilder.toString());
        }

        if (bic != null) {
            view.addCell(bic);
        }

        if (iban != null) {
            view.addCell(iban);
        }
    }
}
