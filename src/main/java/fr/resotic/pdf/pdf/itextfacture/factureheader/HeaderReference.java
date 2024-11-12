package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class HeaderReference extends ItextComponent {

    private static final Map<Integer, String> typeVente = new HashMap<>();
    static {
        typeVente.put(0, "Livraison de biens");
        typeVente.put(1, "Prestation de services");
        typeVente.put(2, "Opération mixte");
    }

    public HeaderReference(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1})));
        view.useAllAvailableWidth();
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_VALORISE) || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
            view.addCell(itextCellFactory.createTypeDocumentHeader("BON DE LIVRAISON").setPaddingBottom(10f));
        } else if (facturierDocument.getType().getId().equals(FacturierType.TYPE_FACTURE)) {
            view.addCell(itextCellFactory.createTypeDocumentHeader("FACTURE").setPaddingBottom(10f));
        } else if (facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_LIBRE) || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_TOTAL)) {
            view.addCell(itextCellFactory.createTypeDocumentHeader("FACTURE D'AVOIR").setPaddingBottom(10f));
        } else {
            view.addCell(itextCellFactory.createTypeDocumentHeader(facturierDocument.getType().getLibelle()).setPaddingBottom(10f));
        }
        view.addCell(createTableContent(facturierDocument).setBorder(Border.NO_BORDER));
    }

    private Cell createTableContent(FacturierDocument facturierDocument) throws IOException {
        Table content = new Table(UnitValue.createPercentArray(new float[]{0.80f, 1.20f}));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(facturierDocument.getRelances() != null && !facturierDocument.getRelances().isEmpty()) {
            content.addCell(itextCellFactory.createHeaderReferenceLine("Relance n°" + facturierDocument.getRelances().size() + " en date du "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(dateFormat.format(facturierDocument.getRelance().getDate())));
        }

        content.addCell(itextCellFactory.createHeaderReferenceLine("Référence : "));
        content.addCell(itextCellFactory.createHeaderReferenceLine(facturierDocument.getReference()));

        if(facturierDocument.getTypeVente() != null) {
            content.addCell(itextCellFactory.createHeaderReferenceLine("Type d'opération : "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(typeVente.get(facturierDocument.getTypeVente())));
        }

        if (facturierDocument.getDocumentReference() != null && StringUtils.isEmpty(facturierDocument.getBlsReferents())) {
            content.addCell(itextCellFactory.createHeaderReferenceLine("Document de référence : "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(facturierDocument.getDocumentReference().getReference()));
        }
        if (!StringUtils.isEmpty(facturierDocument.getBlsReferents())) {
            content.addCell(itextCellFactory.createHeaderReferenceLine("Document de référence : "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(facturierDocument.getBlsReferents()));
        }

        if (!StringUtils.isEmpty(facturierDocument.getTitre())) {
            content.addCell(itextCellFactory.createHeaderReferenceLine("Titre :"));
            content.addCell(itextCellFactory.createHeaderReferenceLine(facturierDocument.getTitre()));
        }

        if (facturierDocument.getDate() != null) {
            String date = "Date";
            if (facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_VALORISE) || facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
                date += " de livraison";
            }
            content.addCell(itextCellFactory.createHeaderReferenceLine(date + " : "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(dateFormat.format(facturierDocument.getDate())));
        }

        if ((facturierDocument.getType().getId().equals(FacturierType.TYPE_DEVIS) || facturierDocument.getType().getId().equals(FacturierType.TYPE_FACTURE)) && facturierDocument.getModele().getEcheanceType() != null) {
            content.addCell(itextCellFactory.createHeaderReferenceLine(facturierDocument.getType().getId().equals(FacturierType.TYPE_DEVIS) ? "Validité : " : "Échéance : "));
            content.addCell(itextCellFactory.createHeaderReferenceLine(getEcheance(facturierDocument)));
        }

        return new Cell().add(content);
    }

    private String getEcheance(FacturierDocument facturierDocument) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if (facturierDocument.getModele().getEcheanceType().equals(FacturierDocument.OPTION_PAIEMENT_JOUR)) {
            return facturierDocument.getModele().getEcheanceNbJour() + " jours - " + (facturierDocument.getModele().getEcheanceDate() != null ? dateFormat.format(facturierDocument.getModele().getEcheanceDate()) : "");
        } else if (facturierDocument.getModele().getEcheanceType().equals(FacturierDocument.OPTION_PAIEMENT_DATE)) {
            return facturierDocument.getModele().getEcheanceDate() != null ? dateFormat.format(facturierDocument.getModele().getEcheanceDate()) : "";
        } else {
            return "À la réception de la facture";
        }
    }
}
