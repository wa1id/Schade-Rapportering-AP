package ap.edu.schademeldingap.activities;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ap.edu.schademeldingap.R;

public class WachtwoordVergetenActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoordvergeten);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText editEmail = findViewById(R.id.editEmailHerstellen);
        Button buttonReset = findViewById(R.id.buttonWachtwoordHestellen);

        final String email = editEmail.getText().toString();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        showDialogInfoToActivity(WachtwoordVergetenActivity.this, MainActivity.class,
                                getString(R.string.geslaagd),
                                getString(R.string.wachtwoord_reset_email_verzonden));
                    }
                });
            }
        });
    }
}
