package ap.edu.schademeldingap.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ap.edu.schademeldingap.R;

public class WachtwoordVergetenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoordvergeten);

        final FirebaseAuth auth = FirebaseAuth.getInstance();

        final EditText textEmail = findViewById(R.id.editEmailHerstellen);
        Button buttonReset = findViewById(R.id.buttonWachtwoordHestellen);

        final String email = textEmail.getText().toString();

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder builder;

                        builder = new AlertDialog.Builder(WachtwoordVergetenActivity.this);

                        builder.setTitle(getString(R.string.geslaagd))
                                .setMessage(getString(R.string.wachtwoord_reset_email_verzonden))
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(WachtwoordVergetenActivity.this, MainActivity.class));
                                        finish(); //zorgt ervoor dat de gebruiker niet terug kan door back button
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .show();
                    }
                });
            }
        });
    }
}
