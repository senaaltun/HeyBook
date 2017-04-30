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
  private ArrayList<Data> userChartList = new ArrayList<>();
  private boolean isDatasetChanged = false;
  private ArrayList<Data> removedItems = new ArrayList<>();
  private ArrayList<String> removedBookIds = new ArrayList<>();

  public boolean isUserPhotoAdd() {
    return isUserPhotoAdd;
  }

  public void setUserPhotoAdd(boolean userPhotoAdd) {
    isUserPhotoAdd = userPhotoAdd;
  }

  private boolean isUserPhotoAdd = false;

  public ArrayList<String> getRemovedBookIds() {
    return removedBookIds;
  }

  public void setRemovedBookIds(ArrayList<String> removedBookIds) {
    this.removedBookIds = removedBookIds;
  }

  public ArrayList<Data> getRemovedItems() {
    return removedItems;
  }

  public void setRemovedItems(ArrayList<Data> removedItems) {
    this.removedItems = removedItems;
  }

  public boolean isDatasetChanged() {
    return isDatasetChanged;
  }

  public void setDatasetChanged(boolean datasetChanged) {
    isDatasetChanged = datasetChanged;
  }

  private boolean isUserLogin;

  public ArrayList<Data> getUserChartList() {
    return userChartList;
  }

  public void setUserChartList(ArrayList<Data> userChartList) {
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
