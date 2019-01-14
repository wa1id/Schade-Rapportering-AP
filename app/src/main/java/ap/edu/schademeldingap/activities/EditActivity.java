package ap.edu.schademeldingap.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.models.Melding;

public class EditActivity extends AppCompatActivity {
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

        id = getIntent().getStringExtra("id");
        mTextDatePicker = findViewById(R.id.textDatePicker);
        mSpinnerWhoRepairs = findViewById(R.id.spinnerWhoRepairs);
        mEditNameRepair = findViewById(R.id.editNameRepair);
        mEditExtraNotes = findViewById(R.id.editExtraNotes);

        loadData();
    }



    private void loadData() {
        db = new Database();

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