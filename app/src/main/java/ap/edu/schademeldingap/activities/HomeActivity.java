package ap.edu.schademeldingap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.PriorityQueue;

import ap.edu.schademeldingap.R;
import ap.edu.schademeldingap.controllers.MeldingController;
import ap.edu.schademeldingap.controllers.StatsController;
import ap.edu.schademeldingap.controllers.UserController;
import ap.edu.schademeldingap.interfaces.IMeldingCallback;
import ap.edu.schademeldingap.interfaces.IStatsCallback;
import ap.edu.schademeldingap.interfaces.IUserCallback;
import ap.edu.schademeldingap.models.Melding;
import ap.edu.schademeldingap.models.Stats;
import ap.edu.schademeldingap.models.User;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button mButtonSignOut;
    private Button mButtonSchadeMelden;
    private Button mButtonSchadeZoeken;
    private TextView mTextWelkom;
    private TextView mTextMeldingenCount;

    private ListView mListLastMeldingen;
    private ArrayList<String> mMeldingen;
    private ArrayAdapter<String> mAdapterMeldingen;
    private ArrayList<String> mMeldingIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("HOME");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mButtonSchadeMelden = findViewById(R.id.buttonSchadeMelden);
        mButtonSchadeZoeken = findViewById(R.id.buttonSchadezoeken);
        mTextWelkom = findViewById(R.id.textWelkom);
        mTextMeldingenCount = findViewById(R.id.textCountMeldingen);
        mListLastMeldingen = findViewById(R.id.listLastMeldingen);

        mMeldingen = new ArrayList<>();
        mMeldingIds = new ArrayList<>();
        mAdapterMeldingen = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMeldingen);

        mListLastMeldingen.setAdapter(mAdapterMeldingen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //makes sure the listview doesnt move when keyboard pops up.

        setupHome();

        mButtonSchadeMelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeToMelden = new Intent(HomeActivity.this, NieuweMeldingActivity.class);
                homeToMelden.putExtra(getString(R.string.key_naam), mTextWelkom.getText().toString().substring(7));
                startActivity(homeToMelden);
            }
        });

        mButtonSchadeZoeken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeToSearchIntent(getString(R.string.key_meldingen));
            }
        });

        mListLastMeldingen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(HomeActivity.this, DetailActivity.class);
                detailIntent.putExtra("id", mMeldingIds.get(i));
                detailIntent.putExtra("detail", getString(R.string.key_meldingen));
                startActivity(detailIntent);
            }
        });
    }

    /**
     * Method to make a new intent for either Archive or Melding
     * @param key database key we want to use in DetailActivity (meldingen/archives)
     */
    private void homeToSearchIntent(String key) {
        Intent homeToSearch = new Intent(HomeActivity.this, HuidigeSchadesActivity.class);
        homeToSearch.putExtra("detail", key);
        startActivity(homeToSearch);
    }

    /**
     * setup the homepage for the current user. Get the name and check if reparateur
     */
    private void setupHome() {
        StatsController sc = new StatsController();

        getPreviewMeldingen();

        //Setting up stats
        sc.getStats(HomeActivity.this, new IStatsCallback() {
            @Override
            public void onStatsCallback(Stats stats) {
                mTextMeldingenCount.append(" " + String.valueOf(stats.getMeldingCurrent()));

                showInterface(); //after everything is loaded, show interface and hide progressbar
            }
        });
    }

    /**
     * Get last 3 meldingen and put them in listview
     */
    private void getPreviewMeldingen(){
        MeldingController mc = new MeldingController();

        mc.getPreviewMelding(HomeActivity.this, new IMeldingCallback() {
            @Override
            public void onMeldingCallback(Melding melding) {
                mMeldingIds.add(melding.getId());
                mMeldingen.add(melding.getVerdieping() + "." + melding.getLokaal()
                        + "   ---   "
                        + melding.getCategorie());
                mAdapterMeldingen.notifyDataSetChanged();
            }
        });
    }

    /**
     * Show buttons/textviews & hide progressbar after setupHome()
     */
    private void showInterface() {
        ProgressBar progressLoading = findViewById(R.id.progressLoading);
        TextView textLastMeldingen = findViewById(R.id.textLastMeldingen);

        progressLoading.setVisibility(View.GONE);
        mButtonSchadeMelden.setVisibility(View.VISIBLE);
        mButtonSchadeZoeken.setVisibility(View.VISIBLE);
        mTextWelkom.setVisibility(View.VISIBLE);
        mTextMeldingenCount.setVisibility(View.VISIBLE);
        mListLastMeldingen.setVisibility(View.VISIBLE);
        textLastMeldingen.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);

        UserController uc = new UserController();

        //Setting up user data
        uc.getUserData(HomeActivity.this, new IUserCallback() {
            @Override
            public void onUserCallback(User user) {
                mTextWelkom.append(" " + user.getName());

                if (!user.getReparateur()) {
                    menu.getItem(0).setVisible(false);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuLogout:
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.menuArchief:
                homeToSearchIntent(getString(R.string.key_archives));
                break;
        }
        return true;
    }
}