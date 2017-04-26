package heybook.team1.com.heybookv2.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import de.hdodenhof.circleimageview.CircleImageView;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;
import java.util.ArrayList;
import java.util.HashMap;

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

  private HashMap<String,String> userInfo;

  private static final int REQUEST_CODE_PICKER = 100;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    userProfileImage = (CircleImageView)findViewById(R.id.userProfileImage);
    addPhoto = (ImageView)findViewById(R.id.addPhotoButton);
    userTitle =(EditText)findViewById(R.id.user_title_settings);
    userEmail = (EditText)findViewById(R.id.user_email_settings);
    userPassword = (EditText)findViewById(R.id.password_settings);
    saveSettingsButton = (Button)findViewById(R.id.saveSettings);

    sessionManager = new SessionManager(getApplicationContext());
    sharedPreferences = getSharedPreferences("HeybookPrefs",0);

    if(sessionManager.isLoggedIn()){
      //userProfileImage.setImageURI(Uri.parse(sharedPreferences.getString("imagePath",null)));
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
        sessionManager.createLoginSession(userTitle.getText().toString(),userPassword.getText().toString());

      }
    });



  }

  @Override
  public void onActivityResult(int requestCode, final int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
      images = (ArrayList<Image>)ImagePicker.getImages(data);
      userProfileImage.setImageURI(Uri.parse(images.get(0).getPath()));
      sessionManager.createUserProfileSession(images.get(0).getPath());
      return;
    }
  }

}
