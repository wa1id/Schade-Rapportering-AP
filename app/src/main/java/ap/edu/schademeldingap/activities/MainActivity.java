package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ap.edu.schademeldingap.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editEmail;
    private EditText editWachtwoord;
    private ProgressBar progressLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button buttonAanmelden = findViewById(R.id.buttonAanmelden);
        Button buttonRegistreren = findViewById(R.id.buttonNietGeregistreerd);
        Button buttonWachtwoordVergeten = findViewById(R.id.buttonWachtwoordVergeten);
        editEmail = findViewById(R.id.editEmail);
        editWachtwoord = findViewById(R.id.editWachtwoord);
        progressLogin = findViewById(R.id.progressLogin);

        editWachtwoord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            signIn(editEmail.getText().toString(), editWachtwoord.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        buttonAanmelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(editEmail.getText().toString(), editWachtwoord.getText().toString());
            }
        });

        buttonRegistreren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegistreerActivity.class));
                finish();
            }
        });

        buttonWachtwoordVergeten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WachtwoordVergetenActivity.class));
            }
        });
    }

    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        showLoginProgress();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, getString(R.string.login_error),
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            hideLoginProgress();
                            Toast.makeText(MainActivity.this, getString(R.string.login_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        //check for empty email
        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            editEmail.setError(null);
        }

        //check for empty password
        String password = editWachtwoord.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editWachtwoord.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            editWachtwoord.setError(null);
        }
        return valid;
    }

    /**
     * Show progressbar and hide EditText's while user is signing in
     */
    private void showLoginProgress() {
        progressLogin.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.INVISIBLE);
        editWachtwoord.setVisibility(View.INVISIBLE);
    }

    /**
     * Show progressbar and hide EditText's while user is signing in
     */
    private void hideLoginProgress() {
        progressLogin.setVisibility(View.INVISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editWachtwoord.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Controleer of er al een user is ingelogd en verander UI
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }
    }


}