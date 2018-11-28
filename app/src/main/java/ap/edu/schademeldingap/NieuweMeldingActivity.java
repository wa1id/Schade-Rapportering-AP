package ap.edu.schademeldingap;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NieuweMeldingActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("meldingen");

    private Button buttonMeldenSchade;
    private EditText vrijeInvoer;
    private EditText beschrijvingSchade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_melding);

        buttonMeldenSchade = findViewById(R.id.buttonMeldenSchade);
        vrijeInvoer = findViewById(R.id.editVrijeInvoer);
        beschrijvingSchade = findViewById(R.id.editBeschrijving);

        final Spinner spinnerCategorie = findViewById(R.id.spinnerCategorie);
        final Spinner spinnerVerdieping = findViewById(R.id.spinnerVerdieping);
        final Spinner spinnerLokaal = findViewById(R.id.spinnerLokaal);

        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this, R.array.categorien, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterVerdieping = ArrayAdapter.createFromResource(this,R.array.verdieping, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiepMin1 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiepMin1, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiepGelijkvloer = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiepGelijkVloer, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep1 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep1, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep2 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep2, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep3 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep3, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep4 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep4, android.R.layout.simple_spinner_item);


        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterVerdieping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepMin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepGelijkvloer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCategorie.setAdapter(adapterCategorie);
        spinnerVerdieping.setAdapter(adapterVerdieping);

        spinnerVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("-1"))
                {
                   spinnerLokaal.setAdapter(adapterLokaalVerdiepMin1);
                }else if(selectedItem.equals("0")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiepGelijkvloer);
                }else if(selectedItem.equals("1")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep1);
                }else if(selectedItem.equals("2")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep2);
                }else if(selectedItem.equals("3")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep3);
                }else{
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


             buttonMeldenSchade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateForm()) {
                    return;
                }


                DatabaseReference myMelding = myRef.push();
                myMelding.child("campus").setValue("ELL");
                myMelding.child("lokaal").setValue(spinnerLokaal.getSelectedItem().toString());
                myMelding.child("lokaal vrij invoer").setValue(vrijeInvoer.getText().toString());
                myMelding.child("categorie").setValue(spinnerCategorie.getSelectedItem().toString());
                myMelding.child("beschrijving schade").setValue(beschrijvingSchade.getText().toString());
                myMelding.child("gerepareerd").setValue(false);



                //Popup geslaagd tonen en naar andere activity gaan
                AlertDialog.Builder builder;

                builder = new AlertDialog.Builder(NieuweMeldingActivity.this, android.R.style.Theme_Material_Dialog_Alert);

                builder.setTitle("Geslaagd!")
                        .setMessage("Uw melding werd succesvol gecreëerd.")
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
    private boolean validateForm() {
        boolean valid = true;

        String beschrijving = beschrijvingSchade.getText().toString();
        if (TextUtils.isEmpty(beschrijving)) {
            beschrijvingSchade.setError("Verplicht.");
            valid = false;
        } else {
            beschrijvingSchade.setError(null);
        }
        return valid;
    }

}
