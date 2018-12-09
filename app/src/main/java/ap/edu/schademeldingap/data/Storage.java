package ap.edu.schademeldingap.data;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Storage {
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public StorageReference getStorageReference() {
        return storageReference;
    }
}
