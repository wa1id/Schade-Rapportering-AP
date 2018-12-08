package ap.edu.schademeldingap.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Melding {

    private enum Campus {ELL, MEISTRAAT, NOORDERPLAATS}

    private String user;
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

    public Melding(String user, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade) {
        this.user = user;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = Campus.ELL;
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        Date date = new Date();
        this.datum = new SimpleDateFormat("dd/MM/yyyy").format(date);
        this.gerepareerd = false;
    }

    public String getUser() {
        return user;
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
}
