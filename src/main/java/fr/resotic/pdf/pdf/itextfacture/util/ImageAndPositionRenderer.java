package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.IRenderer;

public class ImageAndPositionRenderer extends CellRenderer {
    private final Image img;

    public ImageAndPositionRenderer(Cell modelElement, Image img) {
        super(modelElement);
        this.img = img;
    }

    // If a renderer overflows on the next area, iText uses #getNextRenderer() method to create a new renderer for the overflow part.
    // If #getNextRenderer() isn't overridden, the default method will be used and thus the default rather than the custom
    // renderer will be created
    @Override
    public IRenderer getNextRenderer() {
        return new ImageAndPositionRenderer((Cell) modelElement, img);
    }

    @Override
    public void draw(DrawContext drawContext) {
        super.draw(drawContext);
        img.scaleToFit(getOccupiedAreaBBox().getWidth(), getOccupiedAreaBBox().getHeight());
        img.getProperty(Property.HORIZONTAL_SCALING);
        drawContext.getCanvas().addXObject(img.getXObject(),
                getOccupiedAreaBBox().getX() +
                        (getOccupiedAreaBBox().getWidth()
                                - img.getImageWidth() * (float) img.getProperty(Property.HORIZONTAL_SCALING)) / 2,
                getOccupiedAreaBBox().getY() +
                        (getOccupiedAreaBBox().getHeight()
                                - img.getImageHeight() * (float) img.getProperty(Property.VERTICAL_SCALING)) / 2,
                img.getImageWidth() * (float) img.getProperty(Property.HORIZONTAL_SCALING));
        drawContext.getCanvas().stroke();
    }
}
