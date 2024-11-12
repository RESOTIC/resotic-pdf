package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import fr.resotic.pdf.pdf.itextfacture.util.ImageAndPositionRenderer;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HeaderLogo extends HeaderPart {

    protected final Log logger = LogFactory.getLog(this.getClass());

    HeaderLogo(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(1));
        view.useAllAvailableWidth();
    }

    public void init(byte[] logo, Document document) throws Exception {
        Cell logoCell = new Cell().setBorder(Border.NO_BORDER).setHeight(170f);
        if (logo != null) {
            try {
                Image image = new Image(ImageDataFactory.create(logo));
                image.setAutoScale(true);
                logoCell = new Cell().setBorder(Border.NO_BORDER).setHeight(170f).setMarginLeft(14.1732f).setMarginRight(14.1732f);
                logoCell.setNextRenderer(new ImageAndPositionRenderer(logoCell, image));
            } catch (Exception e) {
                logger.error("Erreur logo PDF", e);
            }

        }
        view.addCell(logoCell);
    }
}
