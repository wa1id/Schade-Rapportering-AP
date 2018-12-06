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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ap.edu.schademeldingap.interfaces.MeldingInterface;
import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.models.Melding;

public class MeldingController implements MeldingInterface {
    @Override
    public void nieuweMelding(Melding m, Context c) { //need context to use getString()
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child(c.getString(R.string.key_meldingen)).push();

        ref.child(c.getString(R.string.key_users)).setValue(m.getUser());
        ref.child(c.getString(R.string.key_lokaal)).setValue(m.getLokaal());
        ref.child(c.getString(R.string.key_lokaal_vrije_invoer)).setValue(m.getVrijeInvoerLokaal());
        ref.child(c.getString(R.string.key_campus)).setValue(m.getCampus());
        ref.child(c.getString(R.string.key_categorie)).setValue(m.getCategorie());
        ref.child(c.getString(R.string.key_beschrijving_schade)).setValue(m.getBeschrijvingSchade());
        ref.child(c.getString(R.string.key_datum)).setValue(m.getModifiedDate());
        ref.child(c.getString(R.string.key_gerepareerd)).setValue(m.isGerepareerd());
        uploadFotoToFirebase(m.getImage(), ref.getKey(), c);
    }

    @Override
    public void uploadFotoToFirebase(ImageView image, String name, Context c) {
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
