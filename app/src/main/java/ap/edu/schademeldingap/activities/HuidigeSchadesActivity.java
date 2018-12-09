package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.WindowManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.models.Melding;

public class HuidigeSchadesActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> alleMeldingen;
    private ArrayList<String> alleIds;
    private ChildEventListener mListener;
    private ArrayAdapter<String> adapterAlleMeldingen;
    private Database db;
    private ArrayList<String> tempList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huidigeschades);

        listView = findViewById(R.id.listView);
        alleIds = new ArrayList<>();
        alleMeldingen = new ArrayList<>();
        tempList = new ArrayList<>();
        adapterAlleMeldingen = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alleMeldingen);
        EditText editSearch = findViewById(R.id.editSearch);

        listView.setAdapter(adapterAlleMeldingen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //makes sure the listview doesnt move when keyboard pops up.

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    listView.setAdapter(adapterAlleMeldingen);
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Melding melding = dataSnapshot.getValue(Melding.class);

                alleIds.add(dataSnapshot.getKey());

                alleMeldingen.add(melding.getVerdieping() + "." + melding.getLokaal()
                        + "   ---   "
                        + melding.getCategorie());
                adapterAlleMeldingen.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //unused
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //unused
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //unused
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        };
        db = new Database();
        db.getDbReference().child(getString(R.string.key_meldingen)).addChildEventListener(mListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(HuidigeSchadesActivity.this, DetailActivity.class);
                detailIntent.putExtra("id", alleIds.get(position));
                startActivity(detailIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        db.getDbReference().child(getString(R.string.key_meldingen)).removeEventListener(mListener);
        super.onStop();
    }

    private void searchItem(String textToSearch) {
        tempList.clear();

        for (int i = 0; i < alleMeldingen.size(); i++) {
            if (alleMeldingen.get(i).toLowerCase().contains(textToSearch.toLowerCase())) {
                tempList.add(alleMeldingen.get(i));
            }
        }

        ArrayAdapter<String> adapterTempList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tempList);
        listView.setAdapter(adapterTempList);
        adapterTempList.notifyDataSetChanged();
    }
}
