package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class FooterCoupon extends ItextComponent {

    public FooterCoupon(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1f})));
        view.useAllAvailableWidth();
        view.setBorder(new DashedBorder(1f));
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        view.addCell(itextCellFactory.createCouponReglementLine());
        Cell infoTable = new Cell().setBorder(Border.NO_BORDER);
        infoTable.add(createInfoTable(facturierDocument));
        view.addCell(infoTable);
    }

    private Table createInfoTable(FacturierDocument facturierDocument) throws IOException {
        Table infos = new Table(UnitValue.createPercentArray(new float[]{0.5f, 1f}));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        infos.addCell(itextCellFactory.createCouponValueNormal("Référence :"));
        infos.addCell(itextCellFactory.createCouponValueImportant(facturierDocument.getReference()));

        infos.addCell(itextCellFactory.createCouponValueNormal("Date :"));
        infos.addCell(itextCellFactory.createCouponValueNormal(facturierDocument.getDate() != null ? dateFormat.format(facturierDocument.getDate()) : ""));

        infos.addCell(itextCellFactory.createCouponValueNormal("Montant TTC :"));
        infos.addCell(itextCellFactory.createCouponValueImportant(String.format("%.2f", facturierDocument.getTotalTtcSubAcompte()) + " €"));

        return infos;
    }
}
