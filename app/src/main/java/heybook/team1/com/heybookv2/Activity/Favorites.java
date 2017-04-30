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
import android.widget.TextView;

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
    private ArrayList<Data> favoritesData = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private RecyclerView favoritesRecyclerView;

    private RecyclerView maceraRecyclerView;
    private RecyclerView scienceRecyclerView;
    private RecyclerView classicsRecyclerView;
    private RecyclerView historyRecyclerView;
    private RecyclerView fearRecyclerView;
    private RecyclerView personalDevelopmentRecyclerView;
    private RecyclerView novelRecyclerView;
    private RecyclerView fableRecyclerView;

    private ArrayList<Data> adventureData = new ArrayList<>();
    private ArrayList<Data> scienceData = new ArrayList<>();
    private ArrayList<Data> historyData = new ArrayList<>();
    private ArrayList<Data> fearData = new ArrayList<>();
    private ArrayList<Data> personalDevelopmentData = new ArrayList<>();
    private ArrayList<Data> novelData = new ArrayList<>();
    private ArrayList<Data> fableData = new ArrayList<>();
    private ArrayList<Data> classicsData = new ArrayList<>();

    private TextView classics;
    private TextView science;
    private TextView history;
    private TextView adventure;
    private TextView fear;
    private TextView development;
    private TextView novel;
    private TextView fabel;

    private String userId;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportActionBar().setTitle("Favoriler");

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

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        maceraRecyclerView = (RecyclerView) findViewById(R.id.favadventureRecyclerView);
        scienceRecyclerView = (RecyclerView) findViewById(R.id.favscienceRecyclerView);
        classicsRecyclerView = (RecyclerView) findViewById(R.id.favclassicsRecyclerView);
        historyRecyclerView = (RecyclerView) findViewById(R.id.favhistoryRecyclerView);
        fearRecyclerView = (RecyclerView) findViewById(R.id.favfearRecyclerView);
        personalDevelopmentRecyclerView = (RecyclerView) findViewById(R.id.favpersonalDevelopmentRecyclerView);
        novelRecyclerView = (RecyclerView) findViewById(R.id.favnovalRecyclerView);
        fableRecyclerView = (RecyclerView) findViewById(R.id.favfableRecyclerView);

        classics = (TextView)findViewById(R.id.classics);
        science = (TextView)findViewById(R.id.science);
        history = (TextView)findViewById(R.id.history);
        adventure = (TextView)findViewById(R.id.adventure);
        fear = (TextView)findViewById(R.id.fear);
        development = (TextView)findViewById(R.id.development);
        novel = (TextView)findViewById(R.id.novel);
        fabel = (TextView)findViewById(R.id.fabel);

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

                for (int i = 0; i < favorites.length(); i++) {
                    final JSONObject catData = favorites.getJSONObject(i);
                    if (catData.getString("category_title").equals("Türk Edebiyatı Klasikleri")) {
                        classicsData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                                catData.getString("photo"), catData.getString("author_title"),
                                catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, classicsData);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        classicsRecyclerView.setLayoutManager(layoutManager);
                        classicsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        classicsRecyclerView.setAdapter(favoritesAdapter);

                        if(classicsData.size() == 0){
                            classics.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Bilim Kurgu")) {
                        scienceData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                                catData.getString("photo"), catData.getString("author_title"),
                                catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, scienceData);
                        RecyclerView.LayoutManager scienceLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        scienceRecyclerView.setLayoutManager(scienceLayoutManager);
                        scienceRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        scienceRecyclerView.setAdapter(favoritesAdapter);
                        if(scienceData.size() == 0){
                            science.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Tarih")) {
                        historyData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                                catData.getString("photo"), catData.getString("author_title"),
                                catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, historyData);
                        RecyclerView.LayoutManager historyLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        historyRecyclerView.setLayoutManager(historyLayoutManager);
                        historyRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        historyRecyclerView.setAdapter(favoritesAdapter);
                        if(historyData.size() == 0){
                            history.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Macera")) {
                        adventureData.add(new Data(catData.getString("book_id"), catData.getString("book_title"),
                                catData.getString("photo"), catData.getString("author_title"),
                                catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, adventureData);
                        RecyclerView.LayoutManager adventureLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        maceraRecyclerView.setLayoutManager(adventureLayoutManager);
                        maceraRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        maceraRecyclerView.setAdapter(favoritesAdapter);
                        if(adventureData.size() == 0){
                            adventure.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Korku")) {
                        fearData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title")));
                        favoritesAdapter = new FavoritesAdapter(this, fearData);
                        RecyclerView.LayoutManager fearLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        fearRecyclerView.setLayoutManager(fearLayoutManager);
                        fearRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        fearRecyclerView.setAdapter(favoritesAdapter);
                        if(fearData.size() == 0){
                            fear.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Kişisel Gelişim")) {
                        personalDevelopmentData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, personalDevelopmentData);
                        RecyclerView.LayoutManager personelLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        personalDevelopmentRecyclerView.setLayoutManager(personelLayoutManager);
                        personalDevelopmentRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        personalDevelopmentRecyclerView.setAdapter(favoritesAdapter);
                        if(personalDevelopmentData.size() == 0){
                            development.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Roman")) {
                        novelData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, novelData);
                        RecyclerView.LayoutManager novelLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        novelRecyclerView.setLayoutManager(novelLayoutManager);
                        novelRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        novelRecyclerView.setAdapter(favoritesAdapter);
                        if(novelData.size() == 0){
                            novel.setVisibility(View.GONE);
                        }
                    } else if (catData.getString("category_title").equals("Öykü")) {
                        fableData.add(new Data(catData.getString("book_id"), catData.getString("book_title"), catData.getString("photo"),
                                catData.getString("author_title"), catData.getString("duration"), catData.getString("price"),
                                catData.getString("category_title"), catData.getString("star")));
                        favoritesAdapter = new FavoritesAdapter(this, fableData);
                        RecyclerView.LayoutManager fabelLayoutManager =
                                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                        fableRecyclerView.setLayoutManager(fabelLayoutManager);
                        fableRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        fableRecyclerView.setAdapter(favoritesAdapter);
                        if(fableData.size() == 0){
                            fabel.setVisibility(View.GONE);
                        }
                    }

                }
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
