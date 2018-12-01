package ap.edu.schademeldingap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

public class NieuweMeldingActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Melding melding;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Button buttonMeldenSchade;
    private Button buttonFoto;
    private EditText vrijeInvoer;
    private EditText beschrijvingSchade;
    private ImageView imageThumbnail;

    private Spinner spinnerCat;
    private Spinner spinnerVerdieping;
    private Spinner spinnerLokaal;

    private ArrayAdapter<CharSequence> adapterLokaalVerdiepMin1;
    private ArrayAdapter<CharSequence> adapterLokaalVerdiepGelijkvloer;
    private ArrayAdapter<CharSequence> adapterLokaalVerdiep1;
    private ArrayAdapter<CharSequence> adapterLokaalVerdiep2;
    private ArrayAdapter<CharSequence> adapterLokaalVerdiep3;
    private ArrayAdapter<CharSequence> adapterLokaalVerdiep4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_melding);

        //Permissions
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
        }
      
        mAuth = FirebaseAuth.getInstance();

        //variabelen linken aan de UI
        buttonMeldenSchade = findViewById(R.id.buttonMeldenSchade);
        buttonFoto = findViewById(R.id.buttonFoto);
        vrijeInvoer = findViewById(R.id.editVrijeInvoer);
        beschrijvingSchade = findViewById(R.id.editBeschrijving);
        imageThumbnail = findViewById(R.id.imageThumbnail);
        spinnerCat = findViewById(R.id.spinnerCategorie);
        spinnerVerdieping = findViewById(R.id.spinnerVerdieping);
        spinnerLokaal = findViewById(R.id.spinnerLokaal);

        setAdapters();

        //De juiste lokalen tonen bij desbetreffende verdiepingen
        spinnerVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                switch (selectedItem) {
                    case "-1":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiepMin1);
                        break;
                    case "0":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiepGelijkvloer);
                        break;
                    case "1":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiep1);
                        break;
                    case "2":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiep2);
                        break;
                    case "3":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiep3);
                        break;
                    case "4":
                        spinnerLokaal.setAdapter(adapterLokaalVerdiep4);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

                melding = new Melding(mAuth.getCurrentUser().getUid(),
                                        spinnerLokaal.getSelectedItem().toString(),
                                        vrijeInvoer.getText().toString(),
                                        spinnerCat.getSelectedItem().toString(),
                                        beschrijvingSchade.getText().toString(),
                                        imageThumbnail);

                melding.nieuweMelding(melding);

                //Popup geslaagd tonen en naar andere activity gaan
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(NieuweMeldingActivity.this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle(getString(R.string.geslaagd))
                        .setMessage(getString(R.string.melding_succes))
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(NieuweMeldingActivity.this, HomeActivity.class));
                                finish(); //zorgt ervoor dat de gebruiker niet terug kan door back button
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
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

    private void setAdapters() {
        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this, R.array.categorien, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterVerdieping = ArrayAdapter.createFromResource(this, R.array.verdieping, android.R.layout.simple_spinner_item);

        adapterLokaalVerdiepMin1 = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiepMin1, android.R.layout.simple_spinner_item);
        adapterLokaalVerdiepGelijkvloer = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiepGelijkVloer, android.R.layout.simple_spinner_item);
        adapterLokaalVerdiep1 = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiep1, android.R.layout.simple_spinner_item);
        adapterLokaalVerdiep2 = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiep2, android.R.layout.simple_spinner_item);
        adapterLokaalVerdiep3 = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiep3, android.R.layout.simple_spinner_item);
        adapterLokaalVerdiep4 = ArrayAdapter.createFromResource(this, R.array.lokaalVerdiep4, android.R.layout.simple_spinner_item);


        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterVerdieping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepMin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepGelijkvloer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //inhoud tonen van de spinners
        spinnerCat.setAdapter(adapterCategorie);
        spinnerVerdieping.setAdapter(adapterVerdieping);
    }
  
    private boolean validateForm() {
        boolean valid = true;

        String beschrijving = beschrijvingSchade.getText().toString();
        if (TextUtils.isEmpty(beschrijving)) {
            beschrijvingSchade.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            beschrijvingSchade.setError(null);
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
