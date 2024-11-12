package fr.resotic.pdf.pdf.service;

import com.itextpdf.kernel.pdf.PdfReader;
import fr.resotic.pdf.pdf.domain.footer.FooterPdf;
import fr.resotic.pdf.pdf.domain.html.HtmlPdf;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface PdfService {

    byte[] generatePdf(OutputStream out, PdfReader reader, HtmlPdf pdf) throws IOException;

    byte[] concatenatePdf(List<File> files) throws IOException;

    void addFooterToPdf(FooterPdf pdf) throws IOException;

    byte[] generateWithCustomWidth(HtmlPdf pdf) throws IOException;

}
