package id.kunya.informatikavote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.kunya.informatikavote.adapter.CandidateAdapter;
import id.kunya.informatikavote.inf.RequstInterface;
import id.kunya.informatikavote.model.Candidates;
import id.kunya.informatikavote.model.Data;
import id.kunya.informatikavote.model.Posts;
import id.kunya.informatikavote.utils.PreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private RecyclerView rv_candidate;
    private RequstInterface request;
    private Retrofit retrofit;
    private ProgressDialog progress;
    private TextView candidate;
    private String getStatus, getVoted;
    SharedPreferences prf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        request = retrofit.create(RequstInterface.class);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Voting...");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initViews();
    }

    private void initViews(){
        candidate = findViewById(R.id.candidate);
        rv_candidate = findViewById(R.id.rv_candidate);

        getStatus=PreferencesHelper.getInstance(getApplicationContext()).getVotingStatus();
        getVoted=PreferencesHelper.getInstance(getApplicationContext()).getVoted();

        rv_candidate.setHasFixedSize(true);
        rv_candidate.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_candidate.setLayoutManager(layoutManager);
        loadCandidate();
    }

    private void loadCandidate() {

        Call<Data> call = request.getCandidate();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                ArrayList<Candidates> jsonResponse = response.body().getData();
                Log.d("Repon",response.body().toString());
                CandidateAdapter adapter = new CandidateAdapter(getStatus, getVoted, jsonResponse, getApplicationContext(), candidate, rv_candidate, progress);
                rv_candidate.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(MainActivity.this, "Gagal "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_beranda) {

        }
        else if (id == R.id.nav_keluar) {
            SharedPreferences sp=getSharedPreferences("SHARED_PREF_NAME",MODE_PRIVATE);
            SharedPreferences.Editor e=sp.edit();
            e.clear();
            e.commit();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

