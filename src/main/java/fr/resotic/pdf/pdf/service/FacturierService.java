package fr.resotic.pdf.pdf.service;

public interface FacturierService {

    void generatePdfFacturier(String body) throws Exception;

    void generatePdfA(String body) throws Exception;
}
