package ap.edu.schademeldingap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistreerActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("users");
    private static final String TAG = "registratie";

    private Button buttonRegistreer;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editName;
    private CheckBox checkReparateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreer);

        mAuth = FirebaseAuth.getInstance();

        buttonRegistreer = findViewById(R.id.buttonRegister);
        editUsername = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editName = findViewById(R.id.editName);
        checkReparateur = findViewById(R.id.checkReparateur);

        buttonRegistreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(editUsername.getText().toString(), editPassword.getText().toString());
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
        if (!validateForm()) { //todo: form valideren, bv. email is required
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
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegistreerActivity.this, "Registratie mislukt", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private boolean validateForm() {

        return true;
    }

    private void updateUI(FirebaseUser user) { //todo: wordt opgeroepen wanneer er user doorverwezen moet worden naar de juiste activity na inloggen (home)

    }
}
