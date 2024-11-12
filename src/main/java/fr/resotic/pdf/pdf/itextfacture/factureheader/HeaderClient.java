package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import fr.resotic.pdf.pdf.domain.facturier.FacturierClient;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierModele;
import fr.resotic.pdf.pdf.domain.facturier.FacturierType;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


public class HeaderClient extends HeaderPart {

    private final Table adresseTable;
    private final Table diversTable;

    public HeaderClient(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(1));
        adresseTable = new Table(UnitValue.createPercentArray(new float[]{1})).setMarginTop(15f);
        diversTable = new Table(UnitValue.createPercentArray(new float[]{1}));
        adresseTable.useAllAvailableWidth();
        diversTable.useAllAvailableWidth();
        view.setPadding(0);
    }

    private void createHeaderLivraison(FacturierDocument facturierDocument) throws IOException {
        FacturierClient facturierClient = facturierDocument.getClient();
        FacturierModele facturierModele = facturierDocument.getModele();
        adresseTable.addCell(itextCellFactory.createCellHeaderClient("Adresse de livraison :").setUnderline());
        createNomSocieteRow(facturierClient, facturierModele);
        createNomPrenomRow(facturierClient, facturierModele);
        if (facturierClient.getAdresseLivraison() != null) {
            createAdressesLivraisonRow(facturierClient, facturierModele);
            createCpVilleLivraisonRow(facturierClient, facturierModele);
            createPaysLivraisonRow(facturierClient, facturierModele);
        }
        createTelephoneLivraisonRow(facturierClient);
        adresseTable.addCell(itextCellFactory.createCellHeaderClient("Adresse de facturation :").setUnderline());
        createHeaderAdresseStandard(facturierDocument);
    }

    private void createHeaderAdresseStandard(FacturierDocument facturierDocument) throws IOException {
        FacturierClient facturierClient = facturierDocument.getClient();
        FacturierModele facturierModele = facturierDocument.getModele();
        createNomSocieteRow(facturierClient, facturierModele);
        createNomPrenomRow(facturierClient, facturierModele);
        createAdressesRow(facturierClient, facturierModele);
        createCpVilleRow(facturierClient, facturierModele);
        createPaysRow(facturierClient, facturierModele);
        createNumeroEngagementRow(facturierDocument);
        createCodeServiceRow(facturierDocument);
        view.addCell(new Cell().add(adresseTable).setBorder(Border.NO_BORDER).setPaddingLeft(5f));

        if (!facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_VALORISE) && !facturierDocument.getType().getId().equals(FacturierType.TYPE_BL_NON_VALORISE)) {
            createHeaderDivers(facturierClient, facturierModele);
        }

    }

    private void createHeaderDivers(FacturierClient facturierClient, FacturierModele facturierModele) {
        StringBuilder stringBuilder = new StringBuilder();
        createNumeroContact(facturierClient, facturierModele, stringBuilder);
        createMailContactRow(facturierClient, facturierModele, stringBuilder);
        createSiretRow(facturierClient, facturierModele, stringBuilder);
        createTvaIntraRow(facturierClient, facturierModele, stringBuilder);
        createChampLibreRow(facturierClient, facturierModele, stringBuilder);
        createInfoBancaireRow(facturierClient, facturierModele, stringBuilder);
        createAdresseSiegeRow(facturierClient, stringBuilder);

        diversTable.addCell(itextCellFactory.createCellHeaderDiversClient(stringBuilder.toString()));
        view.addCell(new Cell().add(diversTable).setBorder(Border.NO_BORDER));
    }

    private void createNomSocieteRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if (BooleanUtils.isTrue(facturierModele.getClientSociete()) && !StringUtils.isEmpty(facturierClient.getSociete())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getSociete()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createNomPrenomRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if ((BooleanUtils.isTrue(facturierModele.getClientNom()) && !StringUtils.isEmpty(facturierClient.getNom()))
                || (BooleanUtils.isTrue(facturierModele.getClientPrenom()) && !StringUtils.isEmpty(facturierClient.getPrenom()))) {

            StringBuilder stringBuilder = new StringBuilder();
            if (BooleanUtils.isTrue(facturierModele.getClientCivilite())) {
                stringBuilder.append(facturierClient.getCivilite() != null ? facturierClient.getCivilite().getShortLabel() : "").append(" ");
            }
            if (BooleanUtils.isTrue(facturierModele.getClientNom())) {
                stringBuilder.append(StringUtils.isEmpty(facturierClient.getNom()) ? "" : facturierClient.getNom()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierModele.getClientPrenom())) {
                stringBuilder.append(StringUtils.isEmpty(facturierClient.getPrenom()) ? "" : facturierClient.getPrenom()).append(" ");
            }
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(stringBuilder.toString()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createTelephoneLivraisonRow(FacturierClient facturierClient) {
        if (!StringUtils.isEmpty(facturierClient.getTelephone()) || !StringUtils.isEmpty(facturierClient.getPortable())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient((!StringUtils.isEmpty(facturierClient.getTelephone()) ? facturierClient.getTelephone() + " " : "") + (!StringUtils.isEmpty(facturierClient.getPortable()) ? facturierClient.getPortable() + " " : "")));
        }
    }

    private void createNumeroEngagementRow(FacturierDocument document) {
        if (!StringUtils.isEmpty(document.getChorusNumEngagement())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient("Numéro d'engagement : " + document.getChorusNumEngagement()));
        }
    }

    private void createCodeServiceRow(FacturierDocument document) {
        if (!StringUtils.isEmpty(document.getChorusCodeService())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient("Code service : " + document.getChorusCodeService()));
        }
    }

    private void createAdressesRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if (BooleanUtils.isTrue(facturierModele.getClientAdresse()) && !StringUtils.isEmpty(facturierClient.getAdresse())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getAdressePDF()));
        } else {
            addEmptyCell(adresseTable);
        }
        if (BooleanUtils.isTrue(facturierModele.getClientAdresse()) && !StringUtils.isEmpty(facturierClient.getAdresse2())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getAdresse2()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createAdressesLivraisonRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if (BooleanUtils.isTrue(facturierModele.getClientAdresse()) && !StringUtils.isEmpty(facturierClient.getAdresseLivraison().getAdresse())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getAdresseLivraison().getAdressePDF()));
        } else {
            addEmptyCell(adresseTable);
        }
        if (BooleanUtils.isTrue(facturierModele.getClientAdresse()) && !StringUtils.isEmpty(facturierClient.getAdresseLivraison().getAdresse2())) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getAdresseLivraison().getAdresse2()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createCpVilleRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if ((BooleanUtils.isTrue(facturierModele.getClientCp()) && !StringUtils.isEmpty(facturierClient.getCp()))
                || (BooleanUtils.isTrue(facturierModele.getClientVille()) && !StringUtils.isEmpty(facturierClient.getVille()))) {

            StringBuilder stringBuilder = new StringBuilder();
            if (!StringUtils.isEmpty(facturierClient.getCp()) && BooleanUtils.isTrue(facturierModele.getClientCp())) {
                stringBuilder.append(facturierClient.getCp()).append(" ");
            }
            if (!StringUtils.isEmpty(facturierClient.getVille()) && BooleanUtils.isTrue(facturierModele.getClientVille())) {
                stringBuilder.append(facturierClient.getVille()).append(" ");
            }
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(stringBuilder.toString()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createCpVilleLivraisonRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if ((BooleanUtils.isTrue(facturierModele.getClientCp()) && !StringUtils.isEmpty(facturierClient.getAdresseLivraison().getCp()))
                || (BooleanUtils.isTrue(facturierModele.getClientVille()) && !StringUtils.isEmpty(facturierClient.getAdresseLivraison().getVille()))) {

            StringBuilder stringBuilder = new StringBuilder();
            if (!StringUtils.isEmpty(facturierClient.getAdresseLivraison().getCp()) && BooleanUtils.isTrue(facturierModele.getClientCp())) {
                stringBuilder.append(facturierClient.getAdresseLivraison().getCp()).append(" ");
            }
            if (!StringUtils.isEmpty(facturierClient.getAdresseLivraison().getVille()) && BooleanUtils.isTrue(facturierModele.getClientVille())) {
                stringBuilder.append(facturierClient.getAdresseLivraison().getVille()).append(" ");
            }
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(stringBuilder.toString()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createPaysRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if (BooleanUtils.isTrue(facturierModele.getClientPays()) && facturierClient.getPays() != null) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getPays().getLibelle().toUpperCase()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createPaysLivraisonRow(FacturierClient facturierClient, FacturierModele facturierModele) {
        if (BooleanUtils.isTrue(facturierModele.getClientPays()) && facturierClient.getAdresseLivraison().getPays() != null) {
            adresseTable.addCell(itextCellFactory.createCellHeaderClient(facturierClient.getAdresseLivraison().getPays().getLibelle().toUpperCase()));
        } else {
            addEmptyCell(adresseTable);
        }
    }

    private void createNumeroContact(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if ((BooleanUtils.isTrue(facturierModele.getClientTelephone()) && !StringUtils.isEmpty(facturierClient.getTelephone()))
                || (BooleanUtils.isTrue(facturierModele.getClientPortable()) && !StringUtils.isEmpty(facturierClient.getPortable()))
                || (BooleanUtils.isTrue(facturierModele.getClientFax()) && !StringUtils.isEmpty(facturierClient.getFax()))) {

            if (BooleanUtils.isTrue(facturierModele.getClientTelephone()) && !StringUtils.isEmpty(facturierClient.getTelephone())) {
                stringBuilder.append("Tél: ").append(facturierClient.getTelephone());
            }
            if (BooleanUtils.isTrue(facturierModele.getClientPortable()) && !StringUtils.isEmpty(facturierClient.getPortable())) {
                if (!stringBuilder.toString().equals("")) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Mob: ").append(facturierClient.getPortable());
            }
            if (BooleanUtils.isTrue(facturierModele.getClientFax()) && !StringUtils.isEmpty(facturierClient.getFax())) {
                if (!stringBuilder.toString().equals("")) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Fax: ").append(facturierClient.getFax());
            }
        }
    }

    private void createMailContactRow(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if (BooleanUtils.isTrue(facturierModele.getClientMail()) && !StringUtils.isEmpty(facturierClient.getMail())) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(" - ");
            }
            stringBuilder.append("E-mail: ").append(facturierClient.getMail());
        }
    }

    private void createSiretRow(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if (BooleanUtils.isTrue(facturierModele.getClientSiret()) && !StringUtils.isEmpty(facturierClient.getSiret())) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append("\r");
            }
            stringBuilder.append("Siret: ").append(facturierClient.getSiret());
        }
    }

    private void createTvaIntraRow(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if (BooleanUtils.isTrue(facturierModele.getClientTvaintra()) && !StringUtils.isEmpty(facturierClient.getTvaintra())) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(" - ");
            }
            stringBuilder.append("TVA intra: ").append(facturierClient.getTvaintra());
        }
    }

    private void createChampLibreRow(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if (BooleanUtils.isTrue(facturierModele.getClientChampLibre()) && !StringUtils.isEmpty(facturierClient.getChampLibre())) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append("\r");
            }
            stringBuilder.append(facturierClient.getChampLibre());
        }
    }

    private void createInfoBancaireRow(FacturierClient facturierClient, FacturierModele facturierModele, StringBuilder stringBuilder) {
        if ((BooleanUtils.isTrue(facturierModele.getClientIban()) && !StringUtils.isEmpty(facturierClient.getIban()))
                || (BooleanUtils.isTrue(facturierModele.getClientBic()) && !StringUtils.isEmpty(facturierClient.getRib()))) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append("\r");
            }
            stringBuilder.append("Rérérence bancaire: ");
            if (BooleanUtils.isTrue(facturierModele.getClientBic())) {
                stringBuilder.append(facturierClient.getRib());
            }
            if (BooleanUtils.isTrue(facturierModele.getClientIban())) {
                stringBuilder.append(facturierClient.getIban());
            }
        }
    }

    private void createAdresseSiegeRow(FacturierClient facturierClient, StringBuilder stringBuilder) {
        if (!StringUtils.isEmpty(facturierClient.getAdresseSiege())) {
            if (!stringBuilder.toString().equals("")) {
                stringBuilder.append("\r");
            }
            stringBuilder.append("Adresse siège : ").append(facturierClient.getAdresseSiege()).append(" ")
                    .append(facturierClient.getCpSiege()).append(" ").append(facturierClient.getVilleSiege());
        }
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        if (FacturierType.TYPE_BL.equals(facturierDocument.getType().getId())
                || FacturierType.TYPE_BL_VALORISE.equals(facturierDocument.getType().getId())
                || FacturierType.TYPE_BL_NON_VALORISE.equals(facturierDocument.getType().getId())) {

            createHeaderLivraison(facturierDocument);
        } else {
            createHeaderAdresseStandard(facturierDocument);
        }
    }

    public float calculateHeight(Document document) {
        IRenderer tableRenderer = view.createRendererSubTree().setParent(document.getRenderer());

        LayoutResult tableLayoutResult =
                tableRenderer.layout(new LayoutContext(new LayoutArea(0, new Rectangle(document.getPdfDocument().getDefaultPageSize().getWidth(), 1000))));

        return tableLayoutResult.getOccupiedArea().getBBox().getHeight();
    }

    private void addEmptyCell(Table table) {
        table.addCell(new Cell().setBorder(Border.NO_BORDER));
    }
}
