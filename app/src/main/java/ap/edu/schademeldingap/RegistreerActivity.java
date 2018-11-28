package ap.edu.schademeldingap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistreerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");
    private static final String TAG = "registratie";

    private Button buttonRegistreer;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private CheckBox checkReparateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreer);

        mAuth = FirebaseAuth.getInstance();

        buttonRegistreer = findViewById(R.id.buttonRegister);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        checkReparateur = findViewById(R.id.checkReparateur);

        buttonRegistreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editEmail.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");

                        FirebaseUser user  = mAuth.getCurrentUser();

                        //Extra user informatie die opgeslagen moet worden
                        DatabaseReference myRefUser = myRef.child(user.getUid());
                        myRefUser.child("naam").setValue(editName.getText().toString());
                        myRefUser.child("reparateur").setValue(checkReparateur.isChecked());

                        updateUI(user);
                } else {

                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                        switch (errorCode) {
                            case "ERROR_INVALID_EMAIL":
                                Toast.makeText(RegistreerActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                editEmail.setError(getString(R.string.error_invalid_email));
                                editEmail.requestFocus();
                                break;

                            case "ERROR_WRONG_PASSWORD":
                                Toast.makeText(RegistreerActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                editPassword.setError("Wachtwoord ongeldig.");
                                editPassword.requestFocus();
                                editPassword.setText("");
                                break;

                            case "ERROR_EMAIL_ALREADY_IN_USE":
                                editEmail.setError(getString(R.string.error_user_exists));
                                editEmail.requestFocus();
                                break;

                            case "ERROR_WEAK_PASSWORD":
                                editPassword.setError(getString(R.string.error_weak_password));
                                editPassword.requestFocus();
                                break;

                        }
                    updateUI(null);
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Verplicht.");
            valid = false;
        } else {
            editEmail.setError(null);
        }

        String pass = editPassword.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            editPassword.setError("Verplicht.");
            valid = false;
        } else {
            editPassword.setError(null);
        }

        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            editName.setError("Verplicht.");
            valid = false;
        } else {
            editName.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) { //todo: wordt opgeroepen wanneer er user doorverwezen moet worden naar de juiste activity na inloggen (home)

    }
}
