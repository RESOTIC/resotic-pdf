package fr.resotic.pdf.pdf.itextfacture.factureheader;

import com.itextpdf.layout.element.Table;
import fr.resotic.pdf.pdf.domain.facturier.FacturierDocument;
import fr.resotic.pdf.pdf.domain.facturier.FacturierProfil;
import fr.resotic.pdf.pdf.itextfacture.util.ItextCellFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HeaderProfil extends HeaderPart {

    private static final Map<Integer, String> regimeTva = new HashMap<>();
    static {
        regimeTva.put(0, "Franchise en base");
        regimeTva.put(1, "Normal réel mensuel");
        regimeTva.put(2, "Normal réel trimestriel");
        regimeTva.put(3, "Régime simplifié");
        regimeTva.put(5, "Remboursement forfaitaire");
        regimeTva.put(6, "Non concerné / non assujetti");
    }

    private static final Map<Integer, String> paiementTva = new HashMap<>();
    static {
        paiementTva.put(0, " Paiement de la TVA sur débit");
        paiementTva.put(1, " Paiement de la TVA sur encaissement");
        paiementTva.put(2, " Non concerné par l'option de paiement de la TVA");
    }

    public HeaderProfil(ItextCellFactory itextCellFactory) {
        super(itextCellFactory, 5f, 5f, new Table(1));
    }

    public void init(FacturierDocument facturierDocument) throws IOException {
        createNomCommercialRow(facturierDocument);
        if (FacturierProfil.TYPE_PROFIl_EI.equals(facturierDocument.getProfil().getTypeProfil())) {
            createNomPrenomRow(facturierDocument);
        }
        createAdresseRow(facturierDocument);
        createCpVillePaysRow(facturierDocument);
        createNumeroContactRow(facturierDocument);
        createFormeSocietaireRow(facturierDocument);
        createInfoJuridiqueRow(facturierDocument);
        createAdresseFacturationRow(facturierDocument);
    }

    private void createAdresseFacturationRow(FacturierDocument facturierDocument) throws IOException {
        if (BooleanUtils.isNotTrue(facturierDocument.getProfil().getMemeAdresseFacturation()) &&
                !StringUtils.isEmpty(facturierDocument.getProfil().getAdresseFacturation())) {
            StringBuilder adresseFacturation = new StringBuilder();
            adresseFacturation.append(facturierDocument.getProfil().getAdresseFacturation());
            if (!StringUtils.isEmpty(facturierDocument.getProfil().getCpFacturation())) {
                adresseFacturation.append(" ");
                adresseFacturation.append(facturierDocument.getProfil().getCpFacturation());
            }
            if (!StringUtils.isEmpty(facturierDocument.getProfil().getVilleFacturation())) {
                adresseFacturation.append(" ");
                adresseFacturation.append(facturierDocument.getProfil().getVilleFacturation());
            }
            view.addCell(itextCellFactory.createCellHeaderProfilText("Adr. facturation : " + adresseFacturation));
        }
    }

    private void createFormeSocietaireRow(FacturierDocument facturierDocument) throws IOException {
        if ((BooleanUtils.isTrue(facturierDocument.getModele().getProfilFormeSocietaire()) && !StringUtils.isEmpty(facturierDocument.getProfil().getFormeSocietaire()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilCapitalSocial()) && !StringUtils.isEmpty(facturierDocument.getProfil().getCapitalSocial()))
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilFormeSocietaire()) && !StringUtils.isEmpty(facturierDocument.getProfil().getFormeSocietaire())) {
                stringBuilder.append(facturierDocument.getProfil().getFormeSocietaire()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilCapitalSocial()) && !StringUtils.isEmpty(facturierDocument.getProfil().getCapitalSocial())) {
                stringBuilder.append(" au capital de ").append(facturierDocument.getProfil().getCapitalSocial()).append(" €");
            }

            view.addCell(itextCellFactory.createCellHeaderProfilText(stringBuilder.toString()));
        } else {
            addEmptyRow();
        }
    }

    private void createNomCommercialRow(FacturierDocument facturierDocument) throws IOException {
        String nom = "";
        if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilNomCommercial()) && !StringUtils.isEmpty(facturierDocument.getProfil().getNomCommercial())) {
            nom += facturierDocument.getProfil().getNomCommercial();
        }
        if (StringUtils.isEmpty(nom)) {
            addEmptyRow();
        } else {
            view.addCell(itextCellFactory.createCellHeaderTitle(nom));
        }
    }

    private void createNomPrenomRow(FacturierDocument facturierDocument) throws IOException {
        view.addCell(itextCellFactory.createCellHeaderProfilText(
                "EI " + facturierDocument.getProfil().getNom() + " " + facturierDocument.getProfil().getPrenom()
        ));
    }

    private void createAdresseRow(FacturierDocument facturierDocument) throws IOException {
        if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilAdresse()) && (!StringUtils.isEmpty(facturierDocument.getProfil().getAdresse())
                || !StringUtils.isEmpty(facturierDocument.getProfil().getAdresse2()))) {
            if (!StringUtils.isEmpty(facturierDocument.getProfil().getAdresse())) {
                view.addCell(itextCellFactory.createCellHeaderProfilText(facturierDocument.getProfil().getAdresse()));
            }
            if (!StringUtils.isEmpty(facturierDocument.getProfil().getAdresse2())) {
                view.addCell(itextCellFactory.createCellHeaderProfilText(facturierDocument.getProfil().getAdresse2()));
            }
        } else {
            addEmptyRow();
        }
    }

    private void createCpVillePaysRow(FacturierDocument facturierDocument) throws IOException {
        if ((BooleanUtils.isTrue(facturierDocument.getModele().getProfilCp()) && !StringUtils.isEmpty(facturierDocument.getProfil().getCp()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilPays()) && facturierDocument.getProfil().getPays() != null)
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilVille()) && !StringUtils.isEmpty(facturierDocument.getProfil().getVille()))) {

            StringBuilder stringBuilder = new StringBuilder();

            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilCp()) && !StringUtils.isEmpty(facturierDocument.getProfil().getCp())) {
                stringBuilder.append(facturierDocument.getProfil().getCp()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilVille()) && !StringUtils.isEmpty(facturierDocument.getProfil().getVille())) {
                stringBuilder.append(facturierDocument.getProfil().getVille()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilPays()) && facturierDocument.getProfil().getPays() != null) {
                stringBuilder.append(facturierDocument.getProfil().getPays().getLibelle()).append(" ");
            }

            view.addCell(itextCellFactory.createCellHeaderProfilText(stringBuilder.toString()));
        } else {
            addEmptyRow();
        }
    }

    private void createNumeroContactRow(FacturierDocument facturierDocument) throws IOException {
        if ((BooleanUtils.isTrue(facturierDocument.getModele().getProfilTelephone()) && !StringUtils.isEmpty(facturierDocument.getProfil().getTelephone()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilPortable()) && !StringUtils.isEmpty(facturierDocument.getProfil().getPortable()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilFax()) && !StringUtils.isEmpty(facturierDocument.getProfil().getFax()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilMail()) && !StringUtils.isEmpty(facturierDocument.getProfil().getMail()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilSiteInternet()) && !StringUtils.isEmpty(facturierDocument.getProfil().getSiteInternet()))
        ) {

            StringBuilder stringBuilder = new StringBuilder();
            boolean addTiret = false;

            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilTelephone()) && !StringUtils.isEmpty(facturierDocument.getProfil().getTelephone())) {
                stringBuilder.append("Tél. : ").append(facturierDocument.getProfil().getTelephone()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilPortable()) && !StringUtils.isEmpty(facturierDocument.getProfil().getPortable())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Mob. : ").append(facturierDocument.getProfil().getPortable()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilFax()) && !StringUtils.isEmpty(facturierDocument.getProfil().getFax())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Fax. : ").append(facturierDocument.getProfil().getFax());
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilMail()) && !StringUtils.isEmpty(facturierDocument.getProfil().getMail())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("E-mail : ").append(facturierDocument.getProfil().getMail());
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilSiteInternet()) && !StringUtils.isEmpty(facturierDocument.getProfil().getSiteInternet())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(facturierDocument.getProfil().getSiteInternet());
            }
            view.addCell(itextCellFactory.createCellHeaderProfilText(stringBuilder.toString()));
        } else {
            addEmptyRow();
        }
    }

    private void createInfoJuridiqueRow(FacturierDocument facturierDocument) throws IOException {
        boolean addTiret = false;
        if (isInfoJuridiqueExist(facturierDocument)) {
            StringBuilder stringBuilder = new StringBuilder();

            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilSiret()) && !StringUtils.isEmpty(facturierDocument.getProfil().getSiret())) {
                stringBuilder.append("SIRET").append('\u00A0').append(":").append('\u00A0') // "SIRET : "
                        .append(facturierDocument.getProfil().getSiret()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilSiren()) && !StringUtils.isEmpty(facturierDocument.getProfil().getSiren())) {
                stringBuilder.append("- SIREN").append('\u00A0').append(":").append('\u00A0') // "- SIREN : "
                        .append(facturierDocument.getProfil().getSiren()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilTvaintra()) && !StringUtils.isEmpty(facturierDocument.getProfil().getTvaIntra())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(" TVA").append('\u00A0').append("intra").append('\u00A0').append("com.").append('\u00A0').append(":").append('\u00A0') // " TVA intra com. : "
                        .append(facturierDocument.getProfil().getTvaIntra()).append(" ");
                addTiret = true;
            }
            if (facturierDocument.getProfil().getAbonne().getStructure() != null && facturierDocument.getProfil().getAbonne().getStructure().getPaiementTva() != null) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(
                        paiementTva.get(facturierDocument.getProfil().getAbonne().getStructure().getPaiementTva())
                ).append(" ");
                addTiret = true;
            }

            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilRcs()) && !StringUtils.isEmpty(facturierDocument.getProfil().getRcsComplet()) && !" ".equals(facturierDocument.getProfil().getRcsComplet())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(" RCS").append('\u00A0').append(":").append('\u00A0') // " RCS : "
                        .append(facturierDocument.getProfil().getRcsComplet()).append(" ");
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilNumeroInscriptionRepertoireMetier()) && !StringUtils.isEmpty(facturierDocument.getProfil().getNumeroInscriptionRepertoireMetier())) {
                stringBuilder.append("- Rép.").append('\u00A0').append("métier").append('\u00A0').append(":").append('\u00A0') // "- Rép. métier : "
                        .append(facturierDocument.getProfil().getNumeroInscriptionRepertoireMetier()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilApe()) && !StringUtils.isEmpty(facturierDocument.getProfil().getApe())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Code").append('\u00A0').append("APE").append('\u00A0').append(":").append('\u00A0') // "Code APE : "
                        .append(facturierDocument.getProfil().getApe()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilAgrement()) && !StringUtils.isEmpty(facturierDocument.getProfil().getAgrement())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Agrément").append('\u00A0').append(":").append('\u00A0') // "Agrément : "
                        .append(facturierDocument.getProfil().getAgrement()).append(" ");
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilAccises()) && !StringUtils.isEmpty(facturierDocument.getProfil().getAccises())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append("Accises").append('\u00A0').append(":").append('\u00A0') // "Accises : "
                        .append(facturierDocument.getProfil().getAccises());
                addTiret = true;
            }
            if (BooleanUtils.isTrue(facturierDocument.getModele().getProfilChampLibre()) && !StringUtils.isEmpty(facturierDocument.getProfil().getChampLibre())) {
                if (addTiret) {
                    stringBuilder.append(" - ");
                }
                stringBuilder.append(facturierDocument.getProfil().getChampLibre());
            }
            view.addCell(itextCellFactory.createCellHeaderProfilText(stringBuilder.toString()));
        } else {
            addEmptyRow();
        }
    }

    private Boolean isInfoJuridiqueExist(FacturierDocument facturierDocument) throws IOException {
        return (BooleanUtils.isTrue(facturierDocument.getModele().getProfilCapitalSocial()) && !StringUtils.isEmpty(facturierDocument.getProfil().getCapitalSocial()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilApe()) && !StringUtils.isEmpty(facturierDocument.getProfil().getApe()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilAgrement()) && !StringUtils.isEmpty(facturierDocument.getProfil().getAgrement()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilAccises()) && !StringUtils.isEmpty(facturierDocument.getProfil().getAccises()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilSiret()) && !StringUtils.isEmpty(facturierDocument.getProfil().getSiret()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilTvaintra()) && !StringUtils.isEmpty(facturierDocument.getProfil().getTvaIntra()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilRcs()) && !StringUtils.isEmpty(facturierDocument.getProfil().getRcs()))
                || (BooleanUtils.isTrue(facturierDocument.getModele().getProfilChampLibre()) && !StringUtils.isEmpty(facturierDocument.getProfil().getChampLibre()));
    }
}
