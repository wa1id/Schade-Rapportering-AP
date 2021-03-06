package ap.edu.schademeldingap.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Melding {

    private enum Campus {ELL, MEISTRAAT, NOORDERPLAATS}

    private String name;
    private String verdieping;
    private String lokaal;
    private String vrijeInvoerLokaal;
    private String campus;
    private String categorie;
    private String beschrijvingSchade;
    private String beschrijvingLokaal;
    private String datum;
    private String id;
    private String token; //used for notifications
    private String reparatieDatum;
    private String reparatieUitvoerder;
    private String extraNotities;
    private String naamReparatieUitvoerder;
    private boolean gerepareerd;

    public Melding() {
        // Default constructor required for calls to DataSnapshot.getValue(Melding.class)
    }

    public Melding(String name, String verdieping, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade, String beschrijvingLokaal) {
        this.name = name;
        this.verdieping = verdieping;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = Campus.ELL.name();
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        Date date = new Date();
        this.datum = new SimpleDateFormat("dd/MM/yyyy").format(date);
        this.gerepareerd = false;
        this.id = "";
        this.token = "";
        this.beschrijvingLokaal = beschrijvingLokaal;
    }

    public String getBeschrijvingLokaal() {
        return beschrijvingLokaal;
    }

    public void setBeschrijvingLokaal(String beschrijvingLokaal) {
        this.beschrijvingLokaal = beschrijvingLokaal;
    }

    public String getReparatieDatum() {
        return reparatieDatum;
    }

    public void setReparatieDatum(String reparatieDatum) {
        this.reparatieDatum = reparatieDatum;
    }

    public String getReparatieUitvoerder() {
        return reparatieUitvoerder;
    }

    public void setReparatieUitvoerder(String reparatieUitvoerder) {
        this.reparatieUitvoerder = reparatieUitvoerder;
    }

    public String getExtraNotities() {
        return extraNotities;
    }

    public void setExtraNotities(String extraNotities) {
        this.extraNotities = extraNotities;
    }

    public String getNaamReparatieUitvoerder() {
        return naamReparatieUitvoerder;
    }

    public void setNaamReparatieUitvoerder(String naamReparatieUitvoerder) {
        this.naamReparatieUitvoerder = naamReparatieUitvoerder;
    }

    public String getName() {
        return name;
    }

    public String getLokaal() {
        return lokaal;
    }

    public String getVrijeInvoerLokaal() {
        return vrijeInvoerLokaal;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getBeschrijvingSchade() {
        return beschrijvingSchade;
    }

    public String getDatum() {
        return datum;
    }

    public boolean isGerepareerd() {
        return gerepareerd;
    }

    public String getVerdieping() {
        return verdieping;
    }

    public String getCampus() {
        return campus;
    }

    public String getId() {
        return id;
    }

    public void setGerepareerd(boolean gerepareerd) {
        this.gerepareerd = gerepareerd;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}