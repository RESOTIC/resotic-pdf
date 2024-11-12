package fr.resotic.pdf.pdf.service.implementation;

import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.domain.footer.FooterPdf;
import fr.resotic.pdf.pdf.domain.html.HtmlPdf;
import fr.resotic.pdf.pdf.service.PdfService;
import fr.resotic.pdf.pdf.util.PageOrientationsEventHandler;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    public PdfServiceImpl() {
    }

    @Override
    public byte[] generatePdf(OutputStream out, PdfReader reader, HtmlPdf pdf) throws IOException {
        PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);

        try (PdfDocument pdfDoc = new PdfDocument(reader, new PdfWriter(out))) {
            PageOrientationsEventHandler eventHandler = new PageOrientationsEventHandler();
            pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, eventHandler);

            Document doc = new Document(pdfDoc);
            PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.HELVETICA));
            Paragraph paragraph;
            if (pdf.isWatermark()) {
                paragraph = new Paragraph(pdf.getTextWaterMark()).setFont(font).setFontSize(80);
                for (int pageNum = 1; pageNum <= pdfDoc.getNumberOfPages(); pageNum++) {
                    PdfPage page = pdfDoc.getPage(pageNum);
                    Rectangle pageSize = page.getPageSize();
                    float x = (pageSize.getLeft() + pageSize.getRight()) / 2;
                    float y = (pageSize.getTop() + pageSize.getBottom()) / 2;
                    PdfCanvas over = new PdfCanvas(page);
                    over.saveState();
                    over.setExtGState(gs1);
                    doc.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 45);
                    over.restoreState();
                }
            }

            if (pdf.isEncochePliage()) {
                paragraph = new Paragraph("_").setFont(font).setFontSize(16);
                for (int pageNum = 1; pageNum <= pdfDoc.getNumberOfPages(); pageNum++) {
                    PdfPage page = pdfDoc.getPage(pageNum);
                    PdfCanvas over = new PdfCanvas(page);
                    over.saveState();
                    over.setExtGState(gs1);
                    doc.showTextAligned(paragraph, 10, 261, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
                    doc.showTextAligned(paragraph, 575, 261, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
                    doc.showTextAligned(paragraph, 10, 565, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
                    doc.showTextAligned(paragraph, 575, 565, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);
                    over.restoreState();
                }
            }

            if (pdf.isHeader()) {
                // pas de header sur la 1ere page
                for (int pageNum = 2; pageNum <= pdfDoc.getNumberOfPages(); pageNum++) {
                    paragraph = new Paragraph(pdf.getHeaderText()).setFont(font).setFontSize(9);
                    PdfPage page = pdfDoc.getPage(pageNum);
                    PdfCanvas over = new PdfCanvas(page);
                    over.saveState();
                    over.setExtGState(gs1);
                    doc.showTextAligned(paragraph, 20, page.getPageSize().getHeight() - 10, pageNum, TextAlignment.LEFT, VerticalAlignment.TOP, 0);
                    over.restoreState();
                }
            }

            if (pdf.isFooter()) {
                generateFooter(pdfDoc, doc, gs1, font, pdf.getFooterText());
            }

            if (pdf.isLandscape()) {
                eventHandler.setOrientation(new PdfNumber(90));
            }
        }

        return ((ByteArrayOutputStream) out).toByteArray();
    }

    @Override
    public byte[] concatenatePdf(List<File> files) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(byteArrayOutputStream));
        PdfMerger merger = new PdfMerger(pdf);

        PdfDocument source;
        for (File file : files) {
            source = new PdfDocument(new PdfReader(file));
            merger.merge(source, 1, source.getNumberOfPages());
            source.close();
        }
        pdf.close();
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void addFooterToPdf(FooterPdf footerPdf) throws IOException {
        PdfExtGState gs1 = new PdfExtGState().setFillOpacity(0.5f);

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(footerPdf.getSrc()), new PdfWriter(footerPdf.getDest()))) {
            PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.TIMES_ROMAN));
            Document doc = new Document(pdfDoc);

            generateFooter(pdfDoc, doc, gs1, font, footerPdf.getFooterText());
        }
    }

    private void generateFooter(PdfDocument pdfDoc, Document doc, PdfExtGState gs, PdfFont font, String footerText) {
        Paragraph paragraph;
        for (int pageNum = 1; pageNum <= pdfDoc.getNumberOfPages(); pageNum++) {
            paragraph = new Paragraph(footerText + String.format("Page %s / %s", pageNum, pdfDoc.getNumberOfPages())).setFont(font).setFontSize(9);
            PdfPage page = pdfDoc.getPage(pageNum);
            PdfCanvas over = new PdfCanvas(page);
            over.saveState();
            over.setExtGState(gs);
            doc.showTextAligned(paragraph, page.getPageSize().getWidth() - 20, 16, pageNum, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
            over.restoreState();
        }
    }

    @Override
    public byte[] generateWithCustomWidth(HtmlPdf pdf) throws IOException {
        float defaultMargin = 28.3464f;
        float defaultPageHeight = PageSize.A4.getHeight() - defaultMargin;

        float margin = defaultMargin;
        float height = defaultPageHeight;

        // pdf sur une seule page (mais taille A4 minimum pour qu'en impression classique il n'y ait pas de zoom)
        if (BooleanUtils.isTrue(pdf.getOnlyOnePage())) {
            height = calculateDocumentHeight(pdf);
            margin = 0;
            if (height < defaultPageHeight) {
                height = defaultPageHeight;
            }
        }

        ByteArrayOutputStream finalOut = new ByteArrayOutputStream();
        PdfDocument finalPdfDoc = new PdfDocument(new PdfWriter(finalOut));
        finalPdfDoc.setDefaultPageSize(new PageSize(pdf.getCustomWidth(), height));
        Document document = new Document(finalPdfDoc);
        document.setMargins(margin, 0, margin, 0);
        document.add(getContent(pdf));
        document.close();

        return finalOut.toByteArray();
    }

    private float calculateDocumentHeight(HtmlPdf pdf) {
        // on crée un premier document qui sert de renderer pour déterminer la height finale
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(out));
        pdfDoc.setDefaultPageSize(new PageSize(pdf.getCustomWidth(), 10000));

        Document document = new Document(pdfDoc);
        document.setMargins(0, 0, 0, 0);
        Div div = getContent(pdf);

        // on calcule la height de la div
        LayoutResult layoutResult = div.createRendererSubTree().setParent(document.getRenderer()).layout(new LayoutContext(new LayoutArea(1, new Rectangle(0, 0, pdf.getCustomWidth(), 10000))));
        return layoutResult.getOccupiedArea().getBBox().getHeight();
    }

    private Div getContent(HtmlPdf pdf) {
        Div div = new Div();
        div.setMargin(0);
        div.setPadding(0);

        // on ajoute le contenu venant du html dans cette div
        for (IElement element : HtmlConverter.convertToElements(pdf.getHtmlDoc())) {
            div.add((IBlockElement) element);
        }

        return div;
    }
}
