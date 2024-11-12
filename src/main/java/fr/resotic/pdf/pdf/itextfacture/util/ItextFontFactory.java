package fr.resotic.pdf.pdf.itextfacture.util;


import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;

import java.io.IOException;


public class ItextFontFactory {


    public static final String ARIAL_PATH = "/fonts/arial.ttf";
    private static final String ARIAL_ITALIC_PATH = "/fonts/arial-mt-italic.otf";
    private static final String ARIAL_BOLD_PATH = "/fonts/arial_bold.ttf";
    private static final String HELVETICA_PATH = "/fonts/Helvetica.ttf";

    private PdfFont ARIAL;
    private PdfFont ARIAL_ITALIC;
    private PdfFont ARIAL_BOLD;
    private PdfFont HELVETICA;

    public ItextFontFactory() {
    }

    public void init() throws IOException {
        ARIAL = PdfFontFactory.createFont(ItextFontFactory.class.getResource(ARIAL_PATH).toString(), true);
        ARIAL_ITALIC = PdfFontFactory.createFont(ItextFontFactory.class.getResource(ARIAL_ITALIC_PATH).toString(), true);
        ARIAL_BOLD = PdfFontFactory.createFont(ItextFontFactory.class.getResource(ARIAL_BOLD_PATH).toString(), true);
        HELVETICA = PdfFontFactory.createFont(ItextFontFactory.class.getResource(HELVETICA_PATH).toString(), true);
    }

    public PdfFont getArial() {
        return ARIAL;
    }

    public PdfFont getArialItalic() {
        return ARIAL_ITALIC;
    }

    public PdfFont getArialBold() {
        return ARIAL_BOLD;
    }

    public PdfFont getHelvetica() {
        return HELVETICA;
    }
}
