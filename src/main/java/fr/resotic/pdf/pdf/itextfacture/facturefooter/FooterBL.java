package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;

public class FooterBL extends ItextComponent {

    public FooterBL(ItextCellFactory itextCellFactory){
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1})));
        view.useAllAvailableWidth();
    }

    public void init(){
        view.addCell(new Cell().add(itextCellFactory.createBLTextFooter()).setBorder(Border.NO_BORDER));
    }
}
