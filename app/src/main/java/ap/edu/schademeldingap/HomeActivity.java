package ap.edu.schademeldingap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button buttonSignOut;
    private Button buttonSchadeMelden;
    private Button buttonSchadeZoeken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        buttonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);

        //Tijdelijke sign out knop, moet ergens anders gezet worden
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
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
}
