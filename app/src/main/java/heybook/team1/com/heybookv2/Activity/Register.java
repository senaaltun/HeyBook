package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends BaseActivity {
    private AutoCompleteTextView userTitle;
    private AutoCompleteTextView userEmail;
    private EditText passwordField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        userTitle = (AutoCompleteTextView)findViewById(R.id.regUserName);
        userEmail = (AutoCompleteTextView)findViewById(R.id.mail);
        passwordField = (EditText)findViewById(R.id.passwordRegister);
        registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_title = userTitle.getText().toString();
                String mail = userEmail.getText().toString();
                String password = passwordField.getText().toString();

                Log.d("UserTitle",user_title);
                Log.d("mail",mail);
                Log.d("password",password);

                final RegisterModel registerUser = new RegisterModel(user_title,mail,password);


                ApiClientInterface apiService =
                        ApiClient.getClient().create(ApiClientInterface.class);

                Call<RegisterModel> call = apiService.setUser(user_title,mail,password);


                call.enqueue(new Callback<RegisterModel>() {
                    @Override
                    public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                        Toast.makeText(Register.this,"Başarıyla kayıt olundu",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<RegisterModel> call, Throwable t) {

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



