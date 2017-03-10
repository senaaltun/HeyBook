package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
<<<<<<< HEAD
=======
import android.util.Log;
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
<<<<<<< HEAD

import heybook.team1.com.heybookv2.R;
=======
import android.widget.Toast;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490

public class Register extends BaseActivity {
    private EditText userTitle;
    private EditText userEmail;
<<<<<<< HEAD
    private EditText password;
=======
    private EditText passwordField;
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
<<<<<<< HEAD
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userTitle = (EditText)findViewById(R.id.userName);
        userEmail = (EditText)findViewById(R.id.mail);
        password = (EditText)findViewById(R.id.passwordRegister);
=======


        userTitle = (EditText)findViewById(R.id.regUserName);
        userEmail = (EditText)findViewById(R.id.mail);
        passwordField = (EditText)findViewById(R.id.passwordRegister);
>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
        registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
<<<<<<< HEAD
                String userName = userTitle.getText().toString();
                String uMail = userEmail.getText().toString();
                String pwd = password.getText().toString();


            }
        });

    }

}
=======

                String user_title = userTitle.getText().toString();
                String mail = userEmail.getText().toString();
                String password = passwordField.getText().toString();

                final RegisterModel registerUser = new RegisterModel(user_title,mail,password);

                ApiClientInterface apiService =
                        ApiClient.getClient().create(ApiClientInterface.class);

                Call<String> call = apiService.setUser(user_title,mail,password);


                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();


    }
}



>>>>>>> 4ebd18eb3a4b0fc156fc192f9a36e3fcfd0b7490
