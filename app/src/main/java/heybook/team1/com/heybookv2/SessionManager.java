package heybook.team1.com.heybookv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import heybook.team1.com.heybookv2.Activity.Login;
import java.util.HashMap;

/**
 * Created by senaaltun on 12/03/2017.
 */

public class SessionManager {
  SharedPreferences sharedPreferences;

  SharedPreferences.Editor editor;

  Context context;

  int PRIVATE_MODE = 0;

  private static final String IS_LOGIN = "IsLoggedIn";
  private static final String PREF_NAME = "HeybookPrefs";
  public static final String KEY_NAME = "name";
  public static final String KEY_PASSWORD = "password";
  public static final String KEY_IMAGE_PATH = "imagePath";

  private static final String IS_LOGIN_PROFILE = "IsProfile";


  public SessionManager(Context context){
    this.context = context;
    sharedPreferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
    editor = sharedPreferences.edit();
  }

  public void createLoginSession(String name,String password){
    editor.putBoolean(IS_LOGIN,true);
    editor.putString(KEY_NAME,name);
    editor.putString(KEY_PASSWORD,password);

    editor.commit();
  }

  public HashMap<String,String> getUserDetails(){
    HashMap<String,String> user = new HashMap<>();

    user.put(KEY_NAME,sharedPreferences.getString(KEY_NAME,null));
    user.put(KEY_PASSWORD,sharedPreferences.getString(KEY_PASSWORD,null));

    return user;
  }

  public void checkLogin(){
    if(!this.isLoggedIn()){
      Intent intent = new Intent( context , Login.class);

      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      context.startActivity(intent);
    }
  }

  public boolean isLoggedIn(){
    return sharedPreferences.getBoolean(IS_LOGIN, false);
  }


  public void logoutUser(){
    editor.clear();
    editor.commit();

    Intent intent = new Intent(context,Login.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    context.startActivity(intent);
  }

  public void createUserProfileSession(String imagePath){
    editor.putBoolean(IS_LOGIN_PROFILE,true);
    editor.putString(KEY_IMAGE_PATH,imagePath);
  }

  public boolean isLoggedProfile(){
    return sharedPreferences.getBoolean(IS_LOGIN_PROFILE,false);
  }
  





}


