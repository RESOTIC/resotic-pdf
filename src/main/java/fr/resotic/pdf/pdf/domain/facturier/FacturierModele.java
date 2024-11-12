package fr.resotic.pdf.pdf.domain.facturier;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.math.BigDecimal;
import java.util.*;

public class FacturierModele implements Cloneable {

    public static Integer REMISE_POURCENTAGE = 0;

    private Integer id;

    private String libelle;

    private Integer echeanceType;

    private Integer echeanceNbJour;

    private Boolean saisieTtc;

    private String texteDevis;

    private Boolean profilNomCommercial;

    private Boolean profilAdresse;

    private Boolean profilCp;

    private Boolean profilVille;

    private Boolean profilPays;

    private Boolean profilNom;

    private Boolean profilPrenom;

    private Boolean profilFormeSocietaire;

    private Boolean profilCapitalSocial;

    private Boolean profilTelephone;

    private Boolean profilPortable;

    private Boolean profilFax;

    private Boolean profilMail;

    private Boolean profilSiteInternet;

    private Boolean profilSiret;

    private Boolean profilSiren;

    private Boolean profilRcs;

    private Boolean profilNumeroInscriptionRepertoireMetier;

    private Boolean profilApe;

    private Boolean profilTvaintra;

    private Boolean profilAgrement;

    private Boolean profilAccises;

    private Boolean profilChampLibre;

    private Boolean profilIban;

    private Boolean profilBic;

    private Boolean clientSociete;

    private Boolean clientCivilite;

    private Boolean clientNom;

    private Boolean clientPrenom;

    private Boolean clientAdresse;

    private Boolean clientCp;

    private Boolean clientVille;

    private Boolean clientPays;

    private Boolean clientMail;

    private Boolean clientTelephone;

    private Boolean clientPortable;

    private Boolean clientFax;

    private Boolean clientTvaintra;

    private Boolean clientChampLibre;

    private Boolean clientIban;

    private Boolean clientBic;

    private BigDecimal remise;

    private Integer typeRemise;

    private List<FacturierCommentaire> commentaires;
    private List<FacturierModeleFacturierCommentaire> modeleCommentaires;

    private FacturierLogo logo;

    private List<FacturierArticleSection> sections = new ArrayList<>(0);

    private Boolean clientSiret;

    private String couleurFond;

    private String couleurTexte;

    private Set<FacturierTotaux> totaux = new HashSet<>(0);

    private Date echeanceDate;

    private Set<FacturierLogo> banniere;

    private Boolean isLogoIndep;

    public FacturierModele() {
        // constructeur par défaut
    }


    public Integer getId() {
        return id;
    }

    public FacturierModele setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return libelle;
    }

    public Integer getEcheanceType() {
        return echeanceType;
    }

    public Integer getEcheanceNbJour() {
        return echeanceNbJour;
    }

    public Boolean getSaisieTtc() {
        return saisieTtc;
    }

    public String getTexteDevis() {
        return texteDevis;
    }

    public String getTexteDevisCleanHtml() {
        return Jsoup.clean(texteDevis != null ? texteDevis : "", Whitelist.relaxed().removeTags("a"));
    }

    public Boolean getProfilNomCommercial() {
        return profilNomCommercial;
    }

    public Boolean getProfilAdresse() {
        return profilAdresse;
    }

    public Boolean getProfilCp() {
        return profilCp;
    }

    public Boolean getProfilVille() {
        return profilVille;
    }

    public Boolean getProfilPays() {
        return profilPays;
    }

    public Boolean getProfilNom() {
        return profilNom;
    }

    public Boolean getProfilPrenom() {
        return profilPrenom;
    }

    public Boolean getProfilFormeSocietaire() {
        return profilFormeSocietaire;
    }

    public Boolean getProfilCapitalSocial() {
        return profilCapitalSocial;
    }

    public Boolean getProfilTelephone() {
        return profilTelephone;
    }

    public Boolean getProfilPortable() {
        return profilPortable;
    }

    public Boolean getProfilFax() {
        return profilFax;
    }

    public Boolean getProfilMail() {
        return profilMail;
    }

    public Boolean getProfilSiteInternet() {
        return profilSiteInternet;
    }

    public Boolean getProfilSiret() {
        return profilSiret;
    }

    public Boolean getProfilRcs() {
        return profilRcs;
    }

    public Boolean getProfilApe() {
        return profilApe;
    }

    public Boolean getProfilTvaintra() {
        return profilTvaintra;
    }

    public Boolean getProfilAgrement() {
        return profilAgrement;
    }

    public Boolean getProfilAccises() {
        return profilAccises;
    }

    public Boolean getProfilChampLibre() {
        return profilChampLibre;
    }

    public Boolean getProfilIban() {
        return profilIban;
    }

    public Boolean getProfilBic() {
        return profilBic;
    }

    public Boolean getClientSociete() {
        return clientSociete;
    }

    public Boolean getClientCivilite() {
        return clientCivilite;
    }

    public Boolean getClientNom() {
        return clientNom;
    }

    public Boolean getClientPrenom() {
        return clientPrenom;
    }

    public Boolean getClientAdresse() {
        return clientAdresse;
    }

    public Boolean getClientCp() {
        return clientCp;
    }

    public Boolean getClientVille() {
        return clientVille;
    }

    public Boolean getClientPays() {
        return clientPays;
    }

    public Boolean getClientMail() {
        return clientMail;
    }

    public Boolean getClientTelephone() {
        return clientTelephone;
    }

    public Boolean getClientPortable() {
        return clientPortable;
    }

    public Boolean getClientFax() {
        return clientFax;
    }

    public Boolean getClientTvaintra() {
        return clientTvaintra;
    }

    public Boolean getClientChampLibre() {
        return clientChampLibre;
    }

    public Boolean getClientIban() {
        return clientIban;
    }

    public Boolean getClientBic() {
        return clientBic;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public List<FacturierCommentaire> getCommentaires() {
        return commentaires;
    }

    public List<FacturierModeleFacturierCommentaire> getModeleCommentaires() {
        return modeleCommentaires;
    }

    public Date getEcheanceDate() {
        return echeanceDate;
    }

    public List<FacturierArticleSection> getSections() {
        return sections;
    }

    public FacturierLogo getLogo() {
        return logo;
    }

    public Set<FacturierTotaux> getTotaux() {
        return totaux;
    }

    public Boolean getClientSiret() {
        return clientSiret;
    }

    public String getCouleurFond() {
        return couleurFond;
    }

    public String getCouleurTexte() {
        return couleurTexte;
    }

    public Boolean getProfilSiren() {
        return profilSiren;
    }

    public Boolean getProfilNumeroInscriptionRepertoireMetier() {
        return profilNumeroInscriptionRepertoireMetier;
    }

    public Integer getTypeRemise() {
        return typeRemise;
    }

    public Set<FacturierLogo> getBanniere() {
        return banniere;
    }

    public Boolean getIsLogoIndep() {
        return isLogoIndep;
    }
}
