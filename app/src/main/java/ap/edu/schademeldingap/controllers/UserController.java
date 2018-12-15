package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.interfaces.MyCallback;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.models.User;

public class UserController {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Get current user
     */
    private FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    /**
     * Get Current User data
     */
    public void getUserData(final MyCallback myCallback, final Context context) {
        Database db = new Database();

        db.getDbReference().child("users").child(getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                myCallback.onUserCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, context.getString(R.string.data_ophalen_mislukt), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
