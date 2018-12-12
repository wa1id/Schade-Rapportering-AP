package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;

public class HomeActivity extends AbstractActivity {

    private FirebaseAuth mAuth;

    private Button buttonSignOut;
    private Button buttonSchadeMelden;
    private Button buttonSchadeZoeken;
    private Button buttonArchive;
    private TextView textWelkom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        buttonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);
        buttonArchive = findViewById(R.id.buttonArchive);
        textWelkom = findViewById(R.id.textWelkom);

        //tonen of verbergen van de archive button
        archiveVisibility();

        //Titel van view veranderen naar naam van huidige user
        setNaam(mAuth.getCurrentUser(), textWelkom);

        //Tijdelijke sign out knop, moet ergens anders gezet worden
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }
        });

        buttonSchadeMelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeToMelden = new Intent(HomeActivity.this, NieuweMeldingActivity.class);
                homeToMelden.putExtra(getString(R.string.key_naam), textWelkom.getText().toString().substring(7));
                startActivity(homeToMelden);
            }
        });

       buttonSchadeZoeken.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(HomeActivity.this, HuidigeSchadesActivity.class));
           }
       });





    }


    private void archiveVisibility(){
        Database db = new Database();
        db.getDbReference().child("users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getString(R.string.key_reparateur)).getValue().equals(true)){
                    buttonArchive.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setNaam(FirebaseUser user, final TextView textView) {
        Database db = new Database();

        db.getDbReference().child(getString(R.string.key_users)).child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textView.setText(textView.getText() + " " + dataSnapshot.child(getString(R.string.key_naam)).getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, getString(R.string.kan_naam_niet_ophalen), Toast.LENGTH_SHORT).show();
            }
        });
    }



}
