package ap.edu.schademeldingap.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import ap.edu.schademeldingap.controllers.MeldingController;
import ap.edu.schademeldingap.models.Melding;
import ap.edu.schademeldingap.R;

public class NieuweMeldingActivity extends AbstractActivity {

    private MeldingController mc;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText vrijeInvoer;
    private EditText beschrijvingSchade;
    private ImageView imageThumbnail;

    private Spinner spinnerCat;
    private Spinner spinnerLokaal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_melding);
        setTitle("SCHADE MELDEN");

        //Permissions
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
        }

        //variabelen linken aan de UI
        Button buttonMeldenSchade = findViewById(R.id.buttonMeldenSchade);
        Button buttonFoto = findViewById(R.id.buttonFoto);
        vrijeInvoer = findViewById(R.id.editVrijeInvoer);
        beschrijvingSchade = findViewById(R.id.editBeschrijving);
        imageThumbnail = findViewById(R.id.imageThumbnail);
        spinnerCat = findViewById(R.id.spinnerCategorie);
        final Spinner spinnerVerdieping = findViewById(R.id.spinnerVerdieping);
        spinnerLokaal = findViewById(R.id.spinnerLokaal);

        //De juiste lokalen tonen bij desbetreffende verdiepingen
        spinnerVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiepMin1);
                        break;
                    case 1:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiepGelijkVloer);
                        break;
                    case 2:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiep1);
                        break;
                    case 3:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiep2);
                        break;
                    case 4:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiep3);
                        break;
                    case 5:
                        fillSpinnerLokaalWithAdapter(R.array.lokaalVerdiep4);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //unused
            }
        });

        //Button listerens
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        buttonMeldenSchade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {
                    return;
                }

                String name = getIntent().getStringExtra(getString(R.string.key_naam));
                String lokaal = spinnerLokaal.getSelectedItem().toString();
                Melding melding = new Melding(name,
                                        spinnerVerdieping.getSelectedItem().toString(),
                                        lokaal.substring(0,2),
                                        vrijeInvoer.getText().toString(),
                                        spinnerCat.getSelectedItem().toString(),
                                        beschrijvingSchade.getText().toString(),
                                        lokaal.substring(3,lokaal.length()));
                mc = new MeldingController();
                mc.nieuweMelding(melding, imageThumbnail, v.getContext());

                showDialogInfoToActivity(NieuweMeldingActivity.this, HomeActivity.class,
                        getString(R.string.geslaagd),
                        getString(R.string.melding_succes));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageThumbnail.setImageBitmap(imageBitmap);
        }
    }

    private void fillSpinnerLokaalWithAdapter(int verdiepingArrayAdapter) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, verdiepingArrayAdapter, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLokaal.setAdapter(adapter);
    }

    private boolean validateForm() {
        boolean valid = true;

        if (imageThumbnail.getDrawable() == null) {
            showDialogAlert(NieuweMeldingActivity.this, getString(R.string.fout), getString(R.string.foto_is_verplicht));
            valid = false;
        }

        return valid;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
