package heybook.team1.com.heybookv2.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.User;
import heybook.team1.com.heybookv2.Model.UserData;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private Button loginButton;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.loginButton);
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);

        getUser();



    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void getUser(){
        ApiClientInterface apiService =
                ApiClient.getClient().create(ApiClientInterface.class);

        Call<User> call = apiService.getUser();

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                final User user = response.body();
                final List<UserData> userData = user.getData();

                loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = userName.getText().toString();
                        String pw = password.getText().toString();
                        for(int i=0;i<userData.size();i++){
<<<<<<< HEAD
                            if(userData.get(i).getUser_title().equals(username) && userData.get(i).getPassword().equals(pw)){
=======
                            if(userData.get(i).getUser_title().equals(username)
                                    && userData.get(i).getPassword().equals(pw)){
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
                                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                                int num = sharedPreferences.getInt("isLogged",0);
                                if(num == 0){
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("isLogged",1);
                                    editor.commit();
                                }else{
                                    Intent pos = getIntent();
                                    int p = pos.getIntExtra("bookPos",0);
                                    Intent intent = new Intent(Login.this,Listen.class);
                                    intent.putExtra("BookPosition",p);
                                    startActivity(intent);
                                }

<<<<<<< HEAD
                            }else{
                                Toast.makeText(getApplicationContext(),
                                        "Girdiğiniz bilgilere ait kullanıcı bulunamadı.",
                                        Toast.LENGTH_SHORT).show();
                            }



                        }

                        if(userData.get(0).getUser_title().equals(username) && userData.get(0).getPassword().equals(pw)){
                            Log.d("Login","Success");

=======
                            }

>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
                        }

            }


        });

    }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}

