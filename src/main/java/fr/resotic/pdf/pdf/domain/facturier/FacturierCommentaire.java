package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierCommentaire {

    private Integer id;

    private String libelle;

    private String commentaire;

    private Integer ordre;

    public FacturierCommentaire() {
        // constructeur par défaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
