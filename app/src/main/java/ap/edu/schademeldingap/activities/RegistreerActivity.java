package ap.edu.schademeldingap.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.models.User;

public class RegistreerActivity extends AbstractActivity {

    private FirebaseAuth mAuth;

    private Button buttonRegistreer;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editName;
    private CheckBox checkReparateur;
    private ProgressBar progressBar;

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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        buttonRegistreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editEmail.getText().toString(), editPassword.getText().toString());
            }
        });
    }

    private void writeNewUser(String userId, String name, boolean reparateur) {
        User user = new User(name, reparateur);
        Database db = new Database();
        db.getDbReference().child("users").child(userId).setValue(user);
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    writeNewUser(user.getUid(), editName.getText().toString(), checkReparateur.isChecked());

                    //Show popup after success
                    showDialogInfoToActivity(RegistreerActivity.this, MainActivity.class,
                            getString(R.string.geslaagd),
                            getString(R.string.registreer_succes));

                } else {

                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(RegistreerActivity.this, getString(R.string.error_ongeldig_email), Toast.LENGTH_LONG).show();
                            editEmail.setError(getString(R.string.error_ongeldig_email));
                            editEmail.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(RegistreerActivity.this, getString(R.string.wachtwoord_ongeldig), Toast.LENGTH_LONG).show();
                            editPassword.setError(getString(R.string.wachtwoord_ongeldig));
                            editPassword.requestFocus();
                            editPassword.setText("");
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            editEmail.setError(getString(R.string.error_email_bestaat));
                            editEmail.requestFocus();
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            editPassword.setError(getString(R.string.error_zwak_wachtwoord));
                            editPassword.requestFocus();
                            break;

                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        //checken op lege editText's
        String email = editEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editEmail.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            editEmail.setError(null);
        }

        String pass = editPassword.getText().toString();
        if (TextUtils.isEmpty(pass)) {
            editPassword.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            editPassword.setError(null);
        }

        String name = editName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            editName.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            editName.setError(null);
        }

        return valid;
    }
}
