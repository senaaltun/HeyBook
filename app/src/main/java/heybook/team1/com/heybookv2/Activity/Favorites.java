package heybook.team1.com.heybookv2.Activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import heybook.team1.com.heybookv2.Model.Book;
import heybook.team1.com.heybookv2.Model.Data;
import heybook.team1.com.heybookv2.R;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favorites extends BaseActivity {
  private ArrayList<Data> favoritesData;
  private FavoritesAdapter favoritesAdapter;
  private RecyclerView favoritesRecyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_favorites);
  }

  @Override public void onContentChanged() {
    super.onContentChanged();

    favoritesRecyclerView = (RecyclerView) findViewById(R.id.favoritesBookList);
  }

  public void getFavorites() {
    ApiClientInterface apiService = ApiClient.getClient().create(ApiClientInterface.class);

    Call<Book> call = apiService.getAllBooks();

    call.enqueue(new Callback<Book>() {
      @Override public void onResponse(Call<Book> call, Response<Book> response) {
        Book book = response.body();
        List<Data> data = book.getData();

        favoritesData = new ArrayList<>(data);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        favoritesRecyclerView.setLayoutManager(layoutManager);
        favoritesAdapter = new FavoritesAdapter(Favorites.this, favoritesData);

        favoritesRecyclerView.setAdapter(favoritesAdapter);
      }

      @Override public void onFailure(Call<Book> call, Throwable t) {
        Log.e("MyApp", "onFailure: " + t.toString());
      }
    });
  }
}
