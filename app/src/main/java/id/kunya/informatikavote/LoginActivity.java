package id.kunya.informatikavote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.kunya.informatikavote.inf.RequstInterface;
import id.kunya.informatikavote.model.Posts;
import id.kunya.informatikavote.model.User;
import id.kunya.informatikavote.utils.PreferencesHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RequstInterface request;
    private EditText et_username, et_password;
    private TextView tv_massages;
    private Button btn_login;
    private Button btn_exit;
    private ProgressDialog progress;
    private Retrofit retrofit;
    private String TAG="LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finish();
                System.exit(0);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(URLs.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        request = retrofit.create(RequstInterface.class);

        progress = new ProgressDialog(this);
        progress.setCancelable(false);

        String nim=PreferencesHelper.getInstance(getApplicationContext()).getLoginID();

        if(nim!=null){
            checkVote(nim);
        }

        initViews();
    }


    private void initViews(){
        et_username = findViewById(R.id.text_username);
        et_password = findViewById(R.id.text_password);
        tv_massages = findViewById(R.id.massages);
        btn_login = findViewById(R.id.btn_signin);
        btn_exit = findViewById(R.id.btn_exit);

        btn_login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.btn_signin){
            signIn();
        }
    }

    private void signIn(){
        Call<User> api = request.signIn(et_username.getText().toString(), et_password.getText().toString());
        progress.setMessage("Mohon Tunggu...");
        progress.show();

        if(et_username.getText().toString().equals("") && et_password.getText().toString().equals("")){
            tv_massages.setText("Data Tidak Boleh Kosong!");
            progress.dismiss();
        }else {
            api.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user=response.body();
                    if (response.body().getSuccess().equals("1")) {
//                        finish();
                        PreferencesHelper.getInstance(getApplicationContext()).saveLoginID(""+user.getNim());
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        checkVote(""+user.getNim());
//                        tv_massages.setText(""+user.getMessage());
                        Log.d(TAG, "Success" + user.getMessage());
                    } else {
                        tv_massages.setText(""+user.getMessage());
                        Log.d(TAG, "Failed" + user.getMessage());
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                    progress.dismiss();
                }
            });
        }
    }

    private void checkVote(String nim){
        Call<User> api = request.checkVote(nim);
        progress.setMessage("Mengambil Data...");
        progress.show();

            api.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user=response.body();

                    finish();
                    PreferencesHelper.getInstance(getApplicationContext()).saveVotingStatus(""+user.getStatus());
                    PreferencesHelper.getInstance(getApplicationContext()).saveVoted(""+user.getIdkandidat());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    Log.d(TAG, "Success" + user.getMessage());

                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                    progress.dismiss();
                }
            });

    }

}

