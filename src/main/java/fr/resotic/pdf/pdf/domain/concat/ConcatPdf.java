package fr.resotic.pdf.pdf.domain.concat;

import java.io.File;
import java.util.List;

public class ConcatPdf {

    private List<File> files;

    public ConcatPdf() {
        // constructeur par défaut
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
