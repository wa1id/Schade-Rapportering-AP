package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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
    private StatsController sc = new StatsController();

    private static ApiService getFCMClient() {
        String baseUrl = "https://fcm.googleapis.com/";
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
     * Get preview (3 most recent) Meldingen
     */
    public void getPreviewMelding(final Context context, final IMeldingCallback callback) {
        db.getDbReference().child(context.getString(R.string.key_meldingen)).orderByKey().limitToLast(3).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Melding melding = dataSnapshot.getValue(Melding.class);
                callback.onMeldingCallback(melding);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //unused
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //unused
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //unused
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
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

        //variables used for editting existing melding
        melding.setReparatieDatum("");
        melding.setExtraNotities("");
        melding.setReparatieUitvoerder("");
        melding.setNaamReparatieUitvoerder("");


        ref.setValue(melding);
        uploadFotoToFirebase(image, ref.getKey(), context);

        //update stats
        sc.addOne(db.getDbReference().child(context.getString(R.string.key_stats)).child(context.getString(R.string.key_melding_total)));
        sc.addOne(db.getDbReference().child(context.getString(R.string.key_stats)).child(context.getString(R.string.key_melding_current)));
    }

    public void editMelding(final DatabaseReference ref, Context context, final String date, final String uitvoerder, final String naamUitvoerder, final String extraNotes) {
        ref.child(context.getString(R.string.key_reparatie_datum)).setValue(date);
        ref.child(context.getString(R.string.key_reparatie_uitvoerder)).setValue(uitvoerder);
        ref.child(context.getString(R.string.key_naam_reparatie_uitvoerder)).setValue(naamUitvoerder);
        ref.child(context.getString(R.string.key_extra_notities)).setValue(extraNotes);
    }


    /**
     * Move Melding to Archive and delete the Melding
     */
    public void archiveerMelding(Melding melding, final Context context) {
        DatabaseReference ref = db.getDbReference().child(context.getString(R.string.key_archives));

        //START of notification
        NotificationFCM notification = new NotificationFCM("Uw melding op " + melding.getDatum() + " van lokaal " + melding.getVerdieping() + "." + melding.getLokaal() + " werd gerepareerd.", "Goed nieuws!");
        Sender sender = new Sender(melding.getToken(), notification);
        getFCMClient().sendNotification(sender)
                .enqueue(new Callback<NotificationResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationResponse> call, @NonNull Response<NotificationResponse> response) {
                        assert response.body() != null;
                        if (response.body().getSuccess() == 1) {
                            Log.d("push", "Success");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationResponse> call, @NonNull Throwable t) {
                        Log.d("push", "Failed:" + t.getMessage());
                    }
                });
        //END of notification

        melding.setGerepareerd(true);
        ref.child(getKeyOfMelding(melding)).setValue(melding);

        //update stats
        sc.addOne(db.getDbReference().child(context.getString(R.string.key_stats)).child(context.getString(R.string.key_archief_total)));
    }

    /**
     * Delete melding
     */
    public void deleteMelding(Melding melding, Context context) {
        db.getDbReference().child(context.getString(R.string.key_meldingen)).child(melding.getId()).removeValue();

        //update stats
        sc.deleteOne(db.getDbReference().child(context.getString(R.string.key_stats)).child(context.getString(R.string.key_melding_current)));
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