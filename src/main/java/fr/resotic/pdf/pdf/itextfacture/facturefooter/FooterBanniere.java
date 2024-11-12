package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import fr.resotic.pdf.pdf.itextfacture.ItextEfacGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;

import java.util.List;

public class FooterBanniere extends ItextComponent {
    public FooterBanniere(ItextCellFactory itextCellFactory, float spacingBefore, float spacingAfter, Table view) {
        super(itextCellFactory, spacingBefore, spacingAfter, view);
    }

    public void init(List<byte[]> bannieres) throws Exception {
        if (bannieres != null) {
            float[] tailleBanniere = new float[bannieres.size()];
            for (int i = 0; i < bannieres.size(); i++) {
                tailleBanniere[i] = 1f / bannieres.size();
            }
            Table tmp = new Table(UnitValue.createPercentArray(tailleBanniere)).setHeight(57f).setBorder(Border.NO_BORDER);
            for (byte[] logo : bannieres) {
                if (logo != null) {
                    Image image = new Image(ImageDataFactory.create(logo));
                    image.setAutoScale(true);
                    tmp.addCell(new Cell().add(image).setBorder(Border.NO_BORDER));
                }
            }
            view = tmp;
        } else {
            view = new Table(1).setHeight(ItextEfacGenerator.BANNIERE).setBorder(Border.NO_BORDER);
        }
    }
}
