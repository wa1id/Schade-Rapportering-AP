package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.WindowManager;
import android.util.Log;
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

public class HuidigeSchadesActivity extends AbstractActivity {

    private ListView listView;
    private ArrayList<String> alleMeldingen;
    private ArrayList<String> alleIds;
    private ChildEventListener mListener;
    private ArrayAdapter<String> adapterAlleMeldingen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lijstschades);

        listView = findViewById(R.id.listView);

        alleIds = new ArrayList<>();
        alleMeldingen = new ArrayList<>();
        adapterAlleMeldingen = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alleMeldingen);
        listView.setAdapter(adapterAlleMeldingen);
        EditText theFilter = findViewById(R.id.searchFilter);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        theFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (HuidigeSchadesActivity.this).adapterAlleMeldingen.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                alleMeldingen.add(dataSnapshot.child(getString(R.string.key_lokaal)).getValue() + " --- " + dataSnapshot.child(getString(R.string.key_categorie)).getValue());
                alleIds.add(dataSnapshot.getKey());
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
        getDbReference().child(getString(R.string.key_meldingen)).addChildEventListener(mListener);

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
        getDbReference().child(getString(R.string.key_meldingen)).removeEventListener(mListener);

        super.onStop();
    }
}
