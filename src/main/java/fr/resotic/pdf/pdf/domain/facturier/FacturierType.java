package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierType {

    public static final String TYPE_DEVIS = "devis";
    public static final String TYPE_FACTURE = "facture";
    public static final String TYPE_BL_NON_VALORISE = "bl_non_valorise";
    public static final String TYPE_BL_VALORISE = "bl_valorise";
    public static final String TYPE_BL = "bl";
    public static final String TYPE_AVOIR_TOTAL = "avoir_total";
    public static final String TYPE_AVOIR_LIBRE = "avoir_libre";
    public static final String TYPE_AVOIR = "avoir";

    private String id;

    private String libelle;

    private Boolean blActif;

    public FacturierType() {
        // constructeur par défaut
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

}
