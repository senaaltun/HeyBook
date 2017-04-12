package heybook.team1.com.heybookv2.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import heybook.team1.com.heybookv2.Model.Login;
import heybook.team1.com.heybookv2.Model.LoginData;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private EditText userName;
    private EditText password;

    private SessionManager sessionManager;

    private String username = null;
    private String pw = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sessionManager = new SessionManager(this);

        username = userName.getText().toString();
        pw = password.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser(username,pw);
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void getUser(String mail, String password) {
        final ApiClientInterface apiService = ApiClient.getClient().create(ApiClientInterface.class);

        Call<LoginData> call = apiService.login(mail,password);

        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                LoginData loginData = response.body();
                //Login login = loginData.get();


                Log.d("LoginData",loginData.getMessage());




            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                Log.d("onFail",t.toString());
            }
        });
    }
}

