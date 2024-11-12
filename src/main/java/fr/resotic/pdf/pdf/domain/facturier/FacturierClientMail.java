package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierClientMail {

    private Integer id;

    private FacturierClient client;

    private String adresse;

    public FacturierClientMail() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacturierClient getClient() {
        return client;
    }

    public void setClient(FacturierClient client) {
        this.client = client;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

}
