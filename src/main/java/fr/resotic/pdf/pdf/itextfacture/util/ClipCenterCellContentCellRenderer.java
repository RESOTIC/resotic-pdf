package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

public class ClipCenterCellContentCellRenderer extends CellRenderer {
    private Paragraph content;

    public ClipCenterCellContentCellRenderer(Cell modelElement, Paragraph content) {
        super(modelElement);
        this.content = content;
    }

    // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
    // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
    // renderer will be created
    @Override
    public IRenderer getNextRenderer() {
        return new ClipCenterCellContentCellRenderer((Cell) modelElement, content);
    }

    @Override
    public void draw(DrawContext drawContext) {

        // Fictitiously layout the renderer and find out, how much space does it require
        IRenderer pr = content.createRendererSubTree().setParent(this);

        LayoutResult textArea = pr.layout(new LayoutContext(
                new LayoutArea(0, new Rectangle(getOccupiedAreaBBox().getWidth(), 1000))));

        float spaceNeeded = textArea.getOccupiedArea().getBBox().getHeight();
        System.out.println(String.format("The content requires %s pt whereas the height is %s pt.",
                spaceNeeded, getOccupiedAreaBBox().getHeight()));

        float offset = (getOccupiedAreaBBox().getHeight() - textArea.getOccupiedArea().getBBox().getHeight()) / 2;
        System.out.println(String.format("The difference is %s pt; we'll need an offset of %s pt.",
                -2f * offset, offset));

        PdfFormXObject xObject = new PdfFormXObject(new Rectangle(getOccupiedAreaBBox().getWidth(),
                getOccupiedAreaBBox().getHeight()));
        Canvas layoutCanvas = new Canvas(new PdfCanvas(xObject, drawContext.getDocument()),
                new Rectangle(0, offset, getOccupiedAreaBBox().getWidth(), spaceNeeded));
        layoutCanvas.add(content);

        drawContext.getCanvas().addXObject(xObject, occupiedArea.getBBox().getLeft(), occupiedArea.getBBox().getBottom());
    }
}
