package fr.resotic.pdf.pdf.itextfacture;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.itextfacture.facturefooter.FooterBanniere;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextFontFactory;

import java.io.IOException;
import java.util.List;

public class DetailGenerator {

    private final ItextFontFactory itextFontFactory;
    private final ItextCellFactory itextCellFactory;

    public DetailGenerator(ItextFontFactory itextFontFactory, ItextCellFactory itextCellFactory) {
        this.itextFontFactory = itextFontFactory;
        this.itextCellFactory = itextCellFactory;
    }

    public void addWatermarkToDocument(Document document, String textWaterMark) throws IOException {
        PdfExtGState gState = new PdfExtGState().setFillOpacity(0.5f);

        Paragraph paragraph = new Paragraph(textWaterMark).setFont(itextFontFactory.getHelvetica()).setFontSize(80);
        for (int pageNum = 1; pageNum <= document.getPdfDocument().getNumberOfPages(); pageNum++) {
            PdfPage page = document.getPdfDocument().getPage(pageNum);
            Rectangle pageSize = page.getPageSize();
            float x = (pageSize.getLeft() - document.getLeftMargin() + pageSize.getRight() - document.getRightMargin()) / 2;
            float y = (pageSize.getTop() - document.getTopMargin() + pageSize.getBottom() - document.getBottomMargin()) / 2;
            PdfCanvas over = new PdfCanvas(page);
            over.saveState();
            over.setExtGState(gState);
            document.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 45);
            over.restoreState();
        }
    }

    public void addPliageToDocument(Document document) throws IOException {
        PdfExtGState gState = new PdfExtGState().setFillOpacity(0.5f);
        Paragraph paragraph = new Paragraph("_").setFont(itextFontFactory.getHelvetica()).setFontSize(16);

        for (int pageNum = 1; pageNum <= document.getPdfDocument().getNumberOfPages(); pageNum++) {
            PdfPage page = document.getPdfDocument().getPage(pageNum);
            PdfCanvas over = new PdfCanvas(page);
            over.saveState();
            over.setExtGState(gState);
            document.showTextAligned(paragraph, 10, 261, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            document.showTextAligned(paragraph, 575, 261, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            document.showTextAligned(paragraph, 10, 565, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            document.showTextAligned(paragraph, 575, 565, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
            over.restoreState();
        }
    }

    public void addNumeroPageToDocument(Document document, String signature, String reference) throws IOException {
        for (int pageNum = 1; pageNum <= document.getPdfDocument().getNumberOfPages(); pageNum++) {
            String paragraphText = signature + reference + " - Page " + pageNum + " / " + document.getPdfDocument().getNumberOfPages();

            PdfPage page = document.getPdfDocument().getPage(pageNum);
            Rectangle pageSize = page.getPageSize();

            Table view = new Table(1).setHeight(20).setBorder(Border.NO_BORDER);
            view.addCell(new Cell().add(new Paragraph(paragraphText).setFont(itextFontFactory.getHelvetica()).setFontSize(9)).setBorder(Border.NO_BORDER));
            Canvas canva = new Canvas(page, new Rectangle(pageSize.getRight() - document.getRightMargin() - 320, 14.1732f, 567f, 20f));
            canva.add(view);
        }
    }

    public void addBanniereToFirstPage(Document document, List<byte[]> banniere) throws Exception {
        FooterBanniere footerBanniere = new FooterBanniere(itextCellFactory, 5f, 5f, new Table(1));
        footerBanniere.init(banniere);
        PdfPage page = document.getPdfDocument().getPage(1);
        Canvas canva = new Canvas(page, new Rectangle(14.1732f, 29.1732f, 567f, ItextEfacGenerator.BANNIERE));
        canva.add(footerBanniere.getView());
    }
}
