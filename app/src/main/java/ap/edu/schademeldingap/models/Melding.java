package ap.edu.schademeldingap.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Melding {

    private enum Campus {ELL, MEISTRAAT, NOORDERPLAATS}

    private String name;
    private String verdieping;
    private String lokaal;
    private String vrijeInvoerLokaal;
    private Campus campus;
    private String categorie;
    private String beschrijvingSchade;
    private String datum;
    private boolean gerepareerd;

    public Melding() {
        // Default constructor required for calls to DataSnapshot.getValue(Melding.class)
    }

    public Melding(String name, String verdieping, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade) {
        this.name = name;
        this.verdieping = verdieping;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = Campus.ELL;
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        Date date = new Date();
        this.datum = new SimpleDateFormat("dd/MM/yyyy").format(date);
        this.gerepareerd = false;
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
}
