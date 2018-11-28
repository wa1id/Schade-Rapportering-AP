package ap.edu.schademeldingap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

public class NieuweMeldingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_melding);

        Spinner spinnerCategorie = findViewById(R.id.spinnerCategorie);
        final Spinner spinnerVerdieping = findViewById(R.id.spinnerVerdieping);
        final Spinner spinnerLokaal = findViewById(R.id.spinnerLokaal);

        ArrayAdapter<CharSequence> adapterCategorie = ArrayAdapter.createFromResource(this, R.array.categorien, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterVerdieping = ArrayAdapter.createFromResource(this,R.array.verdieping, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiepMin1 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiepMin1, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiepGelijkvloer = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiepGelijkVloer, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep1 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep1, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep2 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep2, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep3 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep3, android.R.layout.simple_spinner_item);
        final ArrayAdapter<CharSequence> adapterLokaalVerdiep4 = ArrayAdapter.createFromResource(this,R.array.lokaalVerdiep4, android.R.layout.simple_spinner_item);


        adapterCategorie.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterVerdieping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepMin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiepGelijkvloer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterLokaalVerdiep4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinnerCategorie.setAdapter(adapterCategorie);
        spinnerVerdieping.setAdapter(adapterVerdieping);

        spinnerVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("-1"))
                {
                   spinnerLokaal.setAdapter(adapterLokaalVerdiepMin1);
                }else if(selectedItem.equals("0")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiepGelijkvloer);
                }else if(selectedItem.equals("1")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep1);
                }else if(selectedItem.equals("2")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep2);
                }else if(selectedItem.equals("3")){
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep3);
                }else{
                    spinnerLokaal.setAdapter(adapterLokaalVerdiep4);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






     /*  if(spinnerVerdieping.getSelectedItem().toString() == "-1"){
           spinnerLokaal.setAdapter(adapterLokaalVerdiepMin1);
       }else if (spinnerVerdieping.getSelectedItem().toString() == "0"){
           spinnerLokaal.setAdapter(adapterLokaalVerdiepGelijkvloer);
       }else if (spinnerVerdieping.getSelectedItem().toString() == "1"){
           spinnerLokaal.setAdapter(adapterLokaalVerdiep1);
       }else if (spinnerVerdieping.getSelectedItem().toString() == "2"){
           spinnerLokaal.setAdapter(adapterLokaalVerdiep2);
       }else if (spinnerVerdieping.getSelectedItem().toString() == "3"){
           spinnerLokaal.setAdapter(adapterLokaalVerdiep3);
       }else{
           spinnerLokaal.setAdapter(adapterLokaalVerdiep4);
       }*/

      // switch (spinnerVerdieping.getSelectedItem().toString()){
        //   case "-1":

       //}


    }
}
