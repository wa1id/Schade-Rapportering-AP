package ap.edu.schademeldingap.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class AbstractActivity extends AppCompatActivity {

    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public StorageReference getStorageReference() {
        return storageReference;
    }

    /**
     * Show dialog with alert icon and OK button.
     * @param context Context of dialog
     * @param title Title of dialog
     * @param message Message of dialog
     */
    public void showDialogAlert(Context context, String title, String message){
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //unused
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Show dialog with info icon and OK button that goes to another activity
     * @param context Context of dialog
     * @param nextActivity What activity to go to
     * @param title Title of dialog
     * @param message Message of dialog
     */
    public void showDialogInfoToActivity(final Context context, final Class nextActivity, String title, String message){
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context);

        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(context, nextActivity));
                        finish(); //zorgt ervoor dat de gebruiker niet terug kan door back button
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
