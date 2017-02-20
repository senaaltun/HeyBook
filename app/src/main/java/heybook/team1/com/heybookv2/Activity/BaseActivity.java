package heybook.team1.com.heybookv2.Activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import heybook.team1.com.heybookv2.API.ApiClient;
import heybook.team1.com.heybookv2.API.ApiClientInterface;
import heybook.team1.com.heybookv2.Adapter.ResultBookAdapter;
import heybook.team1.com.heybookv2.Adapter.VitrinBookAdapter;
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sena Altun on 7.01.2017.
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<Data> resultDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

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


                        for(int i = 0;i<data.size();i++){
                            if(data.get(i).getBook_title().equalsIgnoreCase(query)
                                    || data.get(i).getBook_title().startsWith(query)){
                                resultDataList.add(data.get(i));
                            }
                        }
                        if(resultDataList.size() == 0){
                            Toast.makeText(getApplicationContext(),
                                    "Aradığınız kriterlere uygun kitap bulunamadı.",Toast.LENGTH_SHORT).show();
                        }else {
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

        }else if (id == R.id.nav_sepet){

        }else if (id == R.id.nav_gecmis){

        }else if (id == R.id.nav_ayarlar){

        }else if(id == R.id.nav_kayit){
            startActivity(new Intent(BaseActivity.this,Register.class));
        }else if(id == R.id.nav_cikis){
            startActivity(new Intent(BaseActivity.this,Logout.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
