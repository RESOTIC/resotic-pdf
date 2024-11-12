package fr.resotic.pdf.pdf.service;

import java.io.IOException;

public interface HtmlService {

    byte[] generatePdfFromHtml(String body) throws IOException;

}
