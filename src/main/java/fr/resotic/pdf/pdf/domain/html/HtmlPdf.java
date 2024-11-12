package fr.resotic.pdf.pdf.domain.html;

public class HtmlPdf {

    private String htmlDoc;
    boolean watermark;
    String textWaterMark;
    boolean encochePliage;
    boolean header;
    String headerText;
    boolean footer;
    String footerText;
    boolean landscape;

    Long customWidth;
    Boolean onlyOnePage;

    public HtmlPdf() {
        // constructeur par défaut
    }

    public String getHtmlDoc() {
        return htmlDoc;
    }

    public void setHtmlDoc(String htmlDoc) {
        this.htmlDoc = htmlDoc;
    }

    public boolean isWatermark() {
        return watermark;
    }

    public void setWatermark(boolean watermark) {
        this.watermark = watermark;
    }

    public String getTextWaterMark() {
        return textWaterMark;
    }

    public void setTextWaterMark(String textWaterMark) {
        this.textWaterMark = textWaterMark;
    }

    public boolean isEncochePliage() {
        return encochePliage;
    }

    public void setEncochePliage(boolean encochePliage) {
        this.encochePliage = encochePliage;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String footerText) {
        this.footerText = footerText;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public void setLandscape(boolean landscape) {
        this.landscape = landscape;
    }

    public Long getCustomWidth() {
        return customWidth;
    }

    public void setCustomWidth(Long customWidth) {
        this.customWidth = customWidth;
    }

    public Boolean getOnlyOnePage() {
        return onlyOnePage;
    }

    public void setOnlyOnePage(Boolean onlyOnePage) {
        this.onlyOnePage = onlyOnePage;
    }
}
