package fr.resotic.pdf.pdf.domain.facturier;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.resotic.pdf.pdf.domain.Abonne;
import fr.resotic.pdf.pdf.domain.Rib;
import fr.resotic.pdf.pdf.serializer.IdDeserializer;
import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class FacturierDocument implements Cloneable {

    public static final Integer OPTION_PAIEMENT_JOUR = 1;
    public static final Integer OPTION_PAIEMENT_DATE = 2;

    public static final Integer CALCUL_TVA_LIGNE = 1;
    public static final Integer CALCUL_TVA_GLOBAL = 2;

    private Integer id;

    private FacturierDocument documentReference;

    private FacturierType type;

    private String data;

    private String titre;

    private String note;

    private BigDecimal totalHt;

    private BigDecimal totalTtc;

    private BigDecimal totalTva;

    private BigDecimal netHt;

    private BigDecimal remiseHt;

    private String reference;

    private Date date;

    private List<FacturierDocumentRelance> relances;

    private Set<FacturierDocument> bls;

    private String signature;

    private FacturierClient client;

    private FacturierProfil profil;

    private FacturierModele modele;

    private List<FacturierAcompteForm> acomptes;

    private BigDecimal totalAcompte;
    private BigDecimal totalHtAcompte;
    private BigDecimal totalTvaAcompte;

    private BigDecimal totalTtcSubAcompte;
    private BigDecimal totalHtSubAcompte;
    private BigDecimal totalNetHtSubAcompte;
    private BigDecimal totalTvaSubAcompte;

    private String chorusNumEngagement;

    private String chorusCodeService;

    private Integer typeVente;

    private Boolean factureSolde;

    private Rib rib;


    public FacturierDocument() {
        // constructeur par défaut
    }

    public FacturierDocumentRelance getRelance() {
        return relances != null ? relances
                .stream()
                .max(Comparator.comparing(FacturierDocumentRelance::getDate)).orElse(null) : null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FacturierDocument getDocumentReference() {
        return documentReference;
    }

    public FacturierType getType() {
        return type;
    }

    public String getTitre() {
        return titre;
    }

    public String getNote() {
        return note;
    }

    public BigDecimal getTotalHt() {
        if (totalHt == null) {
            return new BigDecimal(0);
        }
        return totalHt;
    }

    public BigDecimal getTotalTtc() {
        if (totalTtc == null) {
            return new BigDecimal(0);
        }
        return totalTtc;
    }

    public BigDecimal getTotalTva() {
        if (totalTva == null) {
            return new BigDecimal(0);
        }
        return totalTva;
    }

    public String getReference() {
        return reference;
    }

    public Date getDate() {
        return date;
    }

    public FacturierProfil getProfil() throws IOException {
        if (this.data != null && profil == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
            FacturierDocumentData data = mapper.readValue(this.data, FacturierDocumentData.class);
            if (data.getProfil() != null) {
                this.profil = data.getProfil();
            }
        }
        return profil;
    }

    public Rib getRib() throws IOException {
        if (this.data != null && rib == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
            FacturierDocumentData data = mapper.readValue(this.data, FacturierDocumentData.class);
            if (data.getRib() != null) {
                this.rib = data.getRib();
            }
        }
        return rib;
    }

    public FacturierModele getModele() throws IOException {
        if (this.data != null && modele == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
            FacturierDocumentData data = mapper.readValue(this.data, FacturierDocumentData.class);
            if (data.getModele() != null) {
                this.modele = data.getModele();
            }
        }
        return modele;
    }

    public List<FacturierAcompteForm> getAcomptes() throws IOException {
        if (this.data != null && acomptes == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
            FacturierDocumentData data = mapper.readValue(this.data, FacturierDocumentData.class);
            if (data.getAcomptes() != null) {
                this.acomptes = data.getAcomptes();
            }
        }
        return acomptes;
    }

    public FacturierClient getClient() throws IOException {
        if (client == null) {
            if (data == null) {
                this.client = new FacturierClient();
            } else {
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                mapper.setAnnotationIntrospector(IdDeserializer.IGNORE_JSON_DESERIALIZE_ANNOTATIONS);
                FacturierDocumentData data = mapper.readValue(this.data, FacturierDocumentData.class);
                if (data.getClient() == null) {
                    this.client = new FacturierClient();
                } else {
                    this.client = data.getClient();
                }
            }
        }
        return client;
    }

    public List<FacturierDocumentRelance> getRelances() {
        return relances;
    }

    public String getBlsReferents() {
        List<String> blsReferents = new ArrayList<>();
        if (bls != null) {
            int cpt = 0;
            for (FacturierDocument blReferent : bls) {
                blsReferents.add(blReferent.getReference()
                        .replace("Bon de livraison non valorisé", "BL")
                        .replace("Bon de livraison valorisé", "BL"));
                cpt += 1;
                if (cpt > 4) {
                    break;
                }
            }
            if (bls.size() > 5) {
                blsReferents.add("... cf détail facture");
            }
        }
        return blsReferents.stream().collect(Collectors.joining(", "));
    }

    public BigDecimal getNetHt() {
        if (netHt == null) {
            return new BigDecimal(0);
        }
        return netHt;
    }

    public BigDecimal getRemiseHt() {
        return remiseHt;
    }

    public BigDecimal getTotalAcompte() throws IOException {
        if (totalAcompte == null) {
            totalAcompte = new BigDecimal(0);
            if (getAcomptes() != null) {
                for (FacturierAcompteForm acompte : getAcomptes()) {
                    if (BooleanUtils.isTrue(acompte.getAvoir())) {
                        totalAcompte = totalAcompte.subtract(acompte.getMontant());
                    } else {
                        totalAcompte = totalAcompte.add(acompte.getMontant());
                    }
                }
            }
        }
        return totalAcompte;
    }

    public BigDecimal getTotalHtAcompte() throws IOException {
        if (totalHtAcompte == null) {
            totalHtAcompte = new BigDecimal(0);
            if (getAcomptes() != null) {
                for (FacturierAcompteForm acompte : getAcomptes()) {
                    if (BooleanUtils.isTrue(acompte.getAvoir())) {
                        totalHtAcompte = totalHtAcompte.subtract(acompte.getMontantHt());
                    } else {
                        totalHtAcompte = totalHtAcompte.add(acompte.getMontantHt());
                    }
                }
            }
        }
        return totalHtAcompte;
    }

    public BigDecimal getTotalTvaAcompte() throws IOException {
        if (totalTvaAcompte == null) {
            totalTvaAcompte = new BigDecimal(0);
            if (getAcomptes() != null) {
                for (FacturierAcompteForm acompte : getAcomptes()) {
                    if (BooleanUtils.isTrue(acompte.getAvoir())) {
                        totalTvaAcompte = totalTvaAcompte.subtract(acompte.getMontantTva());
                    } else {
                        totalTvaAcompte = totalTvaAcompte.add(acompte.getMontantTva());
                    }
                }
            }
        }
        return totalTvaAcompte;
    }

    public String getSignature() {
        return signature;
    }

    public BigDecimal getTotalTtcSubAcompte() throws IOException {
        return getTotalTtc().subtract(getTotalAcompte());
    }

    public BigDecimal getTotalHtSubAcompte() throws IOException {
        return getTotalHt().subtract(getTotalHtAcompte());
    }

    public BigDecimal getTotalNetHtSubAcompte() throws IOException {
        return getNetHt().subtract(getTotalHtAcompte());
    }

    public BigDecimal getTotalTvaSubAcompte() throws IOException {
        return getTotalTva().subtract(getTotalTvaAcompte());
    }

    public String getChorusNumEngagement() {
        return chorusNumEngagement;
    }

    public String getChorusCodeService() {
        return chorusCodeService;
    }

    public boolean hasAcompte() throws IOException {
        return getAcomptes() != null && !getAcomptes().isEmpty();
    }

    public boolean hasAcompteV2() throws IOException {
        if (hasAcompte()) {
            return getAcomptes().get(0).getMontantHt() != null;
        } else {
            return false;
        }
    }

    public Integer getTypeVente() {
        return typeVente;
    }

    public Boolean getFactureSolde() {
        return factureSolde;
    }

}
