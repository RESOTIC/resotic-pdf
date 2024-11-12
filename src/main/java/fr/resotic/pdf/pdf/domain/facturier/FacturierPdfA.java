package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierPdfA {

    private String src;

    private String dest;

    private String attachementPath;

    public FacturierPdfA() {
        // constructeur par défaut
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getAttachementPath() {
        return attachementPath;
    }

    public void setAttachementPath(String attachementPath) {
        this.attachementPath = attachementPath;
    }
}
