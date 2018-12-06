package ap.edu.schademeldingap.activities;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class AbstractActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public DatabaseReference getDbReference() {
        return dbReference;
    }

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
