package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sessionManager = new SessionManager(this);

        username = userName.getText().toString();
        pw = password.getText().toString();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username = userName.getText().toString();
                    pw = password.getText().toString();
                    getUser(username,pw);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void getUser(String mail, String password) throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php?");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "login");
        params.put("username", mail);
        params.put("password", password);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        StringBuilder result = new StringBuilder();

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
            String userInfo;
            while ((userInfo = br.readLine()) != null) {
                result.append(userInfo).append("\n");
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                final String userDataMessage = jsonData.getString("message");
                final JSONObject userData = jsonData.getJSONObject("data");
                final String userId = userData.getString("user_id");

                if(userDataMessage.equals("Login successed.")){
                    sessionManager.createLoginSession(username,pw,userId);
                    startActivity(new Intent(LoginActivity.this,Vitrin.class));
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Girdiğiniz bilgilere ait kullanıcı bulunamadı. Lütfen bilgileri deneyip tekrar kontrol ediniz.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
            result.append("&");
            result.append("mail=");
            result.append(params.get("username"));
            result.append("&");
            result.append("password=");
            result.append(params.get("password"));

        Log.d("Result",result.toString());

        return result.toString();
    }
}

