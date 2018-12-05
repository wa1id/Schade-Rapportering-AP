package ap.edu.schademeldingap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference meldingRef = database.getReference().child("meldingen");
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private String id;
    private TextView textLokaal;
    private TextView textLokaalExtra;
    private TextView textCategorie;
    private TextView textDatum;
    private TextView textBeschrijving;
    private TextView textGerepareerd;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        id = getIntent().getStringExtra("id");
        textLokaal = findViewById(R.id.textLokaal);
        textLokaalExtra = findViewById(R.id.textLokaalExtra);
        textCategorie = findViewById(R.id.textCategorie);
        textDatum = findViewById(R.id.textDatum);
        textBeschrijving = findViewById(R.id.textBeschrijving2);
        textGerepareerd = findViewById(R.id.textGerepareerd);
        imageView = findViewById(R.id.imageSchade);

        meldingRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textLokaal.setText("Lokaal: " + dataSnapshot.child("lokaal").getValue().toString());
                textLokaalExtra.setText(dataSnapshot.child("lokaal vrije invoor").getValue().toString());
                textCategorie.setText("Categorie: " + dataSnapshot.child("categorie").getValue().toString());
                textDatum.setText("Datum: " + dataSnapshot.child("datum").getValue().toString());
                textBeschrijving.setText(dataSnapshot.child("beschrijving schade").getValue().toString());

                if (dataSnapshot.child("gerepareerd").getValue().toString().equals(false)) {
                    textGerepareerd.setText("Gerepareerd: Nee");
                } else {
                    textGerepareerd.setText("Gerepareerd: Ja");
                }

                checkEmptyLabels();
                displayImage(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, getString(R.string.data_ophalen_mislukt), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *  Check for empty labels and hide them
     */
    private void checkEmptyLabels() {
        if (textLokaalExtra.getText().length() == 0) {
            textLokaalExtra.setVisibility(View.GONE);
        }

        if (textBeschrijving.getText().length() == 0) {
            textBeschrijving.setVisibility(View.GONE);
        }
    }

    /**
     * Displays image in imageView
     */
    private void displayImage(final ImageView image) {
        StorageReference imageRef = storageRef.child("images/" + id);

        final ProgressBar progressFoto = findViewById(R.id.progressFoto);
        final TextView textFotoLaden = findViewById(R.id.textFotoLaden);

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bitmap);
                progressFoto.setVisibility(View.GONE);
                textFotoLaden.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                textFotoLaden.setText(R.string.foto_downloaden_mislukt);
                progressFoto.setVisibility(View.GONE);
            }
        });
    }
}
