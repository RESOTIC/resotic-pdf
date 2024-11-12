package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;

public class HeaderPart extends ItextComponent {


    public HeaderPart(ItextCellFactory itextCellFactory, float spacingBefore, float spacingAfter, Table view) {
        super(itextCellFactory, spacingBefore, spacingAfter, view);
    }

    protected void addEmptyRow() {
        view.addCell(new Cell().setBorder(Border.NO_BORDER));
    }
}
