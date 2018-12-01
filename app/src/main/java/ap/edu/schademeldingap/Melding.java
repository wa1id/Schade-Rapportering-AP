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

public class Melding {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference().child("meldingen");
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    private String user;
    private String lokaal;
    private String vrijeInvoerLokaal;
    private String campus;
    private String categorie;
    private String beschrijvingSchade;
    private boolean gerepareerd;
    private ImageView image;

    public Melding(){

    }

    public Melding(String lokaal, String categorie) {
        this.lokaal = lokaal;
        this.categorie = categorie;
    }

    public Melding(String user, String lokaal, String vrijeInvoerLokaal, String categorie, String beschrijvingSchade, ImageView image) {
        this.user = user;
        this.lokaal = lokaal;
        this.vrijeInvoerLokaal = vrijeInvoerLokaal;
        this.campus = "ELL";
        this.categorie = categorie;
        this.beschrijvingSchade = beschrijvingSchade;
        this.gerepareerd = false;
        this.image = image;
    }

    public void nieuweMelding(Melding m) {
        DatabaseReference nieuweMelding = myRef.push();

        nieuweMelding.child("user").setValue(m.user);
        nieuweMelding.child("lokaal").setValue(m.lokaal);
        nieuweMelding.child("lokaal vrije invoor").setValue(m.vrijeInvoerLokaal);
        nieuweMelding.child("campus").setValue(campus);
        nieuweMelding.child("categorie").setValue(categorie);
        nieuweMelding.child("beschrijving schade").setValue(m.beschrijvingSchade);
        nieuweMelding.child("gerepareerd").setValue(gerepareerd);
        uploadFotoToFirebase(m.image, nieuweMelding.getKey());
    }

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
