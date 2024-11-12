package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierModeleFacturierCommentaire {

    private FacturierCommentaire commentaire;
    private Boolean check;

    public FacturierModeleFacturierCommentaire() {
        // constructeur par défaut
    }

    public FacturierCommentaire getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(FacturierCommentaire commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
