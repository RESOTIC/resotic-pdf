package fr.resotic.pdf.pdf.service.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfReader;
import fr.resotic.pdf.pdf.domain.html.HtmlPdf;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import fr.resotic.pdf.pdf.service.HtmlService;
import fr.resotic.pdf.pdf.service.PdfService;
import fr.resotic.pdf.pdf.util.Aes128Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class HtmlServiceImpl implements HtmlService {

    @Value("${application.token_key}")
    private String tokenKey;

    private final PdfService pdfService;

    @Autowired
    public HtmlServiceImpl(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public byte[] generatePdfFromHtml(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        HtmlPdf pdf = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), HtmlPdf.class);
        pdf.setHtmlDoc(pdf.getHtmlDoc().replaceAll("<br data-mce-bogus=\"1\">", "<br data-mce-bogus=\"1\"/>"));
        if (pdf.getCustomWidth() != null) {
            return pdfService.generateWithCustomWidth(pdf);
        } else {
            return generateFromHtmlNormal(pdf);
        }
    }

    private byte[] generateFromHtmlNormal(HtmlPdf pdf) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(pdf.getHtmlDoc(), out);
        PdfReader reader = new PdfReader(new ByteArrayInputStream(out.toByteArray()));
        return pdfService.generatePdf(out, reader, pdf);
    }

}
