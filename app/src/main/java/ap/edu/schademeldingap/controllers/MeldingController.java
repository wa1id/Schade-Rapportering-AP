package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.RetrofitClient;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.interfaces.ApiService;
import ap.edu.schademeldingap.interfaces.IMeldingCallback;
import ap.edu.schademeldingap.models.Melding;
import ap.edu.schademeldingap.models.notifications.NotificationFCM;
import ap.edu.schademeldingap.models.notifications.NotificationResponse;
import ap.edu.schademeldingap.models.notifications.Sender;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeldingController {

    private Database db = new Database();
    private static String baseUrl = "https://fcm.googleapis.com/";

    public static ApiService getFCMClient() {
        return RetrofitClient.getClient(baseUrl).create(ApiService.class);
    }

    /**
     * Get Melding data by id
     */
    public void getMelding(String rootKey, String id, final Context context, final IMeldingCallback callback) {
        db.getDbReference().child(rootKey).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Melding melding = dataSnapshot.getValue(Melding.class);
                callback.onMeldingCallback(melding);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, context.getString(R.string.data_ophalen_mislukt), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * make new Melding in Firebase Database
     */
    public void nieuweMelding(Melding melding, ImageView image, Context context) { //need context to use getString()
        DatabaseReference ref = db.getDbReference().child(context.getString(R.string.key_meldingen)).push();

        melding.setId(ref.getKey());
        melding.setToken(FirebaseInstanceId.getInstance().getToken());
        ref.setValue(melding);
        uploadFotoToFirebase(image, ref.getKey(), context);
    }

    /**
     * Move Melding to Archive and delete the Melding
     */
    public void archiveerMelding(Melding melding, final Context context) {
        DatabaseReference ref = db.getDbReference().child(context.getString(R.string.key_archives));

        NotificationFCM notification = new NotificationFCM("Uw melding op " + melding.getDatum() + " van lokaal " + melding.getVerdieping() + "." + melding.getLokaal() + " werd gerepareerd.", "Goed nieuws!");
        Sender sender = new Sender(melding.getToken(), notification);
        getFCMClient().sendNotification(sender)
                .enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                        if (response.body().getSuccess() == 1) {
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationResponse> call, Throwable t) {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

        melding.setGerepareerd(true);
        ref.child(getKeyOfMelding(melding)).setValue(melding);
    }

    /**
     * Delete melding
     */
    public void deleteMelding(Melding melding, Context context) {
        db.getDbReference().child(context.getString(R.string.key_meldingen)).child(melding.getId()).removeValue();
    }

    /**
     * Get the key of a Melding
     */
    private String getKeyOfMelding(Melding melding) {
        return melding.getId();
    }

    /**
     * Uploads photo to Storage
     */
    private void uploadFotoToFirebase(ImageView image, String name, Context context) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        image.setDrawingCacheEnabled(true);
        image.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.child(context.getString(R.string.path_images) + name).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("upload", "Failed to upload image");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("upload", "Success upload");
            }
        });
    }
}