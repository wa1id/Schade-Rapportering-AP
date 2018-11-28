package ap.edu.schademeldingap;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "Aanmelden";

    private Button buttonAanmelden;
    private Button buttonRegistreren;
    private EditText editEmail;
    private EditText editWachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        buttonAanmelden = findViewById(R.id.buttonAanmelden);
        buttonRegistreren = findViewById(R.id.buttonNietGeregistreerd);
        editEmail = findViewById(R.id.editEmail);
        editWachtwoord = findViewById(R.id.editWachtwoord);

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
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        //todo: show progressDialog

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authenticatie mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authenticatie mislukt.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //todo: hide progressBar
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        return valid;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Controleer of er al een user is ingelogd en verander UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish(); //zorgt ervoor dat de gebruiker niet terug kan door back button
        }
    }


}
