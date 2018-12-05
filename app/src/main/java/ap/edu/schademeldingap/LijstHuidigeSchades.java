package ap.edu.schademeldingap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LijstHuidigeSchades extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("meldingen");

    private ListView listView;
    private ArrayList<String> alleMeldingen;
    private ArrayList<String> alleIds;
    private ArrayAdapter<String> adapter;
    private Melding melding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lijstschades);
        
        listView = findViewById(R.id.listView);
        alleMeldingen = new ArrayList<>();
        alleIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alleMeldingen); //todo: Data uit map halen ipv arraylist
        listView.setAdapter(adapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                alleMeldingen.add(dataSnapshot.child("lokaal").getValue() + " --- " + dataSnapshot.child("categorie").getValue());
                alleIds.add(dataSnapshot.getKey());

                Log.d("lokaalcategorie", "onChildAdded: "+ melding.getLokaal() +" --- "+melding.getCategorie());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent = new Intent(LijstHuidigeSchades.this, DetailActivity.class);
                Log.d("list", alleIds.get(position));
            }
        });
    }
}
