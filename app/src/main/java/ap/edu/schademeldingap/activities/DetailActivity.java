package ap.edu.schademeldingap.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import ap.edu.schademeldingap.interfaces.IMeldingCallback;
import ap.edu.schademeldingap.interfaces.IUserCallback;
import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.MeldingController;
import ap.edu.schademeldingap.controllers.UserController;
import ap.edu.schademeldingap.data.Storage;
import ap.edu.schademeldingap.models.Melding;
import ap.edu.schademeldingap.models.User;

public class DetailActivity extends AbstractActivity {

    private String id;
    private TextView textLokaalExtra;
    private TextView textBeschrijving2;
    private TextView textGerepareerd;
    private TextView textUser;
    private TextView textLokaal;
    private TextView textCategorie;
    private TextView textDatum;
    private TextView textLokaalOmschrijving;
    private ImageView imageView;
    private Switch mSwitchArchive;
    private String mMoreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("DETAIL");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        id = getIntent().getStringExtra("id");

        textLokaalExtra = findViewById(R.id.textLokaalExtra);
        textBeschrijving2 = findViewById(R.id.textBeschrijving2);
        imageView = findViewById(R.id.imageSchade);
        textGerepareerd = findViewById(R.id.textGerepareerd);
        textUser = findViewById(R.id.textUser);
        textLokaal = findViewById(R.id.textLokaal);
        textCategorie = findViewById(R.id.textCategorie);
        textDatum = findViewById(R.id.textDatum);
        textLokaalOmschrijving = findViewById(R.id.textLokaalOmschrijving);
        mSwitchArchive = findViewById(R.id.switchArchive);
        Button buttonMoreInfo = findViewById(R.id.buttonMoreInfo);

        //setupInterface();

        mSwitchArchive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    confirmArchive();
                }
            }
        });

        buttonMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogInfo(DetailActivity.this, getString(R.string.meer_informatie), mMoreInfo);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupInterface();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetInterface();
    }

    private void resetInterface(){
        textUser.setText(getString(R.string.melder));
        textLokaal.setText(getString(R.string.lokaal_dubbelpunt));
        textCategorie.setText(getString(R.string.categorie_dubbelpunt));
        textDatum.setText(getString(R.string.datum_dubbelpunt));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        UserController uc = new UserController();

        //Only show menu when user isReparateur
        uc.getUserData(DetailActivity.this, new IUserCallback() {
            @Override
            public void onUserCallback(User user) {

                if (!user.getReparateur()) {
                    menu.getItem(0).setVisible(false);
                }

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuEdit:
                Intent editIntent = new Intent(DetailActivity.this, EditActivity.class);
                editIntent.putExtra("id", this.id);
                startActivity(editIntent);
                break;
        }
        return true;
    }

    private void setupInterface() {
        MeldingController mc = new MeldingController();

        mc.getMelding(getIntent().getStringExtra("detail"), id, DetailActivity.this, new IMeldingCallback() {
            @Override
            public void onMeldingCallback(Melding melding) {
                textUser.append(" "+ melding.getName());
                textLokaal.append(" " + melding.getVerdieping() + "." + melding.getLokaal());
                textLokaalExtra.setText(melding.getVrijeInvoerLokaal());
                textCategorie.append(" " + melding.getCategorie());
                textDatum.append(" " + melding.getDatum());
                textBeschrijving2.setText(melding.getBeschrijvingSchade());
                textLokaalOmschrijving.setText(melding.getBeschrijvingLokaal().trim()); //use trim to delete spaces before word

                if (melding.isGerepareerd()) {
                    textGerepareerd.setText(getString(R.string.gerepareerd_ja));
                } else {
                    textGerepareerd.setText(getString(R.string.gerepareerd_nee));
                }

                setupMoreInfoDialog(melding.getReparatieDatum(), melding.getReparatieUitvoerder(), melding.getNaamReparatieUitvoerder(), melding.getExtraNotities());

                checkEmptyLabels();
                displayImage(imageView);
            }
        });
        reparateurVisibility();
    }

    private void setupMoreInfoDialog(String datum, String uitvoerder, String reparateur, String extraNotes) {
        if (datum.isEmpty()) datum = getString(R.string.niet_bekend);
        if (uitvoerder.isEmpty()) uitvoerder = getString(R.string.niet_bekend);
        if (reparateur.isEmpty()) reparateur = getString(R.string.niet_bekend);
        if (extraNotes.isEmpty()) extraNotes = getString(R.string.geen_extra_notities);


        mMoreInfo = getString(R.string.reparatie_datum) + " " + datum + "\n" +
                getString(R.string.reparatie_uitvoerder) + " " + uitvoerder + "\n" +
                getString(R.string.naam_van_reparateur) + " " + reparateur + "\n" +
                getString(R.string.extra_notities) + " " + extraNotes;
    }

    /**
     * archive & delete Melding
     */
    private void archiveer() {
        final MeldingController mc = new MeldingController();

        mc.getMelding(getIntent().getStringExtra("detail"), id, DetailActivity.this, new IMeldingCallback() {
            @Override
            public void onMeldingCallback(Melding melding) {
                mc.archiveerMelding(melding, DetailActivity.this);
                mc.deleteMelding(melding, DetailActivity.this);
            }
        });
    }

    /**
     * Show popup alerting the user he is about to archive a Melding
     */
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
                        mSwitchArchive.setChecked(false);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Check if current user is reparateur and show switch if true
     */
    private void reparateurVisibility() {
        UserController uc = new UserController();

        uc.getUserData(DetailActivity.this, new IUserCallback() {
            @Override
            public void onUserCallback(User user) {
                if (user.getReparateur()) {
                    mSwitchArchive.setVisibility(View.VISIBLE);
                }
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