package heybook.team1.com.heybookv2.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import heybook.team1.com.heybookv2.HeyBook;
import heybook.team1.com.heybookv2.Model.Favorite;
import heybook.team1.com.heybookv2.R;
import heybook.team1.com.heybookv2.SessionManager;
import java.util.ArrayList;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sena Altun on 7.01.2017.
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Data> resultDataList;

    private SearchView searchView;

    private SessionManager sessionManager;

    private SharedPreferences sharedPreferences;
    private ImageView profile;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    private boolean isLoggedProfile=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("HeybookPrefs",0);

        sessionManager = new SessionManager(BaseActivity.this);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                ApiClientInterface apiService =
                        ApiClient.getClient().create(ApiClientInterface.class);

                Call<Book> call = apiService.getAllBooks();

                call.enqueue(new Callback<Book>() {
                    @Override
                    public void onResponse(Call<Book> call, Response<Book> response) {
                        Book book = response.body();
                        List<Data> data = book.getData();
                        resultDataList = new ArrayList<>();


                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getBook_title().equalsIgnoreCase(query)
                                    || data.get(i).getBook_title().startsWith(query)) {
                                resultDataList.add(data.get(i));
                            }
                        }
                        if (resultDataList.size() == 0) {
                            Toast.makeText(getApplicationContext(),
                                    "Aradığınız kriterlere uygun kitap bulunamadı.", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(BaseActivity.this, SearchResult.class);
                            intent.putExtra("resultDataList", resultDataList);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<Book> call, Throwable t) {
                        Log.e("MyApp", "onFailure: " + t.toString());
                    }
                });


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_mic) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Lütfen sesli bir komut verin : ");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"tr-TR");
            startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vitrin) {
            startActivity(new Intent(BaseActivity.this, Vitrin.class));
        } else if (id == R.id.nav_kitaplarım) {

        } else if (id == R.id.nav_favorilerim) {
            startActivity(new Intent(BaseActivity.this, Favorites.class));
        } else if (id == R.id.nav_sepet) {
            startActivity(new Intent(BaseActivity.this,Sepet.class));
        } else if (id == R.id.nav_gecmis) {

        } else if (id == R.id.nav_ayarlar) {
            startActivity(new Intent(BaseActivity.this,Settings.class));
        } else if (id == R.id.nav_kayit) {
            startActivity(new Intent(BaseActivity.this, Register.class));
        } else if (id == R.id.nav_cikis) {
            startActivity(new Intent(BaseActivity.this, Logout.class));
        } else if( id == R.id.nav_login){
            startActivity(new Intent(BaseActivity.this,LoginActivity.class));
        } else if( id == R.id.nav_cat){
            startActivity(new Intent(BaseActivity.this,Category.class));
        } else if(id == R.id.fav){
            startActivity(new Intent(BaseActivity.this,Favorites.class));
        } else if(id == R.id.nav_search){
            startActivity(new Intent(BaseActivity.this,SearchActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if(matches.contains("vitrin")){
                startActivity(new Intent(BaseActivity.this,Vitrin.class));
            }else if(matches.contains("kayıt ol")){
                startActivity(new Intent(BaseActivity.this,Register.class));
            } else if(matches.contains("kategoriler")){
                startActivity(new Intent(BaseActivity.this,Category.class));
            } else if(matches.contains("sepet")){
                startActivity(new Intent(BaseActivity.this,Sepet.class));
            }

        }
    }
}