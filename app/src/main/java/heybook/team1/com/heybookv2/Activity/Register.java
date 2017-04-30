package heybook.team1.com.heybookv2.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.RegisterModel;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends BaseActivity {
    private EditText userTitle;
    private EditText userEmail;
    private EditText passwordField;

    private Button registerButton;

    private SessionManager sessionManager;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sessionManager = new SessionManager(Register.this);

        userTitle = (EditText) findViewById(R.id.regUserName);
        userEmail = (EditText) findViewById(R.id.mail);
        passwordField = (EditText) findViewById(R.id.passwordRegister);
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_title = userTitle.getText().toString();
                String mail = userEmail.getText().toString();
                String password = passwordField.getText().toString();

                if(!mail.contains("@") || !mail.endsWith("com")){
                    Toast.makeText(Register.this,
                            "Uygun formatta bir mail adresi girmediniz.",Toast.LENGTH_SHORT).show();
                }else{

                    try {
                        registerUser(user_title,mail,password);
                        sessionManager.userSettings(user_title,password,mail,userId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();

    }

    private void registerUser(String userTitle, String mail, String password) throws IOException, JSONException {
        URL url = new URL("https://heybook.online/api.php");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("request", "request");
        params.put("requestValue", "register");
        params.put("user_title", userTitle);
        params.put("mail", mail);
        params.put("password", password);

        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(params));
        writer.flush();
        writer.close();
        os.close();
        connection.connect();

        int responseCode = connection.getResponseCode();

        String book;

        StringBuilder result = new StringBuilder();

        if(responseCode == HttpsURLConnection.HTTP_OK){
            BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            while((book = br.readLine()) != null){
                result.append(book).append("\n");
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                JSONObject jsonData = new JSONObject(result.toString());
                Log.d("jsonData",jsonData.toString());
                JSONObject jsonArray = jsonData.getJSONObject("data");
                Log.d("jsonData",jsonData.toString());
                if(jsonData.getString("response").equals("success")){
                    userId = jsonArray.getString("user_id");
                    Log.d("userID",userId);
                    Toast.makeText(Register.this,"Başarıyla kayıt oldunuz!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this,Vitrin.class));
                }else{
                    Toast.makeText(Register.this,jsonData.getString("message"),Toast.LENGTH_SHORT).show();
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
        result.append("user_title=" + params.get("user_title"));
        result.append("&");
        result.append("mail=" + params.get("mail"));
        result.append("&");
        result.append("password="+params.get("password"));

        return result.toString();
    }
}



