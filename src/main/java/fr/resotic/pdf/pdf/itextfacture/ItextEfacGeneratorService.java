package fr.resotic.pdf.pdf.itextfacture;

import com.itextpdf.kernel.font.PdfFontFactory;
import fr.resotic.pdf.pdf.domain.facturier.FacturierLogo;
import fr.resotic.pdf.pdf.domain.facturier.FacturierPdf;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ItextEfacGeneratorService {

    private final ItextEfacGenerator itextEfacGenerator = new ItextEfacGenerator();

    @Autowired
    public ItextEfacGeneratorService() {
        String ARIAL_PATH = "src/main/resources/fonts/arial.ttf";
        PdfFontFactory.register(String.valueOf(getClass().getResourceAsStream(ARIAL_PATH)), "ARIAL");
    }

    public void generateFacture(FacturierPdf facturierPdf) throws Exception {
        this.itextEfacGenerator.generateFacture(facturierPdf.getFileName(),
                facturierPdf.getDocument(),
                getLogoEntete(facturierPdf),
                getBanniere(facturierPdf),
                facturierPdf.getTextWatermark(),
                facturierPdf.getPapillon(),
                facturierPdf.getFacturierInfos());
    }

    private byte[] getLogoEntete(FacturierPdf facturierPdf) throws IOException {
        byte[] logoEntete = null;
        if (facturierPdf.getDocument().getModele().getLogo() != null) {
            logoEntete = getLogoImage(facturierPdf.getDocument().getModele().getLogo(), facturierPdf.getEchangePath());
        } else if (!BooleanUtils.isTrue(facturierPdf.getDocument().getModele().getIsLogoIndep()) && facturierPdf.getDocument().getProfil().getLogo() != null) {
            logoEntete = getLogoImage(facturierPdf.getDocument().getProfil().getLogo(), facturierPdf.getEchangePath());
        }
        return logoEntete;
    }

    public byte[] getLogoImage(FacturierLogo logo, String echangePath) throws IOException {
        File file = new File(echangePath + FacturierLogo.DEVIS_FACTURIER_LOGO + logo.getNomFichier());
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return null;
    }

    public List<byte[]> getBanniere(FacturierPdf facturierPdf) throws IOException {
        List<byte[]> byteLogos = new ArrayList<>();
        if (facturierPdf.getDocument().getModele().getBanniere() != null && !facturierPdf.getDocument().getModele().getBanniere().isEmpty()) {
            for (FacturierLogo logo : facturierPdf.getDocument().getModele().getBanniere()) {
                byteLogos.add(getLogoImage(logo, facturierPdf.getEchangePath()));
            }
        } else if (!BooleanUtils.isTrue(facturierPdf.getDocument().getModele().getIsLogoIndep()) && facturierPdf.getDocument().getProfil().getBanniere() != null && !facturierPdf.getDocument().getProfil().getBanniere().isEmpty()) {
            for (FacturierLogo logo : facturierPdf.getDocument().getProfil().getBanniere()) {
                byteLogos.add(getLogoImage(logo, facturierPdf.getEchangePath()));
            }
        }
        return byteLogos;
    }

}
