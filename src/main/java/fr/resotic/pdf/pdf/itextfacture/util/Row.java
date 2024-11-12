package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.layout.element.Cell;

import java.util.List;

public class Row {

    private float height;
    private final List<Cell> cells;

    public Row(List<Cell> cells) {
        this.cells = cells;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public List<Cell> getCells() {
        return cells;
    }

}
