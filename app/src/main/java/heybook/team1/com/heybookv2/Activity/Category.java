package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Adapter.CategoryAdapter;
import heybook.team1.com.heybookv2.Adapter.SepetAdapter;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Category extends BaseActivity {
  private RecyclerView maceraRecyclerView;
  private RecyclerView scienceRecyclerView;
  private RecyclerView classicsRecyclerView;
  private RecyclerView historyRecyclerView;
  private RecyclerView fearRecyclerView;
  private RecyclerView personalDevelopmentRecyclerView;
  private RecyclerView novelRecyclerView;
  private RecyclerView fableRecyclerView;

  private CategoryAdapter categoryAdapter;

  private ArrayList<Data> adventureData = new ArrayList<>();
  private ArrayList<Data> scienceData = new ArrayList<>();
  private ArrayList<Data> historyData = new ArrayList<>();
  private ArrayList<Data> fearData = new ArrayList<>();
  private ArrayList<Data> personalDevelopmentData = new ArrayList<>();
  private ArrayList<Data> novelData = new ArrayList<>();
  private ArrayList<Data> fableData = new ArrayList<>();
  private ArrayList<Data> classicsData = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_category);

    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }

    try {
      getCategoryDetails();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override public void onContentChanged() {
    super.onContentChanged();
    maceraRecyclerView = (RecyclerView) findViewById(R.id.adventureRecyclerView);
    scienceRecyclerView = (RecyclerView) findViewById(R.id.scienceRecyclerView);
    classicsRecyclerView = (RecyclerView) findViewById(R.id.classicsRecyclerView);
    historyRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
    fearRecyclerView = (RecyclerView) findViewById(R.id.fearRecyclerView);
    personalDevelopmentRecyclerView =
        (RecyclerView) findViewById(R.id.personalDevelopmentRecyclerView);
    novelRecyclerView = (RecyclerView) findViewById(R.id.novalRecyclerView);
    fableRecyclerView = (RecyclerView) findViewById(R.id.fableRecyclerView);
  }

  private void getCategoryDetails() throws IOException, JSONException {
    URL url = new URL("https://heybook.online/api.php");
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    connection.setReadTimeout(10000);
    connection.setConnectTimeout(15000);
    connection.setRequestMethod("POST");
    connection.setDoInput(true);
    connection.setDoOutput(true);

    HashMap<String, String> params = new HashMap<>();
    params.put("request", "request");
    params.put("requestValue", "books");

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
        final JSONArray category = jsonData.getJSONArray("data");
        Log.d("category", jsonData.toString());

        for (int i = 0; i < category.length(); i++) {
          final JSONObject catData = category.getJSONObject(i);
          if (catData.getString("category_title").equals("Türk Edebiyatı Klasikleri")) {
            Log.d("Log","1");
            classicsData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                catData.getString("photo"), catData.getString("author_title"),
                catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, classicsData);
            RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            classicsRecyclerView.setLayoutManager(layoutManager);
            classicsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            classicsRecyclerView.setAdapter(categoryAdapter);
          } else if (catData.getString("category_title").equals("Bilim Kurgu")) {
            Log.d("Log","2");
            scienceData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                catData.getString("photo"), catData.getString("author_title"),
                catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, scienceData);
            RecyclerView.LayoutManager scienceLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            scienceRecyclerView.setLayoutManager(scienceLayoutManager);
            scienceRecyclerView.setItemAnimator(new DefaultItemAnimator());
            scienceRecyclerView.setAdapter(categoryAdapter);
          } else if (catData.getString("category_title").equals("Tarih")) {
            Log.d("Log","3");
            historyData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                catData.getString("photo"), catData.getString("author_title"),
                catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, historyData);
            RecyclerView.LayoutManager historyLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            historyRecyclerView.setLayoutManager(historyLayoutManager);
            historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
            historyRecyclerView.setAdapter(categoryAdapter);
          } else if (catData.getString("category_title").equals("Macera")) {
            Log.d("Log","4");
            adventureData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                catData.getString("photo"), catData.getString("author_title"),
                catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, adventureData);
            RecyclerView.LayoutManager adventureLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            maceraRecyclerView.setLayoutManager(adventureLayoutManager);
            maceraRecyclerView.setItemAnimator(new DefaultItemAnimator());
            maceraRecyclerView.setAdapter(categoryAdapter);
          } else if(catData.getString("category_title").equals("Korku")){
            Log.d("Log","5");
            fearData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, fearData);
            RecyclerView.LayoutManager fearLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            fearRecyclerView.setLayoutManager(fearLayoutManager);
            fearRecyclerView.setItemAnimator(new DefaultItemAnimator());
            fearRecyclerView.setAdapter(categoryAdapter);
          } else if(catData.getString("category_title").equals("Kişisel Gelişim")){
            Log.d("Log","6");
            personalDevelopmentData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, personalDevelopmentData);
            RecyclerView.LayoutManager personelLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            personalDevelopmentRecyclerView.setLayoutManager(personelLayoutManager);
            personalDevelopmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
            personalDevelopmentRecyclerView.setAdapter(categoryAdapter);
          } else if(catData.getString("category_title").equals("Roman")){
            Log.d("Log","7");
            novelData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, novelData);
            RecyclerView.LayoutManager novelLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            novelRecyclerView.setLayoutManager(novelLayoutManager);
            novelRecyclerView.setItemAnimator(new DefaultItemAnimator());
            novelRecyclerView.setAdapter(categoryAdapter);
          } else if(catData.getString("category_title").equals("Öykü")){
            Log.d("Log","8");
            fableData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                catData.getString("category_title")));
            categoryAdapter = new CategoryAdapter(this, fableData);
            RecyclerView.LayoutManager fabelLayoutManager =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            fableRecyclerView.setLayoutManager(fabelLayoutManager);
            fableRecyclerView.setItemAnimator(new DefaultItemAnimator());
            fableRecyclerView.setAdapter(categoryAdapter);
          }

        }

















      }
    }
  }

  private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
    StringBuilder result = new StringBuilder();
    boolean first = true;

    for (int i = 0; i < params.size(); i++) {
      if (first) {
        first = false;
      } else {
        result.append("&");
      }
      result.append(URLEncoder.encode(params.get("request"), "UTF-8"));
      result.append("=");
      result.append(URLEncoder.encode(params.get("requestValue"), "UTF-8"));
    }

    return result.toString();
  }
}
