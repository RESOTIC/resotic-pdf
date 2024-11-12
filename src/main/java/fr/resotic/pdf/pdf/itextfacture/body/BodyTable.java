package fr.resotic.pdf.pdf.itextfacture.body;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import org.apache.commons.lang3.BooleanUtils;

public class BodyTable extends ItextComponent {

    public BodyTable(ItextCellFactory itextCellFactory, float[] widths) {
        super(itextCellFactory, 0f, 0f,
                new Table(UnitValue.createPercentArray(widths)));
    }

    public void init(boolean isStandard, Boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc) {
        this.createHeader(isStandard, remisePresente, isExonerationTva, saisieTtc);
        view.setKeepTogether(false).useAllAvailableWidth();
    }

    private void createHeader(boolean isStandard, Boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc) {
        if (isStandard) {
            createStandardHeader(remisePresente, isExonerationTva, saisieTtc);
        } else {
            createNonValoriseHeader();
        }
    }

    private void createNonValoriseHeader() {
        view.addHeaderCell(itextCellFactory.getCellTableHeader("Désignation"));
        view.addHeaderCell(itextCellFactory.getCellTableHeader("Quantité"));
        view.addHeaderCell(itextCellFactory.getCellTableHeader("Unité"));
    }

    private void createStandardHeader(Boolean remisePresente, Boolean isExonerationTva, Boolean saisieTtc) {
        createNonValoriseHeader();
        view.addHeaderCell(itextCellFactory.getCellTableHeader("Prix unitaire " + (BooleanUtils.isTrue(saisieTtc) ? "TTC" : "HT")));
        if (remisePresente) {
            view.addHeaderCell(itextCellFactory.getCellTableHeader("Remise"));
        }
        view.addHeaderCell(itextCellFactory.getCellTableHeader("Montant net " + (BooleanUtils.isTrue(saisieTtc) ? "TTC" : "HT")));
        if (BooleanUtils.isFalse(isExonerationTva)) {
            view.addHeaderCell(itextCellFactory.getCellTableHeader("Taux TVA"));
        }
    }
}
