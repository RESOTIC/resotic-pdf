package fr.resotic.pdf.pdf.domain.signature;

public class SignaturePdf {

    private String path;
    private String logo;
    private String nom;
    private String prenom;
    private String lieu;
    private String date;
    private SignaturePosition position;

    public SignaturePdf() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public SignaturePosition getPosition() {
        return position;
    }

    public void setPosition(SignaturePosition position) {
        this.position = position;
    }
}
