package ap.edu.schademeldingap.controllers;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.models.Archive;
import ap.edu.schademeldingap.models.Melding;

public class ArchiveController {
    public void newArchive(Archive a, Context c) { //need context to use getString()
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child(c.getString(R.string.key_archives)).push();

        ref.child(c.getString(R.string.key_user)).setValue(a.getUser());
        ref.child(c.getString(R.string.key_lokaal)).setValue(a.getLokaal());
        ref.child(c.getString(R.string.key_lokaal_vrije_invoer)).setValue(a.getVrijeInvoerLokaal());
        ref.child(c.getString(R.string.key_campus)).setValue(a.getCampus());
        ref.child(c.getString(R.string.key_categorie)).setValue(a.getCategorie());
        ref.child(c.getString(R.string.key_beschrijving_schade)).setValue(a.getBeschrijvingSchade());
        ref.child(c.getString(R.string.key_datum)).setValue(a.getModifiedDate());
        ref.child(c.getString(R.string.key_gerepareerd)).setValue(a.isGerepareerd());
    }
}
