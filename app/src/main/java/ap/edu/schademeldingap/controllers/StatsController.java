package ap.edu.schademeldingap.controllers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.data.Database;
import ap.edu.schademeldingap.interfaces.IStatsCallback;
import ap.edu.schademeldingap.models.Stats;

public class StatsController {

    void addOne(final DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                if (value != null) {
                    ref.setValue(value + 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        });
    }

    void deleteOne(final DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer value = dataSnapshot.getValue(Integer.class);
                if (value != null) {
                    ref.setValue(value - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        });
    }

    public void getStats(Context context, final IStatsCallback callback) {
        Database db = new Database();
        db.getDbReference().child(context.getString(R.string.key_stats)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Stats stats = dataSnapshot.getValue(Stats.class);
                callback.onStatsCallback(stats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        });
    }
}
