package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.UserController;
import ap.edu.schademeldingap.interfaces.IUserCallback;
import ap.edu.schademeldingap.models.User;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button mButtonArchive;
    private Button mButtonSignOut;
    private Button mButtonSchadeMelden;
    private Button mButtonSchadeZoeken;
    private TextView mTextWelkom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HOME");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mButtonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        mButtonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);
        mButtonArchive = findViewById(R.id.buttonArchive);
        mTextWelkom = findViewById(R.id.textWelkom);
        mButtonSignOut = findViewById(R.id.buttonSignOut);
        mButtonSignOut = findViewById(R.id.buttonSignOut);

        setupHome();


        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }
        });
        mButtonSchadeMelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeToMelden = new Intent(HomeActivity.this, NieuweMeldingActivity.class);
                homeToMelden.putExtra(getString(R.string.key_naam), mTextWelkom.getText().toString().substring(7));
                startActivity(homeToMelden);
            }
        });

        mButtonSchadeZoeken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeToSearchIntent(getString(R.string.key_meldingen));
            }
        });

        mButtonArchive.setOnClickListener(new View.OnClickListener() {
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
        uc.getUserData(HomeActivity.this, new IUserCallback() {
            @Override
            public void onUserCallback(User user) {
                mTextWelkom.append(" " + user.getName());

                if (user.getReparateur()) {
                    mButtonArchive.setVisibility(View.VISIBLE);
                }

                showInterface();
            }
        });
    }

    /**
     * Show buttons/textviews & hide progressbar after setupHome()
     */
    private void showInterface() {
        ProgressBar progressLoading = findViewById(R.id.progressLoading);

        progressLoading.setVisibility(View.GONE);
        mButtonSchadeMelden.setVisibility(View.VISIBLE);
        mButtonSchadeZoeken.setVisibility(View.VISIBLE);
        mTextWelkom.setVisibility(View.VISIBLE);
    }
}