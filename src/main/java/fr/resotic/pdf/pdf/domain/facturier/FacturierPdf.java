package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierPdf {

    private String fileName;

    private FacturierDocument document;
    private FacturierInfos facturierInfos;
    private String textWatermark;
    private Boolean papillon;
    private String echangePath;

    public FacturierPdf() {
        // constructeur par défaut
    }

    public String getFileName() {
        return fileName;
    }

    public FacturierDocument getDocument() {
        return document;
    }

    public FacturierInfos getFacturierInfos() {
        return facturierInfos;
    }

    public void setDocument(FacturierDocument document) {
        this.document = document;
    }

    public String getTextWatermark() {
        return textWatermark;
    }

    public Boolean getPapillon() {
        return papillon;
    }

    public String getEchangePath() {
        return echangePath;
    }
}
