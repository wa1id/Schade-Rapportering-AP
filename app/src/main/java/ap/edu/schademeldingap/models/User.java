package ap.edu.schademeldingap.models;

public class User {

    private String name;
    private boolean reparateur;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, boolean reparateur) {
        this.name = name;
        this.reparateur = reparateur;
    }

    public String getName() {
        return name;
    }

    public boolean getReparateur() {
        return reparateur;
    }
}