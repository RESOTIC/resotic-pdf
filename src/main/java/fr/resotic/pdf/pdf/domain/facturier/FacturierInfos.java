package fr.resotic.pdf.pdf.domain.facturier;

public class FacturierInfos {

    private Integer id;

    private String nomLogiciel;

    private String versionLogiciel;

    private String nf525;

    public FacturierInfos() {
        //constructeur par defaut
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomLogiciel() {
        return nomLogiciel;
    }

    public String getVersionLogiciel() {
        return versionLogiciel;
    }

    public String getNf525() {
        return nf525;
    }

}
