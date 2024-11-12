package fr.resotic.pdf.pdf.util;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;

public class PageOrientationsEventHandler implements IEventHandler {
    private PdfNumber orientation = new PdfNumber(0);

    public void setOrientation(PdfNumber orientation) {
        this.orientation = orientation;
    }

    @Override
    public void handleEvent(Event currentEvent) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
        docEvent.getPage().put(PdfName.Rotate, orientation);
    }

}
