package ap.edu.schademeldingap.models;

public class Stats {
    private int archiefTotal;
    private int meldingCurrent;
    private int meldingTotal;

    public Stats() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public int getArchiefTotal() {
        return archiefTotal;
    }

    public int getMeldingCurrent() {
        return meldingCurrent;
    }

    public int getMeldingTotal() {
        return meldingTotal;
    }
}
