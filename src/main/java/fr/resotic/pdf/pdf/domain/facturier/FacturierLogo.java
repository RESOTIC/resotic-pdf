package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierLogo {

    public static final String DEVIS_FACTURIER_LOGO = "logoPath/";

    private Integer id;

    private String nomFichier;

    public FacturierLogo() {
    }

    public Integer getId() {
        return id;
    }

    public FacturierLogo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getNomFichier() {
        return nomFichier;
    }

}
