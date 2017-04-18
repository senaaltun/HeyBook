package heybook.team1.com.heybookv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import heybook.team1.com.heybookv2.Activity.LoginActivity;
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
  public static final String KEY_ID = "userId";

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
  public void createLoginSession(String name,String password,String userID){
    editor.putBoolean(IS_LOGIN,true);
    editor.putString(KEY_NAME,name);
    editor.putString(KEY_PASSWORD,password);
    editor.putString(KEY_ID,userID);

    editor.commit();
  }

  public HashMap<String,String> getUserDetails(){
    HashMap<String,String> user = new HashMap<>();

    user.put(KEY_ID,sharedPreferences.getString(KEY_ID,null));
    user.put(KEY_PASSWORD,sharedPreferences.getString(KEY_PASSWORD,null));

    return user;
  }

  public void checkLogin(){
    if(!this.isLoggedIn()){
      Intent intent = new Intent( context , LoginActivity.class);

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

    Intent intent = new Intent(context,LoginActivity.class);
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
  
  public void saveFavorite(){
    editor.putBoolean("isChecked",true);
    editor.commit();
  }

  public boolean isFavorite(){
    return sharedPreferences.getBoolean("isChecked",false);
  }




}


