package ap.edu.schademeldingap.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ap.edu.schademeldingap.R;

public class WachtwoordVergetenActivity extends AbstractActivity {

    private EditText mEditMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Wachtwoord herstellen");

        setContentView(R.layout.activity_wachtwoordvergeten);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        mEditMail = findViewById(R.id.editEmailHerstellen);
        Button buttonReset = findViewById(R.id.buttonWachtwoordHestellen);

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                auth.sendPasswordResetEmail(mEditMail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            showDialogInfoToActivity(WachtwoordVergetenActivity.this, MainActivity.class,
                                    getString(R.string.geslaagd),
                                    getString(R.string.wachtwoord_reset_email_verzonden));
                        } else {
                            mEditMail.setError(getString(R.string.ongeldige_email));
                        }
                    }
                });
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        //check for empty email
        String email = mEditMail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEditMail.setError(getString(R.string.verplicht));
            valid = false;
        } else {
            mEditMail.setError(null);
        }
        return valid;
    }
}
