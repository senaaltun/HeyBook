package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.User;
import heybook.team1.com.heybookv2.Model.UserData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
  private Button loginButton;
  private EditText userName;
  private EditText password;

  private SessionManager sessionManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    loginButton = (Button) findViewById(R.id.loginButton);
    userName = (EditText) findViewById(R.id.userName);
    password = (EditText) findViewById(R.id.password);

    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    sessionManager = new SessionManager(this);

    getUser();
  }

  @Override protected void onStop() {
    super.onStop();
  }

  public void getUser() {
    ApiClientInterface apiService = ApiClient.getClient().create(ApiClientInterface.class);

    Call<User> call = apiService.getUser();

    call.enqueue(new Callback<User>() {
      @Override public void onResponse(Call<User> call, Response<User> response) {
        final User user = response.body();
        final List<UserData> userData = user.getData();

        loginButton.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View view) {
            String username = userName.getText().toString();
            String pw = password.getText().toString();
            for (int i = 0; i < userData.size(); i++) {
              if (username.trim().length() > 0 && pw.trim().length() > 0) {
                if (userData.get(i).getUser_title().equals(username) && userData.get(i)
                    .getPassword()
                    .equals(pw)) {

                  sessionManager.createLoginSession(username, pw);

                  startActivity(new Intent(getApplicationContext(), Vitrin.class));
                  finish();
                }
              } else {
                Toast.makeText(getApplicationContext(), "Kullanıcı adı ve şifre boş olamaz.",
                    Toast.LENGTH_SHORT).show();
              }
              Toast.makeText(getApplicationContext(),
                        "Girdiğiniz bilgilere ait kullanıcı bulunamadı.Bilgileri tekrar kontrol ediniz.",
                        Toast.LENGTH_SHORT).show();

            }
          }
        });
      }

      @Override public void onFailure(Call<User> call, Throwable t) {

      }
    });
  }
}

