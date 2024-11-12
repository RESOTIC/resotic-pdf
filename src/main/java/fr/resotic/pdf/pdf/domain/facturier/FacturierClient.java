package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Civilite;
import fr.resotic.pdf.pdf.domain.Pays;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FacturierClient {

    private Integer id;

    private Civilite civilite;

    private String societe;

    private String nom;

    private String prenom;

    private String adresse;

    private String adresse2;

    private String cp;

    private String ville;

    private Pays pays;

    private String telephone;

    private String portable;

    private String fax;

    private String tvaintra;

    private String champLibre;

    private String rib;

    private String iban;

    private Set<FacturierClientMail> mails;

    private String adresseSiege;

    private String cpSiege;

    private String villeSiege;

    private String siret;

    private FacturierClientAdresseLivraison adresseLivraison;

    public FacturierClient() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public FacturierClient setId(Integer id) {
        this.id = id;
        return this;
    }

    public Civilite getCivilite() {
        return civilite;
    }

    public String getSociete() {
        return societe;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
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

    public String getTelephone() {
        return telephone;
    }

    public String getPortable() {
        return portable;
    }

    public String getFax() {
        return fax;
    }

    public String getTvaintra() {
        return tvaintra;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public String getRib() {
        return rib;
    }

    public String getIban() {
        return iban;
    }

    public String getAdresseSiege() {
        return adresseSiege;
    }

    public String getCpSiege() {
        return cpSiege;
    }

    public String getVilleSiege() {
        return villeSiege;
    }

    public String getNomComplet() {
        if ((this.getSociete() == null || this.getSociete().equals(""))) {
            return Optional.ofNullable(this.getNom()).orElse("") + " " + Optional.ofNullable(this.getPrenom()).orElse("");
        } else {
            return (this.getSociete() + " - " + Optional.ofNullable(this.getNom()).orElse("") + " " + Optional.ofNullable(this.getPrenom()).orElse(""));
        }
    }

    public FacturierClientAdresseLivraison getAdresseLivraison() {
        return adresseLivraison;
    }

    public Set<FacturierClientMail> getMails() {
        return mails;
    }

    public String getMail() {
        return mails.stream().map(e -> e.getAdresse()).collect(Collectors.joining(", "));
    }

    public String getSiret() {
        return siret;
    }
}
