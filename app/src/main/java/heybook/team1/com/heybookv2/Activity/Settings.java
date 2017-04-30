package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.model.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ImagePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Settings extends BaseActivity {
  private CircleImageView userProfileImage;

  private ArrayList<Image> images = new ArrayList<>();

  private Button saveSettingsButton;

  private EditText userTitle;
  private EditText userEmail;
  private EditText userPassword;

  private ImageView addPhoto;
  private SessionManager sessionManager;

  private SharedPreferences sharedPreferences;

  private String userId;

  private HashMap<String,String> userInfo;

  private static final int REQUEST_CODE_PICKER = 100;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    userProfileImage = (CircleImageView)findViewById(R.id.userProfileImage);
    addPhoto = (ImageView)findViewById(R.id.addPhotoButton);
    userTitle =(EditText)findViewById(R.id.user_title_settings);
    userEmail = (EditText)findViewById(R.id.user_email_settings);
    userPassword = (EditText)findViewById(R.id.password_mevcut);
    saveSettingsButton = (Button)findViewById(R.id.saveSettings);

    sessionManager = new SessionManager(getApplicationContext());
    sharedPreferences = getSharedPreferences("HeybookPrefs",0);

    if(((HeyBook)getApplicationContext()).isUserPhotoAdd()){
      userProfileImage.setImageURI(Uri.parse(sharedPreferences.getString("imagePath",null)));
    }

    if(sessionManager.isLoggedIn() ){

      userTitle.setText(sharedPreferences.getString("name",null));
      userEmail.setText(sharedPreferences.getString("mail",null));
      userPassword.setText(sharedPreferences.getString("password",null));
      userPassword.setKeyListener(null);
      userPassword.setEnabled(false);
    }

    addPhoto.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        ImagePicker.create(Settings.this)
            .limit(1)
            .start(REQUEST_CODE_PICKER);
      }
    });

    saveSettingsButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sessionManager.userSettings(userTitle.getText().toString(),userPassword.getText().toString(),userEmail.getText().toString(),userId);

      }
    });



  }

  @Override
  public void onActivityResult(int requestCode, final int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
      images = (ArrayList<Image>)ImagePicker.getImages(data);
      userProfileImage.setImageURI(Uri.parse(images.get(0).getPath()));
      sessionManager.createUserProfileSession(images.get(0).getPath());
      ((HeyBook)getApplicationContext()).setUserPhotoAdd(true);
      return;
    }
  }

  private void getUserSettings() throws IOException, JSONException {
    URL url = new URL("https://heybook.online/api.php");
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    connection.setReadTimeout(10000);
    connection.setConnectTimeout(15000);
    connection.setRequestMethod("POST");
    connection.setDoInput(true);
    connection.setDoOutput(true);

    HashMap<String, String> user = sessionManager.getUserDetails();
    userId = user.get("userId");
    Log.d("userIdFa", userId);

    HashMap<String, String> params = new HashMap<>();
    params.put("request", "request");
    params.put("requestValue", "settings");

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

    if (responseCode == HttpsURLConnection.HTTP_OK) {
      BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

      while ((book = br.readLine()) != null) {
        result.append(book).append("\n");
      }

      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
        JSONObject jsonData = new JSONObject(result.toString());
        Log.d("sepet", jsonData.toString());
      }
    }
  }

  private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;
    if (first) {
      first = false;
    } else {
      result.append("&");
    }
    result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
    result.append("=");
    result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
    result.append("&");
    result.append("user_id=");
    result.append(userId);
    result.append("&");
    result.append("user_title=");
    Log.d("result", result.toString());


    return result.toString();
  }

}
