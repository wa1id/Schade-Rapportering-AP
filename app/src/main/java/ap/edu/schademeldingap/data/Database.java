package ap.edu.schademeldingap.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference();

    public DatabaseReference getDbReference() {
        return dbReference;
    }
}
