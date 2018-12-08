package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.data.Storage;
import ap.edu.schademeldingap.models.Melding;

public class MeldingController {

    /**
     * make new Melding in Firebase Database
     */
    public void nieuweMelding(Melding melding, ImageView image, Context c) { //need context to use getString()
        Database db = new Database();
        DatabaseReference ref = db.getDbReference().child(c.getString(R.string.key_meldingen)).push();

        ref.setValue(melding);
        uploadFotoToFirebase(image, ref.getKey(), c);
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
