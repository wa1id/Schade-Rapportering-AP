package ap.edu.schademeldingap;

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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Melding {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private String user;
    private String lokaal;
    private String vrijeInvoerLokaal;
    private String campus;
    private String categorie;
    private String beschrijvingSchade;
    private Date datum;
    private String modifiedDate;
    private boolean gerepareerd;
    private ImageView image;

    public Melding(String user, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade, ImageView image) {
        this.user = user;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = "ELL";
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        this.datum = new Date();
        this.modifiedDate = new SimpleDateFormat("dd/MM/yyyy").format(datum);
        this.gerepareerd = false;
        this.image = image;
    }

    //TODO: This should be in a controller class, not in the model
    public void nieuweMelding(Melding m) {
        DatabaseReference meldingId = myRef.child("meldingen").push();

        meldingId.child("user").setValue(m.user);
        meldingId.child("lokaal").setValue(m.lokaal);
        meldingId.child("lokaal vrije invoor").setValue(m.vrijeInvoerLokaal);
        meldingId.child("campus").setValue(campus);
        meldingId.child("categorie").setValue(categorie);
        meldingId.child("beschrijving schade").setValue(m.beschrijvingSchade);
        meldingId.child("datum").setValue(modifiedDate);
        meldingId.child("gerepareerd").setValue(gerepareerd);
        uploadFotoToFirebase(m.image, meldingId.getKey());
    }

    //TODO: This should be in a controller class, not in the model
    private void uploadFotoToFirebase(ImageView image, String name) {
        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.child("images/" + name).putBytes(data);
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
