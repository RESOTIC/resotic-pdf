package fr.resotic.pdf.pdf.itextfacture;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierInfos;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.body.BodyGenerator;
import fr.resotic.pdf.pdf.itextfacture.facturefooter.FooterGenerator;
import fr.resotic.pdf.pdf.itextfacture.factureheader.HeaderGenerator;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextFontFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ItextEfacGenerator {

    // LES MARGES SONT À DONNER EN PIXEL
    // Le document est tiré à 595*842 ce qui équivaut à un A4 - 72PPI
    // Pour trouver les valeurs en CM il faut prendre celle-ci, la diviser par 2,54 (conversion en inch) puis multiplier par 72 pour avoir sa valeur en pixel
    // ex : 0,5cm = 0,19685 inch = 14,1732px

    public static final float MARGES_DOCUMENT = 14.1732f;
    public static final float NB_PAGE = 20f;
    public static final float HEADER_TABLE = 35f;
    public static final float BANNIERE = 57f;
    public static final float MARGIN_UP_DOWN = 28.3464f;

    public ItextEfacGenerator() {
    }

    public void generateFacture(String pathFile, FacturierDocument facturierDocument, byte[] logoImage, List<byte[]> bannieres, String textWatermark, Boolean isCouponPresent, FacturierInfos facturierInfos) {
        try {
            float contentHeight = getContentHeight(pathFile, isCouponPresent, bannieres, facturierDocument, logoImage);

            generatePdf(pathFile, isCouponPresent, bannieres, facturierDocument, logoImage, contentHeight);

            setDetail(pathFile, textWatermark, bannieres, facturierDocument, facturierInfos);

            File fileToDelete = new File(pathFile + "_tmp2.pdf");
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
            fileToDelete = new File(pathFile + "_tmp.pdf");
            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getContentHeight(String pathFile, boolean isCouponPresent, List<byte[]> bannieres, FacturierDocument facturierDocument, byte[] logoImage) throws Exception {
        ItextFontFactory itextFontFactory = new ItextFontFactory();
        itextFontFactory.init();
        ItextCellFactory itextCellFactory = new ItextCellFactory(itextFontFactory, "#c0c0c0", "#000000");

        PdfDocument pdf = new PdfDocument(new PdfWriter(pathFile + "_tmp.pdf"));
        Document document = new Document(pdf, PageSize.A4, false);
        document.setMargins(MARGES_DOCUMENT, MARGES_DOCUMENT, MARGES_DOCUMENT + (bannieres.size() > 0 ? BANNIERE : 0) + NB_PAGE, MARGES_DOCUMENT);

        HeaderGenerator headerGenerator = new HeaderGenerator(itextCellFactory);
        BodyGenerator bodyGenerator = new BodyGenerator(itextCellFactory);
        FooterGenerator footerGenerator = new FooterGenerator(itextCellFactory, isCouponPresent);

        headerGenerator.initContent(document, facturierDocument, logoImage);
        footerGenerator.initContent(document, facturierDocument);
        float footerHeight = footerGenerator.calculateHeight(document, facturierDocument) + NB_PAGE;

        bodyGenerator.fillContent(document, !FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId()), facturierDocument, footerHeight, bannieres.size() > 0, null);

        headerGenerator.addContentToDocument(document, facturierDocument, logoImage, null);
        bodyGenerator.addContentToDocument(document, facturierDocument, logoImage, null);

        float contentHeight = bodyGenerator.getContentHeight(document);

        document.close();
        pdf.close();

        return contentHeight;
    }

    private void generatePdf(String pathFile, boolean isCouponPresent, List<byte[]> bannieres, FacturierDocument facturierDocument, byte[] logoImage, Float contentHeight) throws Exception {
        String couleurFond, couleurTexte;
        if (!StringUtils.isEmpty(facturierDocument.getModele().getCouleurFond())) {
            couleurFond = facturierDocument.getModele().getCouleurFond();
        } else if (!StringUtils.isEmpty(facturierDocument.getProfil().getCouleurFond())) {
            couleurFond = facturierDocument.getProfil().getCouleurFond();
        } else {
            couleurFond = "#c0c0c0";
        }

        if (!StringUtils.isEmpty(facturierDocument.getModele().getCouleurTexte())) {
            couleurTexte = facturierDocument.getModele().getCouleurTexte();
        } else if (!StringUtils.isEmpty(facturierDocument.getProfil().getCouleurTexte())) {
            couleurTexte = facturierDocument.getProfil().getCouleurTexte();
        } else {
            couleurTexte = "#000000";
        }

        ItextFontFactory itextFontFactory = new ItextFontFactory();
        itextFontFactory.init();
        ItextCellFactory itextCellFactory = new ItextCellFactory(itextFontFactory, couleurFond, couleurTexte);

        PdfDocument pdf = new PdfDocument(new PdfWriter(pathFile + "_tmp2.pdf"));
        Document document = new Document(pdf, PageSize.A4, false);
        document.setMargins(MARGES_DOCUMENT, MARGES_DOCUMENT, MARGES_DOCUMENT + (bannieres.size() > 0 ? BANNIERE : 0) + NB_PAGE, MARGES_DOCUMENT);

        BodyGenerator bodyGenerator = new BodyGenerator(itextCellFactory);
        FooterGenerator footerGenerator = new FooterGenerator(itextCellFactory, isCouponPresent);
        HeaderGenerator headerGenerator = new HeaderGenerator(itextCellFactory);

        headerGenerator.initContent(document, facturierDocument, logoImage);
        footerGenerator.initContent(document, facturierDocument);
        float footerHeight = footerGenerator.calculateHeight(document, facturierDocument) + NB_PAGE;

        bodyGenerator.fillContent(document, !FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId()), facturierDocument, footerHeight, bannieres.size() > 0, contentHeight);

        headerGenerator.addContentToDocument(document, facturierDocument, logoImage, null);
        bodyGenerator.addContentToDocument(document, facturierDocument, logoImage, null);
        footerGenerator.addContentToDocument(document, facturierDocument, logoImage, PageSize.A4.getHeight() - footerHeight - ItextEfacGenerator.MARGIN_UP_DOWN - ItextEfacGenerator.HEADER_TABLE - 20 > 0);

        document.close();
        pdf.close();
    }

    private void setDetail(String pathFile, String textWatermark, List<byte[]> bannieres, FacturierDocument facturierDocument, FacturierInfos facturierInfos) throws Exception {
        String couleurFond, couleurTexte;
        if (!StringUtils.isEmpty(facturierDocument.getModele().getCouleurFond())) {
            couleurFond = facturierDocument.getModele().getCouleurFond();
        } else if (!StringUtils.isEmpty(facturierDocument.getProfil().getCouleurFond())) {
            couleurFond = facturierDocument.getProfil().getCouleurFond();
        } else {
            couleurFond = "#c0c0c0";
        }

        if (!StringUtils.isEmpty(facturierDocument.getModele().getCouleurTexte())) {
            couleurTexte = facturierDocument.getModele().getCouleurTexte();
        } else if (!StringUtils.isEmpty(facturierDocument.getProfil().getCouleurTexte())) {
            couleurTexte = facturierDocument.getProfil().getCouleurTexte();
        } else {
            couleurTexte = "#000000";
        }

        ItextFontFactory itextFontFactory = new ItextFontFactory();
        itextFontFactory.init();
        ItextCellFactory itextCellFactory = new ItextCellFactory(itextFontFactory, couleurFond, couleurTexte);

        PdfDocument pdfFinal = new PdfDocument(new PdfReader(pathFile + "_tmp2.pdf"), new PdfWriter(pathFile + ".pdf"));
        Document documentFinal = new Document(pdfFinal);

        DetailGenerator detailGenerator = new DetailGenerator(itextFontFactory, itextCellFactory);

        if (StringUtils.isNotEmpty(textWatermark)) {
            detailGenerator.addWatermarkToDocument(documentFinal, textWatermark);
        }

        if (bannieres.size() > 0) {
            detailGenerator.addBanniereToFirstPage(documentFinal, bannieres);
        }

        detailGenerator.addPliageToDocument(documentFinal);
        String signature = "";
        if (facturierInfos != null && !StringUtils.isEmpty(facturierDocument.getSignature())) {
            signature = "(NF525) B " + facturierInfos.getNf525().split("/")[1].split("-")[0] + " " + facturierDocument.getSignature().charAt(2)
                    + facturierDocument.getSignature().charAt(6) + facturierDocument.getSignature().charAt(12) +
                    facturierDocument.getSignature().charAt(18) + " " +
                    facturierInfos.getNomLogiciel() + " " + facturierInfos.getVersionLogiciel() +
                    " - ";
        }
        detailGenerator.addNumeroPageToDocument(documentFinal, signature, facturierDocument.getReference());

        documentFinal.close();
        pdfFinal.close();
    }
}
