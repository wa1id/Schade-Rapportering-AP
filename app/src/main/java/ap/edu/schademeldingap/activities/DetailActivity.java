package ap.edu.schademeldingap.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import ap.edu.schademeldingap.R;

public class DetailActivity extends AbstractActivity {

    private String id;
    private TextView textLokaal;
    private TextView textLokaalExtra;
    private TextView textCategorie;
    private TextView textDatum;
    private TextView textBeschrijving2;
    private TextView textGerepareerd;
    private ImageView imageView;
    private Switch switchArchive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        id = getIntent().getStringExtra("id");
        textLokaal = findViewById(R.id.textLokaal);
        textLokaalExtra = findViewById(R.id.textLokaalExtra);
        textCategorie = findViewById(R.id.textCategorie);
        textDatum = findViewById(R.id.textDatum);
        textBeschrijving2 = findViewById(R.id.textBeschrijving2);
        imageView = findViewById(R.id.imageSchade);
        switchArchive = findViewById(R.id.switchArchive);

        switchArchive.setTextOff("nee");
        switchArchive.setTextOn("Ja");

        final Boolean switchState = switchArchive.isChecked();


        getDbReference().child(getString(R.string.key_meldingen)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textLokaal.setText(getString(R.string.lokaal_dubbelpunt) + dataSnapshot.child(getString(R.string.key_lokaal)).getValue().toString());
                textLokaalExtra.setText(dataSnapshot.child(getString(R.string.key_lokaal_vrije_invoer)).getValue().toString());
                textCategorie.setText(getString(R.string.categorie_dubbelpunt) + dataSnapshot.child(getString(R.string.key_categorie)).getValue().toString());
                textDatum.setText(getString(R.string.datum_dubbelpunt) + dataSnapshot.child(getString(R.string.key_datum)).getValue().toString());
                textBeschrijving2.setText(dataSnapshot.child(getString(R.string.key_beschrijving_schade)).getValue().toString());

               if (dataSnapshot.child(getString(R.string.key_gerepareerd)).getValue().equals(false)) {
                   switchArchive.setChecked(false);
                   if (switchState.equals(true)){

                   }
                } else {
                    switchArchive.setChecked(true);
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
        TextView textBeschrijving = findViewById(R.id.textBeschrijving);

        if (textLokaalExtra.getText().length() == 0) {
            textLokaalExtra.setVisibility(View.GONE);
        }

        if (textBeschrijving2.getText().length() == 0) {
            textBeschrijving.setVisibility(View.GONE);
            textBeschrijving2.setVisibility(View.GONE);
        }
    }

    /**
     * Displays image in imageView
     */
    private void displayImage(final ImageView image) {
        StorageReference imageRef = getStorageReference().child(getString(R.string.path_images) + id);

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
