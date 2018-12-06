package ap.edu.schademeldingap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference refUsers;

    private Button buttonSignOut;
    private Button buttonSchadeMelden;
    private Button buttonSchadeZoeken;
    private TextView textWelkom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        refUsers = database.getReference().child(getString(R.string.key_users));

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        buttonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);
        textWelkom = findViewById(R.id.textWelkom);

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
                startActivity(new Intent(v.getContext(), NieuweMeldingActivity.class));
            }
        });

       buttonSchadeZoeken.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(HomeActivity.this, LijstHuidigeSchades.class));
           }
       });
    }

    private void setNaam(FirebaseUser user, final TextView textView) {

        refUsers.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
