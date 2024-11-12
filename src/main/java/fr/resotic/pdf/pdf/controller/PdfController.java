package fr.resotic.pdf.pdf.controller;

import fr.resotic.pdf.pdf.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final FacturierService facturierService;
    private final SignatureService signatureService;
    private final HtmlService htmlService;
    private final ConcatService concatService;
    private final FooterService footerService;

    @Autowired
    public PdfController(FacturierService facturierService, SignatureService signatureService, HtmlService htmlService, ConcatService concatService, FooterService footerService) {
        this.facturierService = facturierService;
        this.signatureService = signatureService;
        this.htmlService = htmlService;
        this.concatService = concatService;
        this.footerService = footerService;
    }

    @PostMapping("/facturier")
    public void generatePdfFacturier(@RequestBody String body) throws Exception {
        facturierService.generatePdfFacturier(body);
    }

    @PostMapping("/facturier/A")
    public void generatePdfA(@RequestBody String body) throws Exception {
        facturierService.generatePdfA(body);
    }

    @PostMapping("/signature")
    public void addSignatureToPdf(@RequestBody String body) throws Exception {
        signatureService.addSignatureToPdf(body);
    }

    @PostMapping("/html")
    public byte[] generatePdfFromHtml(@RequestBody String body) throws IOException {
        return htmlService.generatePdfFromHtml(body);
    }

    @PostMapping("/concat")
    public byte[] concatPdf(@RequestBody String body) throws IOException {
        return concatService.concatPdf(body);
    }

    @PostMapping("/add/footer")
    public void addFooterToPdf(@RequestBody String body) throws IOException {
        footerService.addFooterToPdf(body);
    }

}
