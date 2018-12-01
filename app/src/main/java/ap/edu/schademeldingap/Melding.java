package ap.edu.schademeldingap;

public class Melding {
    private String lokaal;
    private String categorie;


    public Melding(){

    }

    public Melding(String lokaal, String categorie) {
        this.lokaal = lokaal;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getLokaal() {
        return lokaal;
    }

    public void setLokaal(String lokaal) {
        this.lokaal = lokaal;
    }
}
