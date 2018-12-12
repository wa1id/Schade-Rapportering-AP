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

    private Database db;

    private ListView mListView;
    private EditText mEditSearch;
    private Spinner mSpinnerCategory;

    private ArrayList<String> mAlleMeldingen;
    private ArrayList<String> mMeldingIds;
    private ArrayList<String> mTempList;
    private ArrayAdapter<String> mAdapterAlleMeldingen;
    private ArrayAdapter<String> mAdapterTempList;

    private ChildEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HUIDIGE SCHADES");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huidigeschades);

        mListView = findViewById(R.id.listView);
        mEditSearch = findViewById(R.id.editSearch);
        mSpinnerCategory = findViewById(R.id.spinnerCategorie);

        mMeldingIds = new ArrayList<>();
        mAlleMeldingen = new ArrayList<>();
        mTempList = new ArrayList<>();
        mAdapterAlleMeldingen = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mAlleMeldingen);
        mAdapterTempList = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTempList);

        mListView.setAdapter(mAdapterAlleMeldingen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //makes sure the listview doesnt move when keyboard pops up.

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("") && mSpinnerCategory.getSelectedItemPosition() == 0) {
                    mListView.setAdapter(mAdapterAlleMeldingen);
                } else {
                    searchItem(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //unused
            }

            @Override
            public void afterTextChanged(Editable s) {
                //unused
            }
        });

        mListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Melding melding = dataSnapshot.getValue(Melding.class);

                mMeldingIds.add(dataSnapshot.getKey());

                mAlleMeldingen.add(melding.getVerdieping() + "." + melding.getLokaal()
                        + "   ---   "
                        + melding.getCategorie());
                mAdapterAlleMeldingen.notifyDataSetChanged();
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

        mSpinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mTempList.clear();

                if (position == 0) { //position 0 = Alles. Reset adapter and recheck searchbar
                    mListView.setAdapter(mAdapterAlleMeldingen);
                    recheckSearch();
                }

                if (position != 0) { //check what category is selected and filter accordingly
                    mListView.setAdapter(mAdapterTempList);
                    for (int i = 0; i < mAlleMeldingen.size(); i++) {
                        if (mAlleMeldingen.get(i).contains(adapterView.getSelectedItem().toString())) {
                            mTempList.add(mAlleMeldingen.get(i));
                        }
                    }
                    recheckSearch();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //unused
            }
        });

        db = new Database();
        db.getDbReference().child(getString(R.string.key_meldingen)).addChildEventListener(mListener);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(HuidigeSchadesActivity.this, DetailActivity.class);
                detailIntent.putExtra("id", mMeldingIds.get(position));
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
        mTempList.clear();

        mListView.setAdapter(mAdapterTempList);

        for (int i = 0; i < mAlleMeldingen.size(); i++) {
            if (mAlleMeldingen.get(i).toLowerCase().contains(textToSearch.toLowerCase())) {
                if (mSpinnerCategory.getSelectedItemPosition() != 0) {
                    if (mAlleMeldingen.get(i).contains(mSpinnerCategory.getSelectedItem().toString())) {
                        mTempList.add(mAlleMeldingen.get(i));
                    }
                } else {
                    mTempList.add(mAlleMeldingen.get(i));
                }
            }
        }
        mAdapterTempList.notifyDataSetChanged();
    }

    private void recheckSearch() {
        if (!mEditSearch.getText().toString().isEmpty())
            searchItem(mEditSearch.getText().toString());
    }
}