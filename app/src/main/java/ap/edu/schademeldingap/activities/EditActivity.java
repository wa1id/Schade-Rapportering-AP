package ap.edu.schademeldingap.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.MeldingController;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.models.Melding;

public class EditActivity extends AbstractActivity {

    public Database db;

    private String id;
    private TextView mTextDatePicker;
    private Spinner mSpinnerWhoRepairs;
    private EditText mEditNameRepair;
    private EditText mEditExtraNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        setTitle("AANPASSEN");

        db = new Database();

        id = getIntent().getStringExtra("id");
        mTextDatePicker = findViewById(R.id.textDatePicker);
        mSpinnerWhoRepairs = findViewById(R.id.spinnerWhoRepairs);
        mEditNameRepair = findViewById(R.id.editNameRepair);
        mEditExtraNotes = findViewById(R.id.editExtraNotes);

        Button buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MeldingController mc = new MeldingController();

                DatabaseReference ref = db.getDbReference().child(getString(R.string.key_meldingen)).child(id);
                String date = mTextDatePicker.getText().toString();
                String naamUitvoerder = mEditNameRepair.getText().toString();
                String extraNotes = mEditExtraNotes.getText().toString();
                String uitvoerder = "";

                switch (mSpinnerWhoRepairs.getSelectedItemPosition()) {
                    case 0: //empty
                        break;
                    case 1: //AP
                        uitvoerder = mSpinnerWhoRepairs.getItemAtPosition(1).toString();
                        break;
                    case 2: //Provincie
                        uitvoerder = mSpinnerWhoRepairs.getItemAtPosition(2).toString();
                        break;
                }

                mc.editMelding(ref, EditActivity.this, date, uitvoerder, naamUitvoerder, extraNotes);

                showDialogInfoAndFinish(EditActivity.this, getString(R.string.opgeslagen), getString(R.string.uw_wijzigingen_zijn_correct_opgeslagen));
            }

        });

        loadData();
    }

    private void loadData() {
        //Check if data is empty, if false then set correct data
        db.getDbReference().child(getString(R.string.key_meldingen)).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Melding melding = dataSnapshot.getValue(Melding.class);

                if (!melding.getReparatieDatum().isEmpty()) {
                    mTextDatePicker.setText(melding.getReparatieDatum());
                }

                if (!melding.getReparatieUitvoerder().isEmpty()) {
                    if (melding.getReparatieUitvoerder().equals(getString(R.string.ap))) {
                        mSpinnerWhoRepairs.setSelection(1);
                    } else if (melding.getReparatieUitvoerder().equals(getString(R.string.provincie))) {
                        mSpinnerWhoRepairs.setSelection(2);
                    }
                }

                if (!melding.getNaamReparatieUitvoerder().isEmpty()) {
                    mEditNameRepair.setText(melding.getNaamReparatieUitvoerder());
                }

                if (!melding.getExtraNotities().isEmpty()) {
                    mEditExtraNotes.setText(melding.getExtraNotities());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        });
    }

}