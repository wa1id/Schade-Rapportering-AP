package ap.edu.schademeldingap.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.ArchiveController;
import ap.edu.schademeldingap.controllers.MeldingController;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.data.Storage;
import ap.edu.schademeldingap.models.Melding;

public class DetailActivity extends AbstractActivity {

    private FirebaseAuth mAuth;
    private Database db;
    private String id;
    private TextView textLokaal;
    private TextView textLokaalExtra;
    private TextView textCategorie;
    private TextView textDatum;
    private TextView textBeschrijving2;
    private TextView textGerepareerd;
    private ImageView imageView;
    private Switch switchArchive;
    private ArchiveController archiveController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("DETAIL");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        db = new Database();

        id = getIntent().getStringExtra("id");
        final TextView textUser = findViewById(R.id.textUser);
        textLokaal = findViewById(R.id.textLokaal);
        textLokaalExtra = findViewById(R.id.textLokaalExtra);
        textCategorie = findViewById(R.id.textCategorie);
        textDatum = findViewById(R.id.textDatum);
        textBeschrijving2 = findViewById(R.id.textBeschrijving2);
        imageView = findViewById(R.id.imageSchade);
        textGerepareerd = findViewById(R.id.textGerepareerd);
        switchArchive = findViewById(R.id.switchArchive);

        //check if current user is reparateur
        reparateurVisibility();

        switchArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (switchArchive.isChecked()) {
                    confirmArchive();
                }
            }

        });

        //TODO: Logic in controller
        db.getDbReference().child(getString(R.string.key_meldingen)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Melding m = dataSnapshot.getValue(Melding.class);

                textUser.setText(m.getName());
                textLokaal.setText(m.getVerdieping() + "." + m.getLokaal());
                textLokaalExtra.setText(m.getVrijeInvoerLokaal());
                textCategorie.setText(m.getCategorie());
                textDatum.setText(m.getDatum());
                textBeschrijving2.setText(m.getBeschrijvingSchade());

                if (m.isGerepareerd()) {
                    textGerepareerd.setText(getString(R.string.gerepareerd_ja));
                } else {
                    textGerepareerd.setText(getString(R.string.gerepareerd_nee));
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

    //TODO: logic in controller
    private void archiveer() {
        db.getDbReference().child(getString(R.string.key_meldingen)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Melding m = dataSnapshot.getValue(Melding.class);

                MeldingController mc = new MeldingController();
                mc.archiveerMelding(m, DetailActivity.this);
                mc.deleteMelding(m, DetailActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DetailActivity.this, R.string.data_ophalen_mislukt, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmArchive() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(DetailActivity.this);

        builder.setTitle(getString(R.string.bent_u_zeker))
                .setMessage(getString(R.string.bent_u_zeker_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        archiveer();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchArchive.setChecked(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Check if current user is reparateur and show button if true
     */
    private void reparateurVisibility() {
        mAuth = FirebaseAuth.getInstance();
        db.getDbReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getString(R.string.key_reparateur)).getValue().equals(true)) {
                    textGerepareerd.setVisibility(View.VISIBLE);
                    switchArchive.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Check for empty labels and hide them
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
        Storage storage = new Storage();
        StorageReference imageRef = storage.getStorageReference().child(getString(R.string.path_images) + id);

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