package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;

import java.io.IOException;

public class FooterFacture extends ItextComponent {

    private final FooterCoupon footerCoupon;
    private final FooterCommentaires footerCommentaires;
    private final Boolean isCouponPresent;

    public FooterFacture(ItextCellFactory itextCellFactory, Boolean isCouponPresent) {
        super(itextCellFactory, 5f, 5f, new Table(isCouponPresent ? UnitValue.createPercentArray(new float[]{0.7f, 1f}) : UnitValue.createPercentArray(new float[]{1f})));
        view.useAllAvailableWidth();
        footerCoupon = new FooterCoupon(itextCellFactory);
        footerCommentaires = new FooterCommentaires(itextCellFactory);
        this.isCouponPresent = isCouponPresent;
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (isCouponPresent) {
            footerCoupon.init(facturierDocument);
            Cell coupon = new Cell().setBorder(Border.NO_BORDER).add(footerCoupon.getView()).setVerticalAlignment(VerticalAlignment.BOTTOM);
            view.addCell(coupon);
        }
        footerCommentaires.init(facturierDocument);
        Cell commentaires = new Cell().setBorder(Border.NO_BORDER).add(footerCommentaires.getView()).setVerticalAlignment(VerticalAlignment.BOTTOM);
        view.addCell(commentaires);
    }
}
