package fr.resotic.pdf.pdf.service.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.resotic.pdf.pdf.domain.footer.FooterPdf;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import fr.resotic.pdf.pdf.service.FooterService;
import fr.resotic.pdf.pdf.service.PdfService;
import fr.resotic.pdf.pdf.util.Aes128Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FooterServiceImpl implements FooterService {

    @Value("${application.token_key}")
    private String tokenKey;

    private final PdfService pdfService;

    @Autowired
    public FooterServiceImpl(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public void addFooterToPdf(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        FooterPdf pdf = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), FooterPdf.class);

        pdfService.addFooterToPdf(pdf);
    }
}
