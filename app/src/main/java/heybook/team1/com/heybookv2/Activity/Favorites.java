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
import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Adapter.CategoryAdapter;
import heybook.team1.com.heybookv2.Adapter.FavoritesAdapter;
import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.Model.Favorite;
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
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favorites extends BaseActivity {
  private List<Favorite> favoritesData = new ArrayList<>();
  private FavoritesAdapter favoritesAdapter;
  private RecyclerView favoritesRecyclerView;

  private String userId;
  private SessionManager sessionManager;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);

    if (android.os.Build.VERSION.SDK_INT > 9) {
      StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      StrictMode.setThreadPolicy(policy);
    }

    sessionManager = new SessionManager(getApplicationContext());

    HashMap<String, String> user = sessionManager.getUserDetails();
    userId = user.get("userId");

    try {
      getFavorites();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override public void onContentChanged() {
    super.onContentChanged();

    favoritesRecyclerView = (RecyclerView) findViewById(R.id.favoritesBookList);

  }

  public void getFavorites() throws IOException, JSONException {
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
    params.put("requestValue", "user_favorites");
    params.put("userId", userId);

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
        final JSONArray favorites = jsonData.getJSONArray("data");

        for(int i=0; i<favorites.length();i++){
          JSONObject f = (JSONObject) favorites.get(i);
          favoritesData.add(new Favorite(f.getString("book_title"),f.getString("author_title"),f.getString("duration"),f.getString("photo")));
        }
        favoritesAdapter = new FavoritesAdapter(Favorites.this,favoritesData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favoritesRecyclerView.setAdapter(favoritesAdapter);
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
    result.append(params.get("userId"));

    return result.toString();
  }
}
