package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.activities.HomeActivity;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.data.Storage;
import ap.edu.schademeldingap.models.Melding;
import ap.edu.schademeldingap.models.User;

public class MeldingController { //TODO: Mss naam verandere naar DataController want die gaat ook archief behandelen

    private Database db = new Database();

    /**
     * make new Melding in Firebase Database
     */
    public void nieuweMelding(Melding melding, ImageView image, Context c) { //need context to use getString()
        DatabaseReference ref = db.getDbReference().child(c.getString(R.string.key_meldingen)).push();

        melding.setId(ref.getKey());
        ref.setValue(melding);
        uploadFotoToFirebase(image, ref.getKey(), c);
    }

    /**
     * Move Melding to Archive
     */
    public void archiveerMelding(Melding melding, Context c) {
        DatabaseReference ref = db.getDbReference().child(c.getString(R.string.key_archives));

        melding.setGerepareerd(true);
        ref.child(getKeyOfMelding(melding)).setValue(melding);
        deleteMelding(melding);
    }

    /**
     * Delete melding
     */
    private void deleteMelding(Melding melding) {
        //db.getDbReference().child(getString(R.string.key_meldingen)).child(id).removeValue();
        db.getDbReference().child("meldingen").child(melding.getId()).removeValue();
    }

    /**
     * Get the key of a Melding
     */
    private String getKeyOfMelding(Melding melding) {
        return melding.getId();
    }

    /**
     * Uploads photo to Storage
     */
    private void uploadFotoToFirebase(ImageView image, String name, Context c) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.child(c.getString(R.string.path_images) + name).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("upload", "Failed to upload image");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("upload", "Success upload");
            }
        });
    }


}
