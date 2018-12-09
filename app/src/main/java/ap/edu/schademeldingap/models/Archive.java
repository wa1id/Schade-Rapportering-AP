package ap.edu.schademeldingap.models;

import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Archive {
    private enum Campus {ELL, MEISTRAAT, NOORDERPLAATS}

    private String user;
    private String lokaal;
    private String vrijeInvoerLokaal;
    private Campus campus;
    private String categorie;
    private String beschrijvingSchade;
    private Date datum;
    private String modifiedDate;
    private boolean gerepareerd;
    private ImageView image;

    public Archive(String user, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade, ImageView image){
        this.user = user;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = Campus.ELL;
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        this.datum = new Date();
        this.modifiedDate = new SimpleDateFormat("dd/MM/yyyy").format(datum);
        this.gerepareerd = false;
        this.image = image;
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

    public Campus getCampus() {
        return campus;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getBeschrijvingSchade() {
        return beschrijvingSchade;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public boolean isGerepareerd() {
        return gerepareerd;
    }

    public ImageView getImage() {
        return image;
    }
}
