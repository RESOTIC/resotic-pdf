package fr.resotic.pdf.pdf.domain.facturier;

import java.util.Date;

public class FacturierDocumentRelance {

    private Integer id;

    private FacturierDocument document;

    private Date date;

    private String type;

    public FacturierDocumentRelance() {
    }

    public FacturierDocumentRelance(FacturierDocument document, Date date, String type) {
        this.document = document;
        this.date = date;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public FacturierDocumentRelance setId(Integer id) {
        this.id = id;
        return this;
    }

    public FacturierDocument getDocumentId() {
        return document;
    }

    public FacturierDocumentRelance setDocumentId(FacturierDocument document) {
        this.document = document;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public FacturierDocumentRelance setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getType() {
        return type;
    }

    public FacturierDocumentRelance setType(String type) {
        this.type = type;
        return this;
    }
}
