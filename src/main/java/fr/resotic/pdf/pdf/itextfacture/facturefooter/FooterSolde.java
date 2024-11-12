package fr.resotic.pdf.pdf.itextfacture.facturefooter;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;
import fr.resotic.pdf.pdf.domain.facturier.*;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import fr.resotic.pdf.pdf.itextfacture.util.ItextComponent;
import fr.resotic.pdf.pdf.util.NumberUtil;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class FooterSolde extends ItextComponent {

    private Table viewTaux;
    private Table viewSolde;

    private final float[] viewWidth = {1.2f, 0.7f, 1f};
    private final float[] viewTauxWidth = {1.5f, 1.5f, 1.5f};
    private final float[] viewSoldeWidths = {1.5f, 1f};

    public FooterSolde(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(UnitValue.createPercentArray(new float[]{1f, 1f})));
        view.useAllAvailableWidth();
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (facturierDocument.getProfil().getExonerationTvaMention() == null && !facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
            this.initTauxView(facturierDocument);
        }
        this.initSoldeView(facturierDocument);
        this.initView(facturierDocument);
    }

    private void initTauxView(FacturierDocument facturierDocument) throws IOException {
        viewTaux = new Table(UnitValue.createPercentArray(viewTauxWidth)).useAllAvailableWidth().setBorder(Border.NO_BORDER);
        viewTaux.setHorizontalAlignment(HorizontalAlignment.LEFT);

        viewTaux.addCell(itextCellFactory.createTvaFooterHeader("Taux"));
        viewTaux.addCell(itextCellFactory.createTvaFooterHeader("Total HT"));
        viewTaux.addCell(itextCellFactory.createTvaFooterHeader("Montant TVA"));

        Map<Integer, BigDecimal> totNetHtAcompteByTVA = new HashMap<>();
        Map<Integer, BigDecimal> totTVAAcompteByTVA = new HashMap<>();
        if (facturierDocument.hasAcompteV2()) {
            for(FacturierAcompteForm acompteForm : facturierDocument.getAcomptes()) {
                for (FacturierTotaux totaux : acompteForm.getTotaux()) {
                    if(!totNetHtAcompteByTVA.containsKey(totaux.getTva().getId())) {
                        totNetHtAcompteByTVA.put(totaux.getTva().getId(), totaux.getNetHt());
                        totTVAAcompteByTVA.put(totaux.getTva().getId(), isTvaGlobal(facturierDocument) ?
                                totaux.getTotalTvaGlobal() :
                                totaux.getTotalTva());
                    } else {
                        totNetHtAcompteByTVA.put(totaux.getTva().getId(), totNetHtAcompteByTVA.get(totaux.getTva().getId()).add(totaux.getNetHt()));
                        totTVAAcompteByTVA.put(totaux.getTva().getId(), totTVAAcompteByTVA.get(totaux.getTva().getId()).add(
                                isTvaGlobal(facturierDocument) ?
                                        totaux.getTotalTvaGlobal() :
                                        totaux.getTotalTva()
                        ));
                    }
                }
            }
        }

        for (FacturierTotaux totaux : facturierDocument.getModele().getTotaux()) {
            viewTaux.addCell(itextCellFactory.createCellFooterValue(String.format("%.2f", totaux.getTva().getTaux()) + " %"));
            if (facturierDocument.hasAcompteV2()) {
                viewTaux.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(totaux.getNetHt().subtract(
                        totNetHtAcompteByTVA.get(totaux.getTva().getId()) != null ? totNetHtAcompteByTVA.get(totaux.getTva().getId()) : BigDecimal.ZERO
                ), 2)));
                viewTaux.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(
                        isTvaGlobal(facturierDocument) ?
                                totaux.getTotalTvaGlobal().subtract(
                                        totTVAAcompteByTVA.get(totaux.getTva().getId()) != null ? totTVAAcompteByTVA.get(totaux.getTva().getId()) : BigDecimal.ZERO
                                ) :
                                totaux.getTotalTva().subtract(
                                        totTVAAcompteByTVA.get(totaux.getTva().getId()) != null ? totTVAAcompteByTVA.get(totaux.getTva().getId()) : BigDecimal.ZERO
                                )
                        , 2)));
            } else {
                viewTaux.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(totaux.getNetHt(), 2)));
                viewTaux.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(
                        isTvaGlobal(facturierDocument) ?
                                totaux.getTotalTvaGlobal() :
                                totaux.getTotalTva()
                        , 2)));
            }
        }
    }

    private void initSoldeView(FacturierDocument facturierDocument) throws IOException {
        viewSolde = new Table(UnitValue.createPercentArray(viewSoldeWidths)).useAllAvailableWidth().setMarginLeft(60f);
        viewSolde.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        if (!facturierDocument.hasAcompteV2()) {
            viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Total HT" + (facturierDocument.getRemiseHt() != null && facturierDocument.getRemiseHt().compareTo(BigDecimal.ZERO) != 0 ? " avant remise" : "")));
            viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalHt(), 2)));

            if (facturierDocument.getRemiseHt() != null && facturierDocument.getRemiseHt().compareTo(BigDecimal.ZERO) != 0) {
                String global = "";
                if (facturierDocument.getModele().getRemise() != null && facturierDocument.getModele().getRemise().compareTo(BigDecimal.ZERO) != 0) {
                    global = " \r dont " + NumberUtil.formatNumberIfNumeric(facturierDocument.getModele().getRemise(), 2, false) + (facturierDocument.getModele().getTypeRemise() == null || facturierDocument.getModele().getTypeRemise().equals(FacturierModele.REMISE_POURCENTAGE) ? "%" : "€") + " globale";
                }

                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Total remise HT" + global));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getRemiseHt(), 2)));

                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Net HT"));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getNetHt(), 2)));
            }

            if (facturierDocument.getProfil().getExonerationTvaMention() == null) {
                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("TVA"));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTva(), 2)));
            }

            if (facturierDocument.hasAcompte()) {
                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Total TTC"));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTtc(), 2)));
            } else {
                viewSolde.addCell(itextCellFactory.createLastTotalFooterCellHeader("Total TTC"));
                viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTtc(), 2)));
            }

            if (facturierDocument.hasAcompte()) {
                for (FacturierAcompteForm acompte : facturierDocument.getAcomptes()) {
                    viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader(acompte.getDocument().getReference()));
                    viewSolde.addCell(itextCellFactory.createCellFooterValue((BooleanUtils.isTrue(acompte.getAvoir()) ? "-" : "") + NumberUtil.formatNumberIfNumeric(acompte.getMontant(), 2)).setVerticalAlignment(VerticalAlignment.MIDDLE));
                }
            }
        } else {
            viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Total HT" + (facturierDocument.getRemiseHt() != null && facturierDocument.getRemiseHt().compareTo(BigDecimal.ZERO) != 0 ? " avant remise" : "")));
            if (facturierDocument.hasAcompteV2()) {
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalHtSubAcompte(), 2)));
            } else {
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalHt(), 2)));
            }

            if (facturierDocument.getRemiseHt() != null && facturierDocument.getRemiseHt().compareTo(BigDecimal.ZERO) != 0) {
                String global = "";
                if (facturierDocument.getModele().getRemise() != null && facturierDocument.getModele().getRemise().compareTo(BigDecimal.ZERO) != 0) {
                    global = " \r dont " + NumberUtil.formatNumberIfNumeric(facturierDocument.getModele().getRemise(), 2, false) + (facturierDocument.getModele().getTypeRemise() == null || facturierDocument.getModele().getTypeRemise().equals(FacturierModele.REMISE_POURCENTAGE) ? "%" : "€") + " globale";
                }

                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Total remise HT" + global));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getRemiseHt(), 2)));

                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("Net HT"));
                viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalNetHtSubAcompte(), 2)));
            }

            if (facturierDocument.getProfil().getExonerationTvaMention() == null) {
                viewSolde.addCell(itextCellFactory.createTotalFooterCellHeader("TVA"));
                if (facturierDocument.hasAcompteV2()) {
                    viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTvaSubAcompte(), 2)));
                } else {
                    viewSolde.addCell(itextCellFactory.createCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTva(), 2)));
                }
            }

            viewSolde.addCell(itextCellFactory.createLastTotalFooterCellHeader("Total TTC"));
            if (facturierDocument.hasAcompteV2()) {
                if(facturierDocument.getProfil().getExonerationTvaMention() == null) {
                    viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTtcSubAcompte(), 2)));
                } else {
                    viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalNetHtSubAcompte(), 2)));
                }
            } else {
                viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTtc(), 2)));
            }
        }

        if(facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR) || facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_LIBRE) ||
                facturierDocument.getType().getId().equals(FacturierType.TYPE_AVOIR_TOTAL)) {
            // Avoir
            viewSolde.addCell(itextCellFactory.createLastTotalFooterCellHeader("Net à vous devoir"));
        } else if (Boolean.TRUE.equals(facturierDocument.getFactureSolde())) {
            // Facture avec solde
            viewSolde.addCell(itextCellFactory.createLastTotalFooterCellHeader("Solde à payer"));
        } else {
            // les autres cas
            viewSolde.addCell(itextCellFactory.createLastTotalFooterCellHeader("Net à payer"));
        }

        if(facturierDocument.getProfil().getExonerationTvaMention() == null) {
            viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalTtcSubAcompte(), 2)));
        } else {
            if (facturierDocument.hasAcompteV2()) {
                viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getTotalNetHtSubAcompte(), 2)));
            } else {
                viewSolde.addCell(itextCellFactory.createLastCellFooterValue(NumberUtil.formatNumberIfNumeric(facturierDocument.getNetHt().subtract(facturierDocument.getTotalAcompte()), 2)));
            }

        }

    }

    private void initView(FacturierDocument facturierDocument) throws IOException {
        if (viewTaux != null && facturierDocument.getModele().getTotaux() != null && facturierDocument.getModele().getTotaux().size() > 0) {
            view.addCell(itextCellFactory.getCellNoBorder(viewTaux, HorizontalAlignment.LEFT)).setHorizontalAlignment(HorizontalAlignment.LEFT);
        } else {
            view.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        view.addCell(itextCellFactory.getCellNoBorder(viewSolde, HorizontalAlignment.RIGHT)).setHorizontalAlignment(HorizontalAlignment.RIGHT);
    }

    private boolean isTvaGlobal(FacturierDocument document) throws IOException {
        if (document.getProfil().getCalculTva() != null) {
            return document.getProfil().getCalculTva().equals(FacturierDocument.CALCUL_TVA_GLOBAL);
        }
        return document.getProfil().getAbonne().getCalculTva() != null && document.getProfil().getAbonne().getCalculTva().equals(FacturierDocument.CALCUL_TVA_GLOBAL);
    }
}
