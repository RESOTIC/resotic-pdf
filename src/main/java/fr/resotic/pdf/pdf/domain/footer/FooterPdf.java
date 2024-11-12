package fr.resotic.pdf.pdf.domain.footer;

public class FooterPdf {

    private String src;
    private String dest;
    private String footerText;

    public FooterPdf() {
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

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }
}
