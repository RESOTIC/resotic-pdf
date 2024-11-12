package fr.resotic.pdf.pdf.service.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.resotic.pdf.pdf.domain.concat.ConcatPdf;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import fr.resotic.pdf.pdf.service.ConcatService;
import fr.resotic.pdf.pdf.service.PdfService;
import fr.resotic.pdf.pdf.util.Aes128Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConcatServiceImpl implements ConcatService {

    @Value("${application.token_key}")
    private String tokenKey;

    private final PdfService pdfService;

    @Autowired
    public ConcatServiceImpl(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @Override
    public byte[] concatPdf(String body) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        ConcatPdf pdf = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), ConcatPdf.class);

        return pdfService.concatenatePdf(pdf.getFiles());
    }
}
