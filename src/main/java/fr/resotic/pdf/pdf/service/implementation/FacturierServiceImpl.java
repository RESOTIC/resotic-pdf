package fr.resotic.pdf.pdf.service.implementation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.kernel.utils.PdfMerger;
import com.itextpdf.kernel.xmp.XMPMeta;
import com.itextpdf.kernel.xmp.XMPMetaFactory;
import com.itextpdf.pdfa.PdfADocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierPdf;
import fr.resotic.pdf.pdf.domain.facturier.FacturierPdfA;
import fr.resotic.pdf.pdf.itextfacture.ItextEfacGeneratorService;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import fr.resotic.pdf.pdf.service.FacturierService;
import fr.resotic.pdf.pdf.util.Aes128Coder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FacturierServiceImpl implements FacturierService {

    @Value("${application.token_key}")
    private String tokenKey;

    @Value("${echange_path}")
    private String echangePath;

    @Value("${icmPath}")
    private String icmPath;

    private final ItextEfacGeneratorService itextEfacGeneratorService;

    @Autowired
    public FacturierServiceImpl(ItextEfacGeneratorService itextEfacGeneratorService) {
        this.itextEfacGeneratorService = itextEfacGeneratorService;
    }

    @Override
    public void generatePdfFacturier(String body) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        FacturierPdf pdf = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), FacturierPdf.class);

        itextEfacGeneratorService.generateFacture(pdf);
    }

    @Override
    public void generatePdfA(String body) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        FacturierPdfA pdfA = mapper.readValue(Aes128Coder.decrypt(tokenKey, body), FacturierPdfA.class);

        String attachementName = "factur-x.xml";
        DocumentProperties properties = new DocumentProperties();

        // create Pdf-A
        InputStream is = Files.newInputStream(Paths.get(echangePath + icmPath));
        PdfADocument pdf = new PdfADocument(new PdfWriter(pdfA.getDest()), PdfAConformanceLevel.PDF_A_3B,
                new PdfOutputIntent("Resotic", "", "http://www.color.org", "sRGB IEC61966-2.1", is), properties);

        // valid XMP
        String meta =
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">" +
                        "    <rdf:Description xmlns:pdfaid=\"http://www.aiim.org/pdfa/ns/id/\" rdf:about=\"\">" +
                        "      <pdfaid:part>3</pdfaid:part>" +
                        "      <pdfaid:conformance>B</pdfaid:conformance>" +
                        "    </rdf:Description>" +
                        "    <rdf:Description xmlns:dc=\"http://purl.org/dc/elements/1.1/\" rdf:about=\"\">" +
                        "    </rdf:Description>" +
                        "    <rdf:Description xmlns:pdf=\"http://ns.adobe.com/pdf/1.3/\" rdf:about=\"\">" +
                        "      <pdf:Producer>RESOTIC</pdf:Producer>" +
                        "    </rdf:Description>" +
                        "    <rdf:Description xmlns:xmp=\"http://ns.adobe.com/xap/1.0/\" rdf:about=\"\">" +
                        "      <xmp:CreatorTool>RESOTIC</xmp:CreatorTool>" +
                        "      <xmp:CreateDate>2019-06-11T23:18:01+00:00</xmp:CreateDate>" +
                        "      <xmp:ModifyDate>2019-06-11T23:18:01+00:00</xmp:ModifyDate>" +
                        "    </rdf:Description>" +
                        "    <rdf:Description xmlns:pdfaExtension=\"http://www.aiim.org/pdfa/ns/extension/\" xmlns:pdfaSchema=\"http://www.aiim.org/pdfa/ns/schema#\" xmlns:pdfaProperty=\"http://www.aiim.org/pdfa/ns/property#\" rdf:about=\"\">" +
                        "      <pdfaExtension:schemas>" +
                        "        <rdf:Bag>" +
                        "          <rdf:li rdf:parseType=\"Resource\">" +
                        "            <pdfaSchema:schema>Factur-X PDFA Extension Schema</pdfaSchema:schema>" +
                        "            <pdfaSchema:namespaceURI>urn:factur-x:pdfa:CrossIndustryDocument:invoice:1p0#</pdfaSchema:namespaceURI>" +
                        "            <pdfaSchema:prefix>fx</pdfaSchema:prefix>" +
                        "            <pdfaSchema:property>" +
                        "              <rdf:Seq>" +
                        "                <rdf:li rdf:parseType=\"Resource\">" +
                        "                  <pdfaProperty:name>DocumentFileName</pdfaProperty:name>" +
                        "                  <pdfaProperty:valueType>Text</pdfaProperty:valueType>" +
                        "                  <pdfaProperty:category>external</pdfaProperty:category>" +
                        "                  <pdfaProperty:description>factur-x.xml</pdfaProperty:description>" +
                        "                </rdf:li>" +
                        "                <rdf:li rdf:parseType=\"Resource\">" +
                        "                  <pdfaProperty:name>DocumentType</pdfaProperty:name>" +
                        "                  <pdfaProperty:valueType>Text</pdfaProperty:valueType>" +
                        "                  <pdfaProperty:category>external</pdfaProperty:category>" +
                        "                  <pdfaProperty:description>INVOICE</pdfaProperty:description>" +
                        "                </rdf:li>" +
                        "                <rdf:li rdf:parseType=\"Resource\">" +
                        "                  <pdfaProperty:name>Version</pdfaProperty:name>" +
                        "                  <pdfaProperty:valueType>Text</pdfaProperty:valueType>" +
                        "                  <pdfaProperty:category>external</pdfaProperty:category>" +
                        "                  <pdfaProperty:description>1.0</pdfaProperty:description>" +
                        "                </rdf:li>" +
                        "                <rdf:li rdf:parseType=\"Resource\">" +
                        "                  <pdfaProperty:name>ConformanceLevel</pdfaProperty:name>" +
                        "                  <pdfaProperty:valueType>Text</pdfaProperty:valueType>" +
                        "                  <pdfaProperty:category>external</pdfaProperty:category>" +
                        "                  <pdfaProperty:description>EXTENDED</pdfaProperty:description>" +
                        "                </rdf:li>" +
                        "              </rdf:Seq>" +
                        "            </pdfaSchema:property>" +
                        "          </rdf:li>" +
                        "        </rdf:Bag>" +
                        "      </pdfaExtension:schemas>" +
                        "    </rdf:Description>" +
                        "    <rdf:Description xmlns:fx=\"urn:factur-x:pdfa:CrossIndustryDocument:invoice:1p0#\" rdf:about=\"\">" +
                        "      <fx:DocumentType>INVOICE</fx:DocumentType>" +
                        "      <fx:DocumentFileName>factur-x.xml</fx:DocumentFileName>" +
                        "      <fx:Version>1.0</fx:Version>" +
                        "      <fx:ConformanceLevel>EXTENDED</fx:ConformanceLevel>" +
                        "    </rdf:Description>" +
                        "  </rdf:RDF>";
        XMPMeta extMeta = XMPMetaFactory.parseFromString(meta);
        pdf.setXmpMetadata(extMeta);

        // create PdfMerger instance
        PdfMerger merger = new PdfMerger(pdf);
        // add pages from the first document
        PdfDocument firstSourcePdf = new PdfDocument(new PdfReader(pdfA.getSrc()));
        merger.merge(firstSourcePdf, 1, firstSourcePdf.getNumberOfPages());

        PdfDictionary parameters = new PdfDictionary();
        parameters.put(PdfName.ModDate, new PdfDate().getPdfObject());

        byte[] bytes = IOUtils.toByteArray(Files.newInputStream(Paths.get(pdfA.getAttachementPath())));
        PdfFileSpec fileSpec = PdfFileSpec.createEmbeddedFileSpec(pdf, bytes, attachementName, attachementName, new PdfName("text/xml"), parameters, PdfName.Data);
        fileSpec.put(new PdfName("AFRelationship"), new PdfName("Data"));

        pdf.addFileAttachment(attachementName, fileSpec);
        PdfArray array = new PdfArray();
        array.add(fileSpec.getPdfObject().getIndirectReference());
        pdf.getCatalog().put(new PdfName("AF"), array);

        // close the documents
        firstSourcePdf.close();
        pdf.close();
        is.close();

        // delete temp pdf
        File f = new File(pdfA.getSrc());
        if (f.exists()) {
            f.delete();
        }
    }

}
