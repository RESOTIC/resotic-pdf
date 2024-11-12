package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Pays;

public class FacturierClientAdresseLivraison {

    private Integer id;

    private FacturierClient client;

    private String adresse;

    private String adresse2;

    private String cp;

    private String ville;

    private Pays pays;

    public FacturierClientAdresseLivraison() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public FacturierClient getClient() {
        return client;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getAdressePDF() {
        return adresse.replaceAll("\t", " ");
    }

    public String getAdresse2() {
        return adresse2;
    }

    public String getCp() {
        return cp;
    }

    public String getVille() {
        return ville;
    }

    public Pays getPays() {
        return pays;
    }

    public String getAdresseComplete() {
        return this.getAdresse() + " " + this.getCp() + " " + this.getVille();
    }
}
