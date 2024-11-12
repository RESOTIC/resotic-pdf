package fr.resotic.pdf.pdf.service;

import java.io.IOException;

public interface ConcatService {

    byte[] concatPdf(String body) throws IOException;

}
