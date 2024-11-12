package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Abonne;
import fr.resotic.pdf.pdf.domain.Pays;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FacturierProfil {


    public static final Integer TYPE_PROFIl_ENTREPRISE = 1;
    public static final Integer TYPE_PROFIl_EI = 2;
    public static final Integer TYPE_PROFIl_ASSOCIATION = 3;

    private Integer id;

    private Abonne abonne;

    private FacturierExonerationTvaMention exonerationTvaMention;

    private String nom;

    private String prenom;

    private String nomCommercial;

    private String formeSocietaire;

    private String capitalSocial;

    private String adresse;

    private String adresse2;

    private String cp;

    private String ville;

    private Pays pays;

    private String telephone;

    private String portable;

    private String fax;

    private String mail;

    private String siteInternet;

    private String siret;

    private String rcs;

    private String ape;

    private String tvaIntra;

    private String agrement;

    private String accises;

    private String iban;

    private String bic;

    private String champLibre;

    private FacturierLogo logo;

    private List<FacturierLogo> banniere = new ArrayList<>(0);

    private String adresseFacturation;

    private String cpFacturation;

    private String villeFacturation;

    private String couleurFond;

    private String couleurTexte;

    private String villeImmatriculation;

    private String numeroInscriptionRepertoireMetier;

    private String siren;

    private Integer calculTva;

    private Integer typeProfil;

    private Boolean memeAdresseFacturation;

    public FacturierProfil() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public FacturierProfil setId(Integer id) {
        this.id = id;
        return this;
    }

    public Abonne getAbonne() {
        return abonne;
    }

    public FacturierExonerationTvaMention getExonerationTvaMention() {
        return exonerationTvaMention;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNomCommercial() {
        return nomCommercial;
    }

    public String getFormeSocietaire() {
        return formeSocietaire;
    }

    public String getCapitalSocial() {
        return capitalSocial;
    }

    public String getAdresse() {
        return adresse;
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

    public String getMail() {
        return mail;
    }

    public String getSiteInternet() {
        return siteInternet;
    }

    public String getSiret() {
        return siret;
    }

    public String getRcs() {
        return rcs;
    }

    public String getApe() {
        return ape;
    }

    public String getTvaIntra() {
        return tvaIntra;
    }

    public String getAgrement() {
        return agrement;
    }

    public String getAccises() {
        return accises;
    }

    public String getIban() {
        return iban;
    }

    public String getBic() {
        return bic;
    }

    public String getChampLibre() {
        return champLibre;
    }

    public FacturierLogo getLogo() {
        return logo;
    }

    public String getAdresseFacturation() {
        return adresseFacturation;
    }

    public String getCpFacturation() {
        return cpFacturation;
    }

    public String getVilleFacturation() {
        return villeFacturation;
    }

    public String getCouleurFond() {
        return couleurFond;
    }

    public String getCouleurTexte() {
        return couleurTexte;
    }

    public List<FacturierLogo> getBanniere() {
        return banniere;
    }

    public String getNumeroInscriptionRepertoireMetier() {
        return numeroInscriptionRepertoireMetier;
    }

    public String getSiren() {
        return siren;
    }

    public String getRcsComplet() {
        String val = "";
        boolean hasVal = false;
        if (StringUtils.isNotBlank(rcs)) {
            val += rcs;
            hasVal = true;
        }
        if (StringUtils.isNotBlank(villeImmatriculation)) {
            if (hasVal) {
                val += " ";
            }
            val += villeImmatriculation;
            hasVal = true;
        }
        return val;
    }

    public Integer getTypeProfil() {
        return typeProfil;
    }

    public String getVilleImmatriculation() {
        return villeImmatriculation;
    }

    public Integer getCalculTva() {
        return calculTva;
    }

    public Boolean getMemeAdresseFacturation() {
        return memeAdresseFacturation;
    }
}
