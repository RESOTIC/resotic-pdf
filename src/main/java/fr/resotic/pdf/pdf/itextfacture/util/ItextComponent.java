package fr.resotic.pdf.pdf.itextfacture.util;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Table;

public abstract class ItextComponent {

    protected final ItextCellFactory itextCellFactory;

    protected final float spacingBefore;
    protected final float spacingAfter;

    protected Table view;

    public ItextComponent(ItextCellFactory itextCellFactory, float spacingBefore, float spacingAfter, Table view) {
        this.itextCellFactory = itextCellFactory;
        this.spacingBefore = spacingBefore;
        this.spacingAfter = spacingAfter;
        this.view = view;
        this.view.setBorder(Border.NO_BORDER);
    }

    public Table getView() {
        return view;
    }
}
