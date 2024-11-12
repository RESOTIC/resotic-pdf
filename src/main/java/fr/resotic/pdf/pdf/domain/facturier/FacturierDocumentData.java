package fr.resotic.pdf.pdf.domain.facturier;

import fr.resotic.pdf.pdf.domain.Rib;

import java.util.List;

public class FacturierDocumentData {

    private FacturierProfil profil;

    private FacturierModele modele;

    private FacturierClient client;

    private List<FacturierAcompteForm> acomptes;

    private FacturierInfos infos;

    private Rib rib;

    public FacturierDocumentData() {
        // constructeur par défaut
    }

    public FacturierProfil getProfil() {
        return profil;
    }

    public void setProfil(FacturierProfil profil) {
        this.profil = profil;
    }

    public FacturierModele getModele() {
        return modele;
    }

    public void setModele(FacturierModele modele) {
        this.modele = modele;
    }

    public FacturierClient getClient() {
        return client;
    }

    public void setClient(FacturierClient client) {
        this.client = client;
    }

    public List<FacturierAcompteForm> getAcomptes() {
        return acomptes;
    }

    public FacturierDocumentData setAcomptes(List<FacturierAcompteForm> acomptes) {
        this.acomptes = acomptes;
        return this;
    }

    public FacturierInfos getInfos() {
        return infos;
    }

    public void setInfos(FacturierInfos infos) {
        this.infos = infos;
    }

    public Rib getRib() {
        return rib;
    }

    public void setRib(Rib rib) {
        this.rib = rib;
    }
}
