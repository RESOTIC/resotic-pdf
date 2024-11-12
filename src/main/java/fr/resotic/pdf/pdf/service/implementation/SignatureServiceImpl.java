package fr.resotic.pdf.pdf.service.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.domain.signature.SignaturePdf;
import fr.resotic.pdf.pdf.domain.signature.SignaturePosition;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import fr.resotic.pdf.pdf.service.SignatureService;
import fr.resotic.pdf.pdf.util.Aes128Coder;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SignatureServiceImpl implements SignatureService {

    @Value("${application.token_key}")
    private String tokenKey;


    @Autowired
    public SignatureServiceImpl() {

    }

    @Override
    public void addSignatureToPdf(String body) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SignaturePdf pdf = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), SignaturePdf.class);
        SignaturePosition position = pdf.getPosition();


        PdfFont font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.HELVETICA));
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdf.getPath()), new PdfWriter(pdf.getPath() + "_2.pdf"));
        Document document = new Document(pdfDoc);
        PdfExtGState gState = new PdfExtGState().setFillOpacity(0.5f);

        ImageData data = ImageDataFactory.create(Base64.decodeBase64(pdf.getLogo()));

        for (int pageNum = 1; pageNum <= document.getPdfDocument().getNumberOfPages(); pageNum++) {
            if (pageNum == position.getPage()) {
                PdfPage page = document.getPdfDocument().getPage(pageNum);
                float x = position.getX() + position.getHeight() + 1;
                PdfCanvas over = new PdfCanvas(page);
                over.saveState();
                over.setExtGState(gState);

                over.addImage(data, position.getX(), position.getY(), position.getWidth(), true);

                Paragraph paragraph = new Paragraph(pdf.getNom()).setFont(font).setFontSize(7);
                float y = position.getY() + 38;
                document.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);

                paragraph = new Paragraph(pdf.getPrenom()).setFont(font).setFontSize(7);
                y = position.getY() + 28;
                document.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);

                paragraph = new Paragraph(pdf.getLieu()).setFont(font).setFontSize(7);
                y = position.getY() + 18;
                document.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);

                paragraph = new Paragraph(pdf.getDate()).setFont(font).setFontSize(7);
                y = position.getY() + 8;
                document.showTextAligned(paragraph, x, y, pageNum, TextAlignment.CENTER, VerticalAlignment.TOP, 0);

                over.restoreState();
            }
        }

        FileUtils.copyFile(new File(pdf.getPath() + "_2"), new File(pdf.getPath()));
        if (new File(pdf.getPath() + "_2").exists()) {
            FileUtils.forceDelete(new File(pdf.getPath() + "_2"));
        }
    }

}
