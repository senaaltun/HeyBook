package heybook.team1.com.heybookv2;

import android.app.Application;

import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.Favorite;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SenaAltun on 18/04/2017.
 */

public class HeyBook extends Application{
  private String userId;
  private List<Favorite> userFavList = new ArrayList();
  private List<Data> userChartList = new ArrayList<>();

  private boolean isUserLogin;

  public List<Data> getUserChartList() {
    return userChartList;
  }

  public void setUserChartList(List<Data> userChartList) {
    this.userChartList = userChartList;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<Favorite> getUserFavList() {
    return userFavList;
  }

  public void setUserFavList(List<Favorite> userFavList) {
    this.userFavList = userFavList;
  }

  public boolean isUserLogin() {
    return isUserLogin;
  }

  public void setUserLogin(boolean userLogin) {
    isUserLogin = userLogin;
  }
}
