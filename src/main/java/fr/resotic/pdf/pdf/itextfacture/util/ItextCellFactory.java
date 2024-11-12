package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.font.FontProvider;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItextCellFactory {

    private final ItextFontFactory itextFontFactory;
    private final String couleurFond;
    private final String couleurTexte;

    public ItextCellFactory(ItextFontFactory itextFontFactory, String couleurFond, String couleurTexte) {
        this.itextFontFactory = itextFontFactory;
        this.couleurFond = couleurFond;
        this.couleurTexte = couleurTexte;
    }

    public Cell createCellHeaderTitle(String title) {
        return new Cell().setPadding(0).setHorizontalAlignment(HorizontalAlignment.LEFT).setBorder(Border.NO_BORDER)
                .add(new Paragraph(title).setFont(itextFontFactory.getArial()).setFontSize(14).setMultipliedLeading(0.7f));
    }

    public Cell createCellHeaderProfilText(String text) {
        Cell cell = new Cell().add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(7).setMultipliedLeading(1f));
        cell.setPadding(0);
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell createCellHeaderClient(String text) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(11).setMultipliedLeading(0.7f));
    }

    public Cell createCellHeaderDiversClient(String text) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(7).setMultipliedLeading(1f));
    }

    public Cell createCommentaireFooter(String text) {
        return new Cell().add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(7).setPadding(0f).setMultipliedLeading(1f)).setBorder(Border.NO_BORDER);
    }

    public Cell getCellNoBorder(Table table, HorizontalAlignment alignment) {
        Cell cell = new Cell().add(table);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    public Cell getCellTableHeader(String text) {
        Cell cell = new Cell().add(new Paragraph(text).setFontColor(new DeviceRgb(Color.decode(couleurTexte))).setTextAlignment(TextAlignment.CENTER).setFont(itextFontFactory.getArialBold()).setFontSize(11));
        cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setBackgroundColor(new DeviceRgb(Color.decode(couleurFond)));
        cell.setHeight(35);
        return cell;
    }

    public Cell createTvaFooterHeader(String text) {
        return new Cell().add(new Paragraph(text).setFontColor(new DeviceRgb(Color.decode(couleurTexte))).setTextAlignment(TextAlignment.CENTER).setFont(itextFontFactory.getArialBold()).setFontSize(11)).setBackgroundColor(new DeviceRgb(Color.decode(couleurFond)));
    }

    public Cell createColspanFooterHeader(String text, int colspan) {
        return new Cell(1, colspan).add(new Paragraph(text).setTextAlignment(TextAlignment.CENTER).setFont(itextFontFactory.getArialBold()).setFontSize(11));
    }

    public Cell createTotalFooterCellHeader(String text) {
        return new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.LEFT).setFont(itextFontFactory.getArial()).setFontSize(11));
    }

    public Cell createLastTotalFooterCellHeader(String text) {
        Cell cell = new Cell().add(new Paragraph(text).setFontColor(new DeviceRgb(Color.decode(couleurTexte))).setTextAlignment(TextAlignment.LEFT).setFont(itextFontFactory.getArialBold()).setFontSize(11));
        cell.setBackgroundColor(new DeviceRgb(Color.decode(couleurFond)));
        return cell;
    }

    public Cell createCellFooterValue(String text) {
        return new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.RIGHT).setFont(itextFontFactory.getArial()).setFontSize(10));
    }

    public Cell createCellColspanFooterValue(String text, int colspan) {
        return new Cell(1, colspan).add(new Paragraph(text).setTextAlignment(TextAlignment.RIGHT).setFont(itextFontFactory.getArial()).setFontSize(10));
    }

    public Cell createCellFooterText(String text) {
        return new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.LEFT).setFont(itextFontFactory.getArial()).setFontSize(10));
    }

    public Cell createLastCellFooterValue(String text) {
        Cell cell = new Cell().add(new Paragraph(text).setTextAlignment(TextAlignment.RIGHT).setFontColor(new DeviceRgb(Color.decode(couleurTexte))).setFont(itextFontFactory.getArialBold()).setFontSize(10));
        cell.setBackgroundColor(new DeviceRgb(Color.decode(couleurFond)));
        return cell;
    }

    public Cell createCellBodyArticle(String text, Boolean isDesignationOrCommentaire, String commentaireText) {
        if (text == null) {
            text = "";
        }

        Paragraph p = new Paragraph().setFontSize(8).setMultipliedLeading(1f);
        Text t1 = new Text(text).setFont(itextFontFactory.getArial());
        p.add(t1);

        if (!StringUtils.isEmpty(commentaireText)) {
            Text t2 = new Text("\r" + commentaireText).setFont(itextFontFactory.getArialItalic());
            p.add(t2);
        }

        p.setTextAlignment(TextAlignment.RIGHT);
        if (isDesignationOrCommentaire) {
            p.setTextAlignment(TextAlignment.LEFT);
        }
        Cell cell = new Cell().add(p);
        cell.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setBorderTop(Border.NO_BORDER);
        cell.setBorderBottom(Border.NO_BORDER);
        cell.setMargins(0, 0, 0, 0);
        cell.setPaddings(5, 1, 0, 1);

        return cell;
    }

    public Cell createCellBodySection(String text) {
        if (text == null) {
            text = "";
        }
        Paragraph p = new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(8).setUnderline().setMultipliedLeading(1f);

        p.setTextAlignment(TextAlignment.LEFT);
        p.setMarginLeft(1f);
        Cell cell = new Cell().add(p);
        cell.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setBorderTop(Border.NO_BORDER);
        cell.setBorderBottom(Border.NO_BORDER);
        cell.setMargins(0, 0, 0, 0);
        cell.setPaddings(5, 1, 3, 1);
        return cell;
    }

    public Cell createCellBodySousTotal(String text) {
        if (text == null) {
            text = "";
        }
        Paragraph p = new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(8).setBold().setMultipliedLeading(1f);

        p.setTextAlignment(TextAlignment.RIGHT);
        p.setMarginLeft(1f);
        Cell cell = new Cell().add(p);
        cell.setVerticalAlignment(VerticalAlignment.TOP);
        cell.setBorderTop(Border.NO_BORDER);
        cell.setBorderBottom(Border.NO_BORDER);
        cell.setMargins(0, 0, 0, 0);
        cell.setPaddings(5, 1, 3, 1);
        return cell;
    }

    public Cell createCommentaireCell(String text) {
        Paragraph p = new Paragraph(text).setFont(itextFontFactory.getArialItalic()).setFontSize(8).setMultipliedLeading(1f);
        Cell cell = new Cell().add(p);
        cell.setBorderTop(Border.NO_BORDER);
        cell.setBorderBottom(Border.NO_BORDER);
        cell.setMargins(0, 0, 0, 0);
        cell.setPaddings(0, 0, 0, 2);
        return cell;
    }

    public List<Cell> fillRowWithEmptyCells(Integer queriedCells) {
        List<Cell> emptyCells = new ArrayList<>();
        for (int i = 0; i < queriedCells; i++) {
            emptyCells.add(new Cell().setBorderBottom(Border.NO_BORDER).setBorderTop(Border.NO_BORDER));
        }
        return emptyCells;
    }

    public Cell createCouponReglementLine() {
        Paragraph p = new Paragraph("Coupon à joindre à votre règlement :");
        p.setFont(itextFontFactory.getArial());
        p.setFontSize(7).setUnderline();
        return new Cell().add(p).setBorder(Border.NO_BORDER);
    }

    public Cell createCouponValueNormal(String text) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(7));
    }

    public Cell createCouponValueImportant(String text) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(text).setFont(itextFontFactory.getArialBold()).setFontSize(7));
    }

    public Cell createTexteDevis(String text) throws IOException {
        ConverterProperties properties = new ConverterProperties();

        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
        FontProgram fontProgram = FontProgramFactory.createFont(ItextFontFactory.ARIAL_PATH);
        fontProvider.addFont(fontProgram);
        properties.setFontProvider(fontProvider);

        Paragraph paragraph = new Paragraph();
        for (IElement element : HtmlConverter.convertToElements("<div style=\"font-size: 7pt;\">" + text + "</div>", properties)) {
            paragraph.setFont(itextFontFactory.getArial()).setFontSize(7).add((IBlockElement) element);
        }
        return new Cell().setBorder(Border.NO_BORDER).add(paragraph).setFont(itextFontFactory.getArial()).setFontSize(7);
    }

    public Table createBLTextFooter() {
        Cell livraison = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Livré le:")).setFont(itextFontFactory.getArial()).setFontSize(10);
        Cell signature = new Cell().setBorder(Border.NO_BORDER).add(new Paragraph("Signature:")).setFont(itextFontFactory.getArial()).setFontSize(10);
        Table table = new Table(UnitValue.createPercentArray(new float[]{1})).setBorder(Border.NO_BORDER);
        table.addCell(livraison);
        table.addCell(signature);
        return table;
    }

    public Cell createProfilBancaireText(String text) {
        return new Cell().setBorder(Border.NO_BORDER).setPadding(0f).add(new Paragraph(text).setFont(itextFontFactory.getArial()).setFontSize(9));
    }

    public Cell createTypeDocumentHeader(String libelle) {
        return new Cell().setBorder(Border.NO_BORDER).add(new Paragraph(libelle.toUpperCase()).setFont(itextFontFactory.getArial()).setFontSize(14).setTextAlignment(TextAlignment.CENTER));
    }

    public Cell createHeaderReferenceLine(String text) {
        return new Cell().add(new Paragraph(text).setFont(itextFontFactory.getArialBold()).setFontSize(8)).setBorder(Border.NO_BORDER);
    }
}
