package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.UserController;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.interfaces.MyCallback;
import ap.edu.schademeldingap.models.User;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button buttonArchive;
    private TextView mTextWelkom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HOME");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        Button buttonSignOut = findViewById(R.id.buttonSignOut);
        Button buttonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        Button buttonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);
        buttonArchive = findViewById(R.id.buttonArchive);
        mTextWelkom = findViewById(R.id.textWelkom);

        setupHome();

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
                homeToMelden.putExtra(getString(R.string.key_naam), mTextWelkom.getText().toString().substring(7));
                startActivity(homeToMelden);
            }
        });

        buttonSchadeZoeken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeToSearchIntent(getString(R.string.key_meldingen));
            }
        });

        buttonArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeToSearchIntent(getString(R.string.key_archives));
            }
        });
    }

    /**
     * Method to make a new intent for either Archive or Melding
     * @param key database key we want to use in DetailActivity (meldingen/archives)
     */
    private void homeToSearchIntent(String key) {
        Intent homeToSearch = new Intent(HomeActivity.this, HuidigeSchadesActivity.class);
        homeToSearch.putExtra("detail", key);
        startActivity(homeToSearch);
    }

    /**
     * setup the homepage for the current user. Get the name and check if reparateur
     */
    private void setupHome() {
        UserController uc = new UserController();
        uc.getUserData(new MyCallback() {
            @Override
            public void onUserCallback(User user) {
                mTextWelkom.append(" " + user.getName());

                if (user.getReparateur()) {
                    buttonArchive.setVisibility(View.VISIBLE);
                }
            }
        }, HomeActivity.this);
    }
}