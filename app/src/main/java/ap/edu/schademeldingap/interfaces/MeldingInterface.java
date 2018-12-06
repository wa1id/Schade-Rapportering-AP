package ap.edu.schademeldingap.interfaces;

import android.content.Context;
import android.widget.ImageView;

import ap.edu.schademeldingap.models.Melding;

public interface MeldingInterface {

    void nieuweMelding(Melding melding, Context context);
    void uploadFotoToFirebase(ImageView image, String name, Context c);
}