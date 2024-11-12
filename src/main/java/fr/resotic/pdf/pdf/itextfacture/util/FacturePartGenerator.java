package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.layout.Document;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;

import java.io.IOException;

public interface FacturePartGenerator {

    void addContentToDocument(Document document, FacturierDocument facturierDocument, byte[] logo, Boolean isFixedPosition) throws IOException;

    ItextComponent initContent(Document document, FacturierDocument facturierDocument) throws IOException;

    float calculateHeight(Document document, FacturierDocument facturierDocument) throws IOException;
}
